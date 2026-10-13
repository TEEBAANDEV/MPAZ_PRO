package com.example.mpaz_pro.domain.validation

object FormValidators {

    fun validateUsername(input: String): ValidationResult {
        val trimmed = input.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult(false, "El nombre de usuario es obligatorio.")
            trimmed.length < 3 -> ValidationResult(false, "El usuario debe tener al menos 3 caracteres.")
            !trimmed.matches(Regex("^[a-zA-Z0-9_.-]+$")) -> ValidationResult(false, "Solo se permiten letras, números, puntos o guiones.")
            else -> ValidationResult(true)
        }
    }

    fun validatePassword(input: String): ValidationResult {
        return when {
            input.isEmpty() -> ValidationResult(false, "La contraseña es obligatoria.")
            input.length < 4 -> ValidationResult(false, "La contraseña debe tener al menos 4 caracteres.")
            else -> ValidationResult(true)
        }
    }

    fun validateObjectiveCode(input: String): ValidationResult {
        val trimmed = input.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult(false, "El código del OA es obligatorio (ej: OA 04).")
            !trimmed.uppercase().matches(Regex("^OA\\s*\\d{1,2}$")) -> {
                ValidationResult(false, "Formato inválido. Debe ser como 'OA 04' o 'OA 1'.")
            }
            else -> ValidationResult(true)
        }
    }

    fun validateTitle(input: String, fieldName: String = "El título"): ValidationResult {
        val trimmed = input.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult(false, "$fieldName no puede estar vacío.")
            trimmed.length < 5 -> ValidationResult(false, "$fieldName debe tener al menos 5 caracteres.")
            else -> ValidationResult(true)
        }
    }

    fun validateDescription(input: String, minLength: Int = 10): ValidationResult {
        val trimmed = input.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult(false, "La descripción pedagógica es requerida.")
            trimmed.length < minLength -> ValidationResult(false, "La descripción debe tener al menos $minLength caracteres.")
            else -> ValidationResult(true)
        }
    }

    fun validateMaxAttempts(input: String): ValidationResult {
        val number = input.toIntOrNull()
        return when {
            number == null -> ValidationResult(false, "Ingrese un número válido de intentos.")
            number < 1 -> ValidationResult(false, "El número de intentos debe ser al menos 1.")
            number > 10 -> ValidationResult(false, "El máximo permitido son 10 intentos.")
            else -> ValidationResult(true)
        }
    }

    fun validateDateFormat(input: String): ValidationResult {
        val trimmed = input.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult(false, "La fecha no puede estar vacía.")
            !trimmed.matches(Regex("^\\d{4}-\\d{2}-\\d{2}$")) -> {
                ValidationResult(false, "Formato requerido: AAAA-MM-DD (ej: 2026-04-30).")
            }
            else -> ValidationResult(true)
        }
    }

    fun validateTeacherObservation(input: String): ValidationResult {
        val trimmed = input.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult(false, "Debe ingresar una observación pedagógica antes de guardar.")
            trimmed.length < 8 -> ValidationResult(false, "La observación debe tener al menos 8 caracteres explicativos.")
            else -> ValidationResult(true)
        }
    }
}
