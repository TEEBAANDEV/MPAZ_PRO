package com.example.mpaz_pro.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_progress")
data class StudentProgressEntity(
    @PrimaryKey val id: String,
    val studentId: String,
    val objectiveId: String,
    val completedContent: Boolean = false,
    val completedChallengesCount: Int = 0,
    val totalChallengesCount: Int = 2,
    val attemptsUsed: Int = 0,
    val lastScore: Int = 0,
    val maxScore: Int = 20,
    val lastAttemptDate: String = "",
    val percentage: Int = 0,
    val statusText: String = "Pendiente" // Pendiente, En Progreso, Aprobado, Refuerzo Requerido
)
