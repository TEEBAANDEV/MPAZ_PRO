package com.example.mpaz_pro.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpaz_pro.data.local.entity.LearningObjectiveEntity
import com.example.mpaz_pro.data.local.entity.ObjectiveStatus
import com.example.mpaz_pro.data.local.entity.SubjectEntity
import com.example.mpaz_pro.data.repository.AppRepository
import com.example.mpaz_pro.domain.validation.FormValidators
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class UtpObjectiveFormState(
    val id: String = "",
    val subjectId: String = "sub_lenguaje",
    val codeInput: String = "",
    val titleInput: String = "",
    val descriptionInput: String = "",
    val unitInput: String = "Unidad 1",
    val axisInput: String = "Lectura",
    val referenceTextInput: String = "Texto Escolar Mineduc 2°",
    val status: ObjectiveStatus = ObjectiveStatus.BORRADOR,
    val codeError: String? = null,
    val titleError: String? = null,
    val descriptionError: String? = null,
    val isSuccessMessage: String? = null
)

class UtpViewModel(private val repository: AppRepository) : ViewModel() {

    val subjects: StateFlow<List<SubjectEntity>> = repository.getAllSubjects()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allObjectives: StateFlow<List<LearningObjectiveEntity>> = repository.getAllObjectives()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedSubjectId = MutableStateFlow<String?>(null)
    val selectedSubjectId: StateFlow<String?> = _selectedSubjectId.asStateFlow()

    private val _formState = MutableStateFlow(UtpObjectiveFormState())
    val formState: StateFlow<UtpObjectiveFormState> = _formState.asStateFlow()

    fun selectSubject(subjectId: String?) {
        _selectedSubjectId.value = subjectId
    }

    fun loadObjectiveForEditing(objective: LearningObjectiveEntity) {
        _formState.update {
            it.copy(
                id = objective.id,
                subjectId = objective.subjectId,
                codeInput = objective.code,
                titleInput = objective.title,
                descriptionInput = objective.description,
                unitInput = objective.unitTitle,
                axisInput = objective.curriculumAxis,
                referenceTextInput = objective.referenceText,
                status = objective.status,
                codeError = null,
                titleError = null,
                descriptionError = null,
                isSuccessMessage = null
            )
        }
    }

    fun resetForm(defaultSubjectId: String = "sub_lenguaje") {
        _formState.update {
            UtpObjectiveFormState(
                id = "obj_${System.currentTimeMillis()}",
                subjectId = defaultSubjectId
            )
        }
    }

    fun onCodeChanged(value: String) {
        val valResult = FormValidators.validateObjectiveCode(value)
        _formState.update {
            it.copy(
                codeInput = value,
                codeError = if (value.isNotBlank() && !valResult.isValid) valResult.errorMessage else null
            )
        }
    }

    fun onTitleChanged(value: String) {
        val valResult = FormValidators.validateTitle(value)
        _formState.update {
            it.copy(
                titleInput = value,
                titleError = if (value.isNotBlank() && !valResult.isValid) valResult.errorMessage else null
            )
        }
    }

    fun onDescriptionChanged(value: String) {
        val valResult = FormValidators.validateDescription(value)
        _formState.update {
            it.copy(
                descriptionInput = value,
                descriptionError = if (value.isNotBlank() && !valResult.isValid) valResult.errorMessage else null
            )
        }
    }

    fun onUnitChanged(value: String) {
        _formState.update { it.copy(unitInput = value) }
    }

    fun onAxisChanged(value: String) {
        _formState.update { it.copy(axisInput = value) }
    }

    fun onReferenceTextChanged(value: String) {
        _formState.update { it.copy(referenceTextInput = value) }
    }

    fun onStatusChanged(status: ObjectiveStatus) {
        _formState.update { it.copy(status = status) }
    }

    fun saveObjective(onSuccess: () -> Unit) {
        val form = _formState.value
        val codeVal = FormValidators.validateObjectiveCode(form.codeInput)
        val titleVal = FormValidators.validateTitle(form.titleInput)
        val descVal = FormValidators.validateDescription(form.descriptionInput)

        if (!codeVal.isValid || !titleVal.isValid || !descVal.isValid) {
            _formState.update {
                it.copy(
                    codeError = codeVal.errorMessage,
                    titleError = titleVal.errorMessage,
                    descriptionError = descVal.errorMessage
                )
            }
            return
        }

        viewModelScope.launch {
            val objective = LearningObjectiveEntity(
                id = if (form.id.isNotBlank()) form.id else "obj_${System.currentTimeMillis()}",
                subjectId = form.subjectId,
                code = form.codeInput.uppercase().trim(),
                title = form.titleInput.trim(),
                description = form.descriptionInput.trim(),
                unitTitle = form.unitInput.trim(),
                curriculumAxis = form.axisInput.trim(),
                referenceText = form.referenceTextInput.trim(),
                status = form.status,
                utpResponsible = "Jefatura UTP Marcela Paz",
                creationDate = "2026-03-24"
            )
            repository.insertObjective(objective)
            _formState.update { it.copy(isSuccessMessage = "Objetivo guardado exitosamente con estado ${form.status.name}") }
            onSuccess()
        }
    }

    fun quickUpdateStatus(objectiveId: String, newStatus: ObjectiveStatus) {
        viewModelScope.launch {
            repository.updateObjectiveStatus(objectiveId, newStatus)
        }
    }
}
