package com.example.mpaz_pro.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contents")
data class ContentEntity(
    @PrimaryKey val id: String,
    val objectiveId: String,
    val title: String,
    val writtenExplanation: String,
    val audioScript: String, // Texto que el motor nativo de Text-To-Speech reproducirá con voz clara
    val visualAidDescription: String,
    val visualAidIcon: String,
    val guidedExample: String,
    val instructions: String,
    val sequenceOrder: Int,
    val sourceOrigin: String = "Mineduc / Marcela Paz Didáctica"
)
