package com.example.mpaz_pro.ui.screens.teacher

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mpaz_pro.data.local.entity.PedagogicalReportEntity
import com.example.mpaz_pro.ui.components.MpazTextField
import com.example.mpaz_pro.ui.components.MpazTopBar
import com.example.mpaz_pro.ui.viewmodel.TeacherViewModel

@Composable
fun TeacherReportDetailScreen(
    reportId: String,
    teacherViewModel: TeacherViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val reports by teacherViewModel.reports.collectAsState()
    val report = reports.find { it.id == reportId }
    val obsState by teacherViewModel.observationForm.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            MpazTopBar(
                title = "Informe Pedagógico",
                subtitle = report?.studentName ?: "Detalle",
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        if (report == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("Informe no encontrado.")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tarjeta de Identificación del Estudiante
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = report.studentName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${report.courseName} • Escuela Marcela Paz (San Bernardo)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "${report.subjectName} (${report.objectiveCode})",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = report.objectiveTitle,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Rendimiento Consolidado: ${report.globalScore}/${report.maxScore} pts (${report.percentage}%)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (report.percentage >= 60) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }
            }

            // Diagnóstico Pedagógico
            Card(
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Diagnóstico de Fortalezas:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = report.strengths,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                    )

                    Text(
                        text = "Áreas de Refuerzo Prioritarias:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = report.reinforcementAreas,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Formulario de Observaciones Docente con Validación Visual Centralizada (IE 2.1.2 & IE 2.2.1)
            Card(
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Registro de Observaciones Pedagógicas Docente",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "De acuerdo a la pauta del colegio, ingrese su evaluación cualitativa sobre el estudiante.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    MpazTextField(
                        value = obsState.observationInput,
                        onValueChange = { teacherViewModel.onObservationChanged(it) },
                        label = "Observación Pedagógica del Profesor",
                        placeholder = "Escriba aquí sus observaciones sobre el avance del estudiante...",
                        leadingIcon = Icons.Default.Comment,
                        isSingleLine = false,
                        errorMessage = obsState.observationError
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { teacherViewModel.saveObservation {} },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar y Validar Observación")
                    }

                    AnimatedVisibility(
                        visible = obsState.isSavedSuccess,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Row(
                            modifier = Modifier.padding(top = 8.dp),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "¡Observación guardada y validada con éxito!",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Recurso Nativo: Compartir / Exportar Informe (IE 2.4.1)
            OutlinedButton(
                onClick = { teacherViewModel.shareReportNatively(context, report) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Descargar / Compartir Informe (Nativo)",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
