package com.example.mpaz_pro.ui.screens.teacher

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mpaz_pro.ui.components.MpazTextField
import com.example.mpaz_pro.ui.components.MpazTopBar
import com.example.mpaz_pro.ui.viewmodel.TeacherViewModel

@Composable
fun TeacherAssignScreen(
    teacherViewModel: TeacherViewModel,
    onNavigateBack: () -> Unit
) {
    val formState by teacherViewModel.assignmentForm.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            MpazTopBar(
                title = "Asignar Objetivo al Curso",
                subtitle = "${formState.objectiveCode} - 2° Básico A",
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Objetivo Curricular:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${formState.objectiveCode}: ${formState.objectiveTitle}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "Parámetros Pedagógicos de Disponibilidad",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // Switch de asignación
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Habilitar para Estudiantes",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "El contenido y la prueba estarán visibles para el perfil estudiante.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = formState.isAssigned,
                        onCheckedChange = { teacherViewModel.onAssignmentToggled(it) }
                    )
                }
            }

            // Formulario Validado Desacoplado (IE 2.1.2 & IE 2.2.1)
            MpazTextField(
                value = formState.maxAttemptsInput,
                onValueChange = { teacherViewModel.onMaxAttemptsChanged(it) },
                label = "Número Máximo de Intentos para la Prueba",
                placeholder = "ej: 2",
                leadingIcon = Icons.Default.Repeat,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                errorMessage = formState.maxAttemptsError
            )

            MpazTextField(
                value = formState.startDateInput,
                onValueChange = { teacherViewModel.onStartDateChanged(it) },
                label = "Fecha de Inicio de Disponibilidad",
                placeholder = "AAAA-MM-DD (ej: 2026-03-25)",
                leadingIcon = Icons.Default.CalendarToday,
                errorMessage = formState.startDateError
            )

            MpazTextField(
                value = formState.endDateInput,
                onValueChange = { teacherViewModel.onEndDateChanged(it) },
                label = "Fecha Límite de Rendición",
                placeholder = "AAAA-MM-DD (ej: 2026-04-30)",
                leadingIcon = Icons.Default.CalendarToday,
                errorMessage = formState.endDateError
            )

            AnimatedVisibility(
                visible = formState.successMessage != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = formState.successMessage ?: "",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { teacherViewModel.saveAssignment(onNavigateBack) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Guardar Parámetros de Asignación",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
