package com.example.mpaz_pro.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpaz_pro.data.local.entity.LearningObjectiveEntity
import com.example.mpaz_pro.data.local.entity.PedagogicalReportEntity
import com.example.mpaz_pro.data.local.entity.StudentProgressEntity
import com.example.mpaz_pro.data.repository.AppRepository
import com.example.mpaz_pro.domain.validation.FormValidators
import com.example.mpaz_pro.native.ShareReportHelper
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class TeacherAssignmentFormState(
    val objectiveId: String = "",
    val objectiveCode: String = "",
    val objectiveTitle: String = "",
    val course: String = "2° Básico A",
    val maxAttemptsInput: String = "2",
    val startDateInput: String = "2026-03-25",
    val endDateInput: String = "2026-04-30",
    val isAssigned: Boolean = true,
    val maxAttemptsError: String? = null,
    val startDateError: String? = null,
    val endDateError: String? = null,
    val successMessage: String? = null
)

data class TeacherObservationFormState(
    val reportId: String = "",
    val observationInput: String = "",
    val observationError: String? = null,
    val isSavedSuccess: Boolean = false
)

class TeacherViewModel(private val repository: AppRepository) : ViewModel() {

    val authorizedObjectives: StateFlow<List<LearningObjectiveEntity>> =
        repository.getAuthorizedObjectives()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allProgress: StateFlow<List<StudentProgressEntity>> =
        repository.getAllProgress()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reports: StateFlow<List<PedagogicalReportEntity>> =
        repository.getAllReports()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _assignmentForm = MutableStateFlow(TeacherAssignmentFormState())
    val assignmentForm: StateFlow<TeacherAssignmentFormState> = _assignmentForm.asStateFlow()

    private val _observationForm = MutableStateFlow(TeacherObservationFormState())
    val observationForm: StateFlow<TeacherObservationFormState> = _observationForm.asStateFlow()

    fun loadObjectiveForAssignment(objective: LearningObjectiveEntity) {
        _assignmentForm.update {
            it.copy(
                objectiveId = objective.id,
                objectiveCode = objective.code,
                objectiveTitle = objective.title,
                course = objective.assignedCourse,
                maxAttemptsInput = objective.maxAttempts.toString(),
                startDateInput = objective.availabilityStartDate,
                endDateInput = objective.availabilityEndDate,
                isAssigned = objective.isAssignedToCourse,
                maxAttemptsError = null,
                startDateError = null,
                endDateError = null,
                successMessage = null
            )
        }
    }

    fun onMaxAttemptsChanged(value: String) {
        val valResult = FormValidators.validateMaxAttempts(value)
        _assignmentForm.update {
            it.copy(
                maxAttemptsInput = value,
                maxAttemptsError = if (value.isNotBlank() && !valResult.isValid) valResult.errorMessage else null
            )
        }
    }

    fun onStartDateChanged(value: String) {
        val valResult = FormValidators.validateDateFormat(value)
        _assignmentForm.update {
            it.copy(
                startDateInput = value,
                startDateError = if (value.isNotBlank() && !valResult.isValid) valResult.errorMessage else null
            )
        }
    }

    fun onEndDateChanged(value: String) {
        val valResult = FormValidators.validateDateFormat(value)
        _assignmentForm.update {
            it.copy(
                endDateInput = value,
                endDateError = if (value.isNotBlank() && !valResult.isValid) valResult.errorMessage else null
            )
        }
    }

    fun onAssignmentToggled(assigned: Boolean) {
        _assignmentForm.update { it.copy(isAssigned = assigned) }
    }

    fun saveAssignment(onSuccess: () -> Unit) {
        val state = _assignmentForm.value
        val attemptsVal = FormValidators.validateMaxAttempts(state.maxAttemptsInput)
        val startVal = FormValidators.validateDateFormat(state.startDateInput)
        val endVal = FormValidators.validateDateFormat(state.endDateInput)

        if (!attemptsVal.isValid || !startVal.isValid || !endVal.isValid) {
            _assignmentForm.update {
                it.copy(
                    maxAttemptsError = attemptsVal.errorMessage,
                    startDateError = startVal.errorMessage,
                    endDateError = endVal.errorMessage
                )
            }
            return
        }

        viewModelScope.launch {
            repository.updateAssignment(
                id = state.objectiveId,
                isAssigned = state.isAssigned,
                maxAttempts = state.maxAttemptsInput.toInt(),
                startDate = state.startDateInput.trim(),
                endDate = state.endDateInput.trim()
            )
            _assignmentForm.update { it.copy(successMessage = "Asignación guardada para el curso 2° Básico") }
            onSuccess()
        }
    }

    fun loadReportForObservation(report: PedagogicalReportEntity) {
        _observationForm.update {
            TeacherObservationFormState(
                reportId = report.id,
                observationInput = report.teacherObservation,
                observationError = null,
                isSavedSuccess = false
            )
        }
    }

    fun onObservationChanged(value: String) {
        val valResult = FormValidators.validateTeacherObservation(value)
        _observationForm.update {
            it.copy(
                observationInput = value,
                observationError = if (value.isNotBlank() && !valResult.isValid) valResult.errorMessage else null,
                isSavedSuccess = false
            )
        }
    }

    fun saveObservation(onSuccess: () -> Unit) {
        val form = _observationForm.value
        val valResult = FormValidators.validateTeacherObservation(form.observationInput)

        if (!valResult.isValid) {
            _observationForm.update { it.copy(observationError = valResult.errorMessage) }
            return
        }

        viewModelScope.launch {
            repository.updateTeacherObservation(form.reportId, form.observationInput.trim())
            _observationForm.update { it.copy(isSavedSuccess = true, observationError = null) }
            onSuccess()
        }
    }

    fun shareReportNatively(context: Context, report: PedagogicalReportEntity) {
        ShareReportHelper.shareReport(context, report)
    }
}
