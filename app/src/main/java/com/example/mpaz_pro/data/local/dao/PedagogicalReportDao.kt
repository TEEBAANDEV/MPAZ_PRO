package com.example.mpaz_pro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mpaz_pro.data.local.entity.PedagogicalReportEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PedagogicalReportDao {
    @Query("SELECT * FROM pedagogical_reports")
    fun getAllReports(): Flow<List<PedagogicalReportEntity>>

    @Query("SELECT * FROM pedagogical_reports WHERE id = :reportId LIMIT 1")
    suspend fun getReportById(reportId: String): PedagogicalReportEntity?

    @Query("SELECT * FROM pedagogical_reports WHERE studentId = :studentId")
    fun getReportsForStudent(studentId: String): Flow<List<PedagogicalReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: PedagogicalReportEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReports(reports: List<PedagogicalReportEntity>)

    @Update
    suspend fun updateReport(report: PedagogicalReportEntity)

    @Query("UPDATE pedagogical_reports SET teacherObservation = :observation, isValidatedByTeacher = 1 WHERE id = :reportId")
    suspend fun updateObservation(reportId: String, observation: String)
}
