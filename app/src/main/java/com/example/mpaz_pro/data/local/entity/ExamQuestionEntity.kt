package com.example.mpaz_pro.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exam_questions")
data class ExamQuestionEntity(
    @PrimaryKey val id: String,
    val objectiveId: String,
    val questionNumber: Int,
    val questionText: String,
    val audioQuestion: String,
    val options: List<String>,
    val correctIndex: Int,
    val points: Int = 10,
    val explanation: String
)
