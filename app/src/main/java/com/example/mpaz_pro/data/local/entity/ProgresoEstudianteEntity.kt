package com.example.mpaz_pro.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "progreso_estudiante")
data class ProgresoEstudianteEntity(
    @PrimaryKey val id: String,
    val idEstudiante: String,
    val idObjetivo: String,
    val contenidoCompletado: Boolean = false,
    val desafiosCompletados: Int = 0,
    val totalDesafios: Int = 2,
    val intentosUsados: Int = 0,
    val ultimoPuntaje: Int = 0,
    val puntajeMaximo: Int = 20,
    val fechaUltimoIntento: String = "",
    val porcentaje: Int = 0,
    val textoEstado: String = "Pendiente" // Pendiente, En Progreso, Aprobado, Refuerzo Requerido
)
