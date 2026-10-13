package com.example.mpaz_pro.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preguntas_evaluacion")
data class PreguntaEvaluacionEntity(
    @PrimaryKey val id: String,
    val idObjetivo: String,
    val numeroPregunta: Int,
    val textoPregunta: String,
    val audioPregunta: String,
    val opciones: List<String>,
    val indiceCorrecto: Int,
    val puntaje: Int = 10,
    val explicacion: String
)
