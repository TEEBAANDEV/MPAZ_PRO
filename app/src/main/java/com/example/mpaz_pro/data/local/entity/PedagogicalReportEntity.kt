package com.example.mpaz_pro.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pedagogical_reports")
data class PedagogicalReportEntity(
    @PrimaryKey val id: String,
    val studentId: String,
    val studentName: String,
    val courseName: String,
    val subjectName: String,
    val objectiveCode: String,
    val objectiveTitle: String,
    val globalScore: Int,
    val maxScore: Int,
    val percentage: Int,
    val strengths: String,
    val reinforcementAreas: String,
    val teacherObservation: String,
    val emissionDate: String,
    val isValidatedByTeacher: Boolean = false
)
