package com.example.mpaz_pro.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpaz_pro.data.local.entity.*
import com.example.mpaz_pro.data.repository.AppRepository
import com.example.mpaz_pro.native.HapticFeedbackHelper
import com.example.mpaz_pro.native.TextToSpeechHelper
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ChallengeState(
    val currentChallengeIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswerChecked: Boolean = false,
    val isCorrect: Boolean = false,
    val score: Int = 0,
    val isCompleted: Boolean = false
)

data class ExamState(
    val currentQuestionIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val selectedAnswers: MutableMap<Int, Int> = mutableMapOf(), // questionIndex -> optionIndex
    val isFinished: Boolean = false,
    val finalScore: Int = 0,
    val maxScore: Int = 20,
    val correctCount: Int = 0,
    val totalCount: Int = 0,
    val attemptsRemaining: Int = 2,
    val canTakeExam: Boolean = true
)

class StudentViewModel(
    private val repository: AppRepository,
    private val ttsHelper: TextToSpeechHelper,
    private val hapticHelper: HapticFeedbackHelper
) : ViewModel() {

    val assignedObjectives: StateFlow<List<LearningObjectiveEntity>> =
        repository.getAssignedObjectivesForStudents()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val isSpeaking: StateFlow<Boolean> = ttsHelper.isSpeaking

    private val _currentContent = MutableStateFlow<ContentEntity?>(null)
    val currentContent: StateFlow<ContentEntity?> = _currentContent.asStateFlow()

    private val _currentChallenges = MutableStateFlow<List<ChallengeEntity>>(emptyList())
    val currentChallenges: StateFlow<List<ChallengeEntity>> = _currentChallenges.asStateFlow()

    private val _challengeState = MutableStateFlow(ChallengeState())
    val challengeState: StateFlow<ChallengeState> = _challengeState.asStateFlow()

    private val _currentExamQuestions = MutableStateFlow<List<ExamQuestionEntity>>(emptyList())
    val currentExamQuestions: StateFlow<List<ExamQuestionEntity>> = _currentExamQuestions.asStateFlow()

    private val _examState = MutableStateFlow(ExamState())
    val examState: StateFlow<ExamState> = _examState.asStateFlow()

    fun loadContentForObjective(objectiveId: String) {
        ttsHelper.stop()
        viewModelScope.launch {
            val content = repository.getFirstContentForObjective(objectiveId)
            _currentContent.value = content
        }
    }

    fun playAudio(text: String) {
        ttsHelper.speak(text)
    }

    fun stopAudio() {
        ttsHelper.stop()
    }

    // --- ACTIVIDADES / DESAFÍOS ---
    fun loadChallengesForObjective(objectiveId: String) {
        ttsHelper.stop()
        _challengeState.value = ChallengeState()
        viewModelScope.launch {
            repository.getChallengesByObjective(objectiveId).collect { list ->
                _currentChallenges.value = list
            }
        }
    }

    fun onChallengeOptionSelected(index: Int) {
        if (_challengeState.value.isAnswerChecked) return
        _challengeState.update { it.copy(selectedOptionIndex = index) }
    }

    fun checkChallengeAnswer() {
        val state = _challengeState.value
        val challenges = _currentChallenges.value
        if (challenges.isEmpty() || state.selectedOptionIndex == null) return

        val currentChallenge = challenges[state.currentChallengeIndex]
        val isCorrect = state.selectedOptionIndex == currentChallenge.correctIndex

        if (isCorrect) {
            hapticHelper.playSuccessFeedback()
        } else {
            hapticHelper.playErrorFeedback()
        }

        _challengeState.update {
            it.copy(
                isAnswerChecked = true,
                isCorrect = isCorrect,
                score = if (isCorrect) it.score + 10 else it.score
            )
        }
    }

    fun nextChallenge() {
        val state = _challengeState.value
        val challenges = _currentChallenges.value
        if (state.currentChallengeIndex + 1 < challenges.size) {
            _challengeState.update {
                it.copy(
                    currentChallengeIndex = it.currentChallengeIndex + 1,
                    selectedOptionIndex = null,
                    isAnswerChecked = false,
                    isCorrect = false
                )
            }
        } else {
            _challengeState.update { it.copy(isCompleted = true) }
            hapticHelper.playCelebrationFeedback()
        }
    }

    // --- PRUEBA FINAL ---
    fun loadExamForObjective(studentId: String, objective: LearningObjectiveEntity) {
        ttsHelper.stop()
        viewModelScope.launch {
            val existingProgress = repository.getProgressForObjective(studentId, objective.id)
            val attemptsUsed = existingProgress?.attemptsUsed ?: 0
            val remaining = (objective.maxAttempts - attemptsUsed).coerceAtLeast(0)
            val canTake = remaining > 0

            _examState.value = ExamState(
                attemptsRemaining = remaining,
                canTakeExam = canTake
            )

            repository.getExamQuestionsByObjective(objective.id).collect { questions ->
                _currentExamQuestions.value = questions
            }
        }
    }

    fun onExamOptionSelected(index: Int) {
        _examState.update { it.copy(selectedOptionIndex = index) }
    }

    fun nextExamQuestion() {
        val state = _examState.value
        val questions = _currentExamQuestions.value
        if (state.selectedOptionIndex == null) return

        state.selectedAnswers[state.currentQuestionIndex] = state.selectedOptionIndex!!

        if (state.currentQuestionIndex + 1 < questions.size) {
            val nextIdx = state.currentQuestionIndex + 1
            _examState.update {
                it.copy(
                    currentQuestionIndex = nextIdx,
                    selectedOptionIndex = state.selectedAnswers[nextIdx]
                )
            }
        }
    }

    fun submitExam(
        student: UserEntity,
        objective: LearningObjectiveEntity,
        subjectName: String
    ) {
        val state = _examState.value
        val questions = _currentExamQuestions.value
        if (state.selectedOptionIndex != null) {
            state.selectedAnswers[state.currentQuestionIndex] = state.selectedOptionIndex!!
        }

        var correctCount = 0
        var totalPoints = 0
        questions.forEachIndexed { idx, q ->
            val chosen = state.selectedAnswers[idx]
            if (chosen != null && chosen == q.correctIndex) {
                correctCount++
                totalPoints += q.points
            }
        }
        val maxPoints = questions.sumOf { it.points }.coerceAtLeast(20)
        val percentage = ((totalPoints.toDouble() / maxPoints.toDouble()) * 100).toInt()

        viewModelScope.launch {
            val existingProgress = repository.getProgressForObjective(student.id, objective.id)
            val newAttemptsUsed = (existingProgress?.attemptsUsed ?: 0) + 1
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val timestamp = dateFormat.format(Date())

            val updatedProgress = StudentProgressEntity(
                id = "prog_${student.id}_${objective.id}",
                studentId = student.id,
                objectiveId = objective.id,
                completedContent = true,
                completedChallengesCount = 2,
                totalChallengesCount = 2,
                attemptsUsed = newAttemptsUsed,
                lastScore = totalPoints,
                maxScore = maxPoints,
                lastAttemptDate = timestamp,
                percentage = percentage,
                statusText = if (percentage >= 60) "Aprobado" else "Requiere Refuerzo"
            )
            repository.saveProgress(updatedProgress)

            // Guardar reporte pedagógico reservado para el docente
            val report = PedagogicalReportEntity(
                id = "rep_${student.id}_${objective.id}",
                studentId = student.id,
                studentName = student.fullName,
                courseName = student.course,
                subjectName = subjectName,
                objectiveCode = objective.code,
                objectiveTitle = objective.title,
                globalScore = totalPoints,
                maxScore = maxPoints,
                percentage = percentage,
                strengths = if (percentage >= 60) "Logra resolver con éxito los ejercicios del objetivo evaluado." else "Participa activamente en la resolución guiada.",
                reinforcementAreas = if (percentage < 60) "Reforzar comprensión de instrucciones y repasar contenidos clave con el docente." else "Mantener práctica constante.",
                teacherObservation = "",
                emissionDate = timestamp,
                isValidatedByTeacher = false
            )
            repository.saveReport(report)

            val remaining = (objective.maxAttempts - newAttemptsUsed).coerceAtLeast(0)
            _examState.update {
                it.copy(
                    isFinished = true,
                    finalScore = totalPoints,
                    maxScore = maxPoints,
                    correctCount = correctCount,
                    totalCount = questions.size,
                    attemptsRemaining = remaining,
                    canTakeExam = remaining > 0
                )
            }

            if (percentage >= 60) {
                hapticHelper.playCelebrationFeedback()
            } else {
                hapticHelper.playErrorFeedback()
            }
        }
    }

    override fun onCleared() {
        ttsHelper.stop()
        super.onCleared()
    }
}
