package com.example.mpaz_pro.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpaz_pro.data.local.entity.UserEntity
import com.example.mpaz_pro.data.local.entity.UserRole
import com.example.mpaz_pro.data.repository.AppRepository
import com.example.mpaz_pro.domain.validation.FormValidators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val usernameInput: String = "",
    val passwordInput: String = "",
    val usernameError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val generalError: String? = null,
    val currentUser: UserEntity? = null
)

class AuthViewModel(private val repository: AppRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onUsernameChanged(value: String) {
        val validation = FormValidators.validateUsername(value)
        _uiState.update {
            it.copy(
                usernameInput = value,
                usernameError = if (value.isNotBlank() && !validation.isValid) validation.errorMessage else null,
                generalError = null
            )
        }
    }

    fun onPasswordChanged(value: String) {
        val validation = FormValidators.validatePassword(value)
        _uiState.update {
            it.copy(
                passwordInput = value,
                passwordError = if (value.isNotBlank() && !validation.isValid) validation.errorMessage else null,
                generalError = null
            )
        }
    }

    fun login(onSuccess: (UserRole) -> Unit) {
        val currentState = _uiState.value
        val usernameVal = FormValidators.validateUsername(currentState.usernameInput)
        val passwordVal = FormValidators.validatePassword(currentState.passwordInput)

        if (!usernameVal.isValid || !passwordVal.isValid) {
            _uiState.update {
                it.copy(
                    usernameError = usernameVal.errorMessage,
                    passwordError = passwordVal.errorMessage
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }
            val user = repository.login(currentState.usernameInput.trim(), currentState.passwordInput.trim())
            if (user != null) {
                _uiState.update { it.copy(isLoading = false, currentUser = user, generalError = null) }
                onSuccess(user.role)
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        generalError = "Credenciales incorrectas. Verifique usuario y contraseña."
                    )
                }
            }
        }
    }

    fun quickLoginAs(username: String, onSuccess: (UserRole) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }
            val user = repository.login(username, "1234")
            if (user != null) {
                _uiState.update {
                    it.copy(
                        usernameInput = user.username,
                        passwordInput = "1234",
                        usernameError = null,
                        passwordError = null,
                        currentUser = user,
                        isLoading = false
                    )
                }
                onSuccess(user.role)
            } else {
                _uiState.update { it.copy(isLoading = false, generalError = "Usuario demo no encontrado") }
            }
        }
    }

    fun logout() {
        _uiState.update { AuthUiState() }
    }
}
