package com.example.mpaz_pro.data.repository

import com.example.mpaz_pro.data.local.AppDatabase
import com.example.mpaz_pro.data.local.entity.*
import kotlinx.coroutines.flow.Flow

class AppRepository(private val database: AppDatabase) {

    // Auth & Users
    suspend fun login(username: String, password: String): UserEntity? =
        database.userDao().login(username, password)

    suspend fun getUserById(userId: String): UserEntity? =
        database.userDao().getUserById(userId)

    fun getUsersByRole(role: UserRole): Flow<List<UserEntity>> =
        database.userDao().getUsersByRole(role)

    // Subjects
    fun getAllSubjects(): Flow<List<SubjectEntity>> =
        database.subjectDao().getAllSubjects()

    suspend fun getSubjectById(id: String): SubjectEntity? =
        database.subjectDao().getSubjectById(id)

    // Objectives (UTP & Teacher)
    fun getAllObjectives(): Flow<List<LearningObjectiveEntity>> =
        database.learningObjectiveDao().getAllObjectives()

    fun getObjectivesBySubject(subjectId: String): Flow<List<LearningObjectiveEntity>> =
        database.learningObjectiveDao().getObjectivesBySubject(subjectId)

    suspend fun getObjectiveById(id: String): LearningObjectiveEntity? =
        database.learningObjectiveDao().getObjectiveById(id)

    fun getAuthorizedObjectives(): Flow<List<LearningObjectiveEntity>> =
        database.learningObjectiveDao().getAuthorizedObjectives()

    fun getAssignedObjectivesForStudents(): Flow<List<LearningObjectiveEntity>> =
        database.learningObjectiveDao().getAssignedObjectivesForStudents()

    suspend fun insertObjective(objective: LearningObjectiveEntity) =
        database.learningObjectiveDao().insertObjective(objective)

    suspend fun updateObjective(objective: LearningObjectiveEntity) =
        database.learningObjectiveDao().updateObjective(objective)

    suspend fun updateObjectiveStatus(id: String, status: ObjectiveStatus) =
        database.learningObjectiveDao().updateObjectiveStatus(id, status)

    suspend fun updateAssignment(id: String, isAssigned: Boolean, maxAttempts: Int, startDate: String, endDate: String) =
        database.learningObjectiveDao().updateAssignment(id, isAssigned, maxAttempts, startDate, endDate)

    // Didactic Contents
    fun getContentsByObjective(objectiveId: String): Flow<List<ContentEntity>> =
        database.contentDao().getContentsByObjective(objectiveId)

    suspend fun getFirstContentForObjective(objectiveId: String): ContentEntity? =
        database.contentDao().getFirstContentForObjective(objectiveId)

    suspend fun insertContent(content: ContentEntity) =
        database.contentDao().insertContent(content)

    // Challenges
    fun getChallengesByObjective(objectiveId: String): Flow<List<ChallengeEntity>> =
        database.challengeDao().getChallengesByObjective(objectiveId)

    // Exam Questions
    fun getExamQuestionsByObjective(objectiveId: String): Flow<List<ExamQuestionEntity>> =
        database.examQuestionDao().getQuestionsByObjective(objectiveId)

    // Student Progress
    fun getProgressByStudent(studentId: String): Flow<List<StudentProgressEntity>> =
        database.studentProgressDao().getProgressByStudent(studentId)

    suspend fun getProgressForObjective(studentId: String, objectiveId: String): StudentProgressEntity? =
        database.studentProgressDao().getProgressForObjective(studentId, objectiveId)

    fun getAllProgress(): Flow<List<StudentProgressEntity>> =
        database.studentProgressDao().getAllProgress()

    suspend fun saveProgress(progress: StudentProgressEntity) =
        database.studentProgressDao().insertProgress(progress)

    // Pedagogical Reports
    fun getAllReports(): Flow<List<PedagogicalReportEntity>> =
        database.pedagogicalReportDao().getAllReports()

    suspend fun getReportById(reportId: String): PedagogicalReportEntity? =
        database.pedagogicalReportDao().getReportById(reportId)

    fun getReportsForStudent(studentId: String): Flow<List<PedagogicalReportEntity>> =
        database.pedagogicalReportDao().getReportsForStudent(studentId)

    suspend fun saveReport(report: PedagogicalReportEntity) =
        database.pedagogicalReportDao().insertReport(report)

    suspend fun updateTeacherObservation(reportId: String, observation: String) =
        database.pedagogicalReportDao().updateObservation(reportId, observation)
}
