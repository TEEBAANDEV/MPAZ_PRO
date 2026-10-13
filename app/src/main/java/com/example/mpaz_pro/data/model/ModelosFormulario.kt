package com.example.mpaz_pro.data.model

data class FormularioLoginModel(
    val rut: String = "",
    val clave: String = ""
)

data class FormularioLoginErrores(
    val rut: String? = null,
    val clave: String? = null
)

data class FormularioObjetivoModel(
    val idObjetivo: String = "",
    val idAsignatura: String = "lenguaje",
    val nombreAsignatura: String = "Lenguaje y Comunicación",
    val codigo: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val pregunta: String = "",
    val opciones: String = "",
    val indiceCorrecto: Int = 0,
    val estado: String = "AUTORIZADO"
)

data class FormularioObjetivoErrores(
    val codigo: String? = null,
    val titulo: String? = null,
    val descripcion: String? = null,
    val pregunta: String? = null
)

data class FormularioObservacionModel(
    val idInforme: String = "",
    val observacion: String = ""
)

data class FormularioObservacionErrores(
    val observacion: String? = null
)
