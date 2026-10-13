package com.example.mpaz_pro.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contenidos_didacticos")
data class ContenidoDidacticoEntity(
    @PrimaryKey val id: String,
    val idObjetivo: String,
    val titulo: String,
    val explicacionEscrita: String,
    val guionAudio: String, // Texto que el motor nativo de Text-To-Speech lee en voz alta
    val descripcionApoyoVisual: String,
    val iconoApoyoVisual: String,
    val ejemploGuiado: String,
    val instrucciones: String,
    val ordenSecuencia: Int,
    val origenMaterial: String = "Mineduc / Marcela Paz Didáctica"
)
