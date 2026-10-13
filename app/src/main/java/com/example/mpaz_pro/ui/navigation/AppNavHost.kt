package com.example.mpaz_pro.ui.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mpaz_pro.data.local.entity.UserRole
import com.example.mpaz_pro.ui.screens.LoginScreen
import com.example.mpaz_pro.ui.screens.student.StudentChallengeScreen
import com.example.mpaz_pro.ui.screens.student.StudentContentScreen
import com.example.mpaz_pro.ui.screens.student.StudentExamScreen
import com.example.mpaz_pro.ui.screens.student.StudentHomeScreen
import com.example.mpaz_pro.ui.screens.teacher.TeacherAssignScreen
import com.example.mpaz_pro.ui.screens.teacher.TeacherHomeScreen
import com.example.mpaz_pro.ui.screens.teacher.TeacherReportDetailScreen
import com.example.mpaz_pro.ui.screens.utp.UtpEditObjectiveScreen
import com.example.mpaz_pro.ui.screens.utp.UtpHomeScreen
import com.example.mpaz_pro.ui.viewmodel.AuthViewModel
import com.example.mpaz_pro.ui.viewmodel.StudentViewModel
import com.example.mpaz_pro.ui.viewmodel.TeacherViewModel
import com.example.mpaz_pro.ui.viewmodel.UtpViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    utpViewModel: UtpViewModel,
    teacherViewModel: TeacherViewModel,
    studentViewModel: StudentViewModel,
    modifier: Modifier = Modifier
) {
    val authState by authViewModel.uiState.collectAsState()
    val assignedObjectives by studentViewModel.assignedObjectives.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = { role ->
                    when (role) {
                        UserRole.UTP -> navController.navigate(Screen.UtpHome.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                        UserRole.DOCENTE -> navController.navigate(Screen.TeacherHome.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                        UserRole.ESTUDIANTE -> navController.navigate(Screen.StudentHome.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        // --- UTP ---
        composable(Screen.UtpHome.route) {
            UtpHomeScreen(
                utpViewModel = utpViewModel,
                onNavigateToEdit = { objectiveId ->
                    navController.navigate(Screen.UtpEditObjective.createRoute(objectiveId))
                },
                onCreateNewObjective = {
                    navController.navigate(Screen.UtpEditObjective.createRoute("new"))
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.UtpEditObjective.route,
            arguments = listOf(navArgument("objectiveId") { type = NavType.StringType })
        ) {
            UtpEditObjectiveScreen(
                utpViewModel = utpViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- DOCENTE ---
        composable(Screen.TeacherHome.route) {
            TeacherHomeScreen(
                teacherViewModel = teacherViewModel,
                onNavigateToAssign = { objectiveId ->
                    navController.navigate(Screen.TeacherAssign.createRoute(objectiveId))
                },
                onNavigateToReport = { reportId ->
                    navController.navigate(Screen.TeacherReportDetail.createRoute(reportId))
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.TeacherAssign.route,
            arguments = listOf(navArgument("objectiveId") { type = NavType.StringType })
        ) {
            TeacherAssignScreen(
                teacherViewModel = teacherViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.TeacherReportDetail.route,
            arguments = listOf(navArgument("reportId") { type = NavType.StringType })
        ) { backStackEntry ->
            val reportId = backStackEntry.arguments?.getString("reportId") ?: ""
            TeacherReportDetailScreen(
                reportId = reportId,
                teacherViewModel = teacherViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- ESTUDIANTE (2° BÁSICO) ---
        composable(Screen.StudentHome.route) {
            StudentHomeScreen(
                currentUser = authState.currentUser,
                studentViewModel = studentViewModel,
                onNavigateToContent = { objectiveId ->
                    navController.navigate(Screen.StudentContent.createRoute(objectiveId))
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.StudentContent.route,
            arguments = listOf(navArgument("objectiveId") { type = NavType.StringType })
        ) { backStackEntry ->
            val objectiveId = backStackEntry.arguments?.getString("objectiveId") ?: ""
            StudentContentScreen(
                objectiveId = objectiveId,
                studentViewModel = studentViewModel,
                onNavigateToChallenges = { id ->
                    navController.navigate(Screen.StudentChallenge.createRoute(id))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.StudentChallenge.route,
            arguments = listOf(navArgument("objectiveId") { type = NavType.StringType })
        ) { backStackEntry ->
            val objectiveId = backStackEntry.arguments?.getString("objectiveId") ?: ""
            StudentChallengeScreen(
                objectiveId = objectiveId,
                studentViewModel = studentViewModel,
                onNavigateToExam = { id ->
                    navController.navigate(Screen.StudentExam.createRoute(id))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.StudentExam.route,
            arguments = listOf(navArgument("objectiveId") { type = NavType.StringType })
        ) { backStackEntry ->
            val objectiveId = backStackEntry.arguments?.getString("objectiveId") ?: ""
            StudentExamScreen(
                objectiveId = objectiveId,
                currentUser = authState.currentUser,
                assignedObjectives = assignedObjectives,
                studentViewModel = studentViewModel,
                onNavigateHome = {
                    navController.navigate(Screen.StudentHome.route) {
                        popUpTo(Screen.StudentHome.route) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
