package com.example.mpaz_pro.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ChallengeType {
    SELECCION_MULTIPLE,
    COMPLETACION,
    ASOCIACION
}

@Entity(tableName = "challenges")
data class ChallengeEntity(
    @PrimaryKey val id: String,
    val objectiveId: String,
    val title: String,
    val type: ChallengeType,
    val prompt: String,
    val audioPrompt: String,
    val options: List<String>,
    val correctIndex: Int,
    val feedbackExplanation: String,
    val difficulty: String = "Fácil", // Fácil, Medio, Desafío
    val sequenceOrder: Int
)
