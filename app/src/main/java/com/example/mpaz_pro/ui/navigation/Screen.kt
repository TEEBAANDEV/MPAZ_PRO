package com.example.mpaz_pro.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")

    // Perfil UTP
    object UtpHome : Screen("utp_home")
    object UtpSubjectObjectives : Screen("utp_subject/{subjectId}") {
        fun createRoute(subjectId: String) = "utp_subject/$subjectId"
    }
    object UtpEditObjective : Screen("utp_edit_objective/{objectiveId}") {
        fun createRoute(objectiveId: String) = "utp_edit_objective/$objectiveId"
    }

    // Perfil Docente
    object TeacherHome : Screen("teacher_home")
    object TeacherAssign : Screen("teacher_assign/{objectiveId}") {
        fun createRoute(objectiveId: String) = "teacher_assign/$objectiveId"
    }
    object TeacherProgress : Screen("teacher_progress")
    object TeacherReportDetail : Screen("teacher_report/{reportId}") {
        fun createRoute(reportId: String) = "teacher_report/$reportId"
    }

    // Perfil Estudiante (2° Básico)
    object StudentHome : Screen("student_home")
    object StudentContent : Screen("student_content/{objectiveId}") {
        fun createRoute(objectiveId: String) = "student_content/$objectiveId"
    }
    object StudentChallenge : Screen("student_challenge/{objectiveId}") {
        fun createRoute(objectiveId: String) = "student_challenge/$objectiveId"
    }
    object StudentExam : Screen("student_exam/{objectiveId}") {
        fun createRoute(objectiveId: String) = "student_exam/$objectiveId"
    }
}
