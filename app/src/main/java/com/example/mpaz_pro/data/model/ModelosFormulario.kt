package com.example.mpaz_pro.data.model

import com.example.mpaz_pro.data.local.entity.EstadoObjetivo

/**
 * Modelos de datos para formularios y sus respectivos estados de error.
 * Sigue la arquitectura y estructura explicada en clase y en los ejemplos
 * (MiAppFormulario y Arquitectura de una APP.docx).
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
    val idAsignatura: String = "asig_lenguaje",
    val codigoMinisterial: String = "",
    val descripcion: String = "",
    val estado: EstadoObjetivo = EstadoObjetivo.BORRADOR,
    val fechaPlanificacion: Long? = System.currentTimeMillis()
)

data class FormularioObjetivoErrores(
    val codigoMinisterial: String? = null,
    val descripcion: String? = null,
    val fecha: String? = null
)

// --- 3. Formulario de Asignación e Informe Pedagógico (Perfil Docente) ---
data class FormularioInformeModel(
    val idObjetivo: String = "",
    val idEstudiante: String = "estudiante_2b_01",
    val observacion: String = "",
    val porcentajeLogro: String = "85",
    val asistencia: String = "95",
    val fechaSeguimiento: Long? = System.currentTimeMillis(),
    val requiereRefuerzo: Boolean = false
)

data class FormularioInformeErrores(
    val observacion: String? = null,
    val porcentajeLogro: String? = null,
    val asistencia: String? = null,
    val fecha: String? = null
)
