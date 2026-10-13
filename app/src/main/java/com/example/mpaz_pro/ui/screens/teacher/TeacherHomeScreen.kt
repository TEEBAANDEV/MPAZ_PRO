package com.example.mpaz_pro.ui.screens.teacher

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mpaz_pro.data.local.entity.LearningObjectiveEntity
import com.example.mpaz_pro.data.local.entity.PedagogicalReportEntity
import com.example.mpaz_pro.data.local.entity.StudentProgressEntity
import com.example.mpaz_pro.ui.components.MpazTopBar
import com.example.mpaz_pro.ui.components.StatusBadge
import com.example.mpaz_pro.ui.viewmodel.TeacherViewModel

@Composable
fun TeacherHomeScreen(
    teacherViewModel: TeacherViewModel,
    onNavigateToAssign: (String) -> Unit,
    onNavigateToReport: (String) -> Unit,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Objetivos UTP", "Monitoreo", "Informes")

    val authorizedObjectives by teacherViewModel.authorizedObjectives.collectAsState()
    val allProgress by teacherViewModel.allProgress.collectAsState()
    val reports by teacherViewModel.reports.collectAsState()

    Scaffold(
        topBar = {
            MpazTopBar(
                title = "Docente 2° Básico",
                subtitle = "Prof. Mario Silva - Marcela Paz",
                onLogout = onLogout
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, fontWeight = FontWeight.SemiBold) }
                    )
                }
            }

            when (selectedTab) {
                0 -> TeacherObjectivesTab(
                    objectives = authorizedObjectives,
                    onAssignClick = { objective ->
                        teacherViewModel.loadObjectiveForAssignment(objective)
                        onNavigateToAssign(objective.id)
                    }
                )
                1 -> TeacherProgressTab(progressList = allProgress)
                2 -> TeacherReportsTab(
                    reports = reports,
                    onReportClick = { report ->
                        teacherViewModel.loadReportForObservation(report)
                        onNavigateToReport(report.id)
                    }
                )
            }
        }
    }
}

@Composable
fun TeacherObjectivesTab(
    objectives: List<LearningObjectiveEntity>,
    onAssignClick: (LearningObjectiveEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Unidades Autorizadas por UTP (${objectives.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Seleccione un objetivo de aprendizaje para asignar al curso y configurar intentos de evaluación.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        items(objectives, key = { it.id }) { objective ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = objective.code,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        StatusBadge(status = objective.status)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = objective.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Unidad: ${objective.unitTitle}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (objective.isAssignedToCourse) "Asignado a: ${objective.assignedCourse} (${objective.maxAttempts} intentos)" else "No asignado a curso",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (objective.isAssignedToCourse) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline
                        )

                        Button(
                            onClick = { onAssignClick(objective) },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Configurar", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TeacherProgressTab(progressList: List<StudentProgressEntity>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Avance y Rendimiento por Objetivo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        if (progressList.isEmpty()) {
            item {
                Text(
                    text = "Aún no se registran actividades resueltas por estudiantes.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(progressList, key = { it.id }) { progress ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Estudiante: ${if (progress.studentId.contains("1")) "Lucas Martínez" else "Sofía Morales"}",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = "${progress.percentage}% Logro",
                            fontWeight = FontWeight.ExtraBold,
                            color = if (progress.percentage >= 60) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { progress.percentage / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        trackColor = MaterialTheme.colorScheme.outlineVariant,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Intentos usados: ${progress.attemptsUsed}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Puntaje: ${progress.lastScore}/${progress.maxScore} pts",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Text(
                        text = "Última fecha: ${progress.lastAttemptDate}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun TeacherReportsTab(
    reports: List<PedagogicalReportEntity>,
    onReportClick: (PedagogicalReportEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Informes Pedagógicos Consolidados",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Acceso exclusivo para docentes. Seleccione un informe para registrar observaciones u exportarlo.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        items(reports, key = { it.id }) { report ->
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onReportClick(report) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = report.studentName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        AssistChip(
                            onClick = { onReportClick(report) },
                            label = { Text(if (report.isValidatedByTeacher) "Validado" else "Pendiente obs.") }
                        )
                    }

                    Text(
                        text = "${report.subjectName} • ${report.objectiveCode}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Rendimiento: ${report.globalScore}/${report.maxScore} pts (${report.percentage}%)",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Observación docente: ${if (report.teacherObservation.isNotBlank()) report.teacherObservation else "(Sin registrar aún)"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (report.teacherObservation.isNotBlank()) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}
