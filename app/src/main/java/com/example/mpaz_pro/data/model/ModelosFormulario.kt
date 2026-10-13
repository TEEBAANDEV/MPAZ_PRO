package com.example.mpaz_pro.data.model

/**
 * Modelos de datos para formularios y sus estados de error.
 * Basado estrictamente en el patrón enseñado por el docente en MiAppFormulario.
 */

// --- 1. Formulario de Inicio de Sesión ---
data class FormularioLoginModel(
    val rut: String = "",
    val clave: String = ""
)

data class FormularioLoginErrores(
    val rut: String? = null,
    val clave: String? = null
)

// --- 2. Formulario de Objetivos de Aprendizaje (Perfil UTP) ---
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

// --- 3. Formulario de Observación Docente (Perfil Docente) ---
data class FormularioObservacionModel(
    val idInforme: String = "",
    val observacion: String = ""
)

data class FormularioObservacionErrores(
    val observacion: String? = null
)
