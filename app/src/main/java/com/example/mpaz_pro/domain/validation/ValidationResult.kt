package com.example.mpaz_pro.domain.validation

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)
