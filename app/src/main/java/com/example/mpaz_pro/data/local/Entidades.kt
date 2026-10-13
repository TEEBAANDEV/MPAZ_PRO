package com.example.mpaz_pro.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidades consolidadas y simplificadas de la base de datos Room,
 * modeladas en español para el caso MPAZ PRO (Escuela Marcela Paz).
 */

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey val nombreUsuario: String,
    val clave: String,
    val nombreCompleto: String,
    val rol: String // "UTP", "DOCENTE", "ESTUDIANTE"
)

@Entity(tableName = "objetivos")
data class ObjetivoEntity(
    @PrimaryKey val id: String,
    val idAsignatura: String, // "lenguaje", "matematica", "ciencias", "historia"
    val nombreAsignatura: String,
    val codigo: String, // ej: "OA 04"
    val titulo: String,
    val descripcion: String, // Contenido didáctico leído por Text-To-Speech
    val pregunta: String, // Pregunta del desafío para el alumno
    val opciones: String, // Opciones separadas por coma
    val indiceCorrecto: Int, // Índice de la respuesta correcta (0, 1, 2)
    val estado: String = "AUTORIZADO", // "BORRADOR", "AUTORIZADO"
    val asignadoAlCurso: Boolean = true,
    val intentosMaximos: Int = 2
)

@Entity(tableName = "informes")
data class InformeEntity(
    @PrimaryKey val id: String,
    val idEstudiante: String,
    val nombreEstudiante: String,
    val curso: String = "2° Básico A",
    val asignatura: String,
    val porcentajeLogro: Int,
    val desafiosCompletados: Int,
    val observacionDocente: String,
    val fecha: String
)
