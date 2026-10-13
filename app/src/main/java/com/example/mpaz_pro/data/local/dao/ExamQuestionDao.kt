package com.example.mpaz_pro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mpaz_pro.data.local.entity.ExamQuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamQuestionDao {
    @Query("SELECT * FROM exam_questions WHERE objectiveId = :objectiveId ORDER BY questionNumber ASC")
    fun getQuestionsByObjective(objectiveId: String): Flow<List<ExamQuestionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamQuestions(questions: List<ExamQuestionEntity>)
}
