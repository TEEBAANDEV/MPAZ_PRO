package com.example.mpaz_pro.domain.validation

data class ResultadoValidacion(
    val esValido: Boolean,
    val mensajeError: String? = null
)
