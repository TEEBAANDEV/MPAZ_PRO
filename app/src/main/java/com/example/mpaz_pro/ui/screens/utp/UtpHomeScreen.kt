package com.example.mpaz_pro.ui.screens.utp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mpaz_pro.data.local.entity.LearningObjectiveEntity
import com.example.mpaz_pro.data.local.entity.ObjectiveStatus
import com.example.mpaz_pro.ui.components.MpazTopBar
import com.example.mpaz_pro.ui.components.StatusBadge
import com.example.mpaz_pro.ui.viewmodel.UtpViewModel

@Composable
fun UtpHomeScreen(
    utpViewModel: UtpViewModel,
    onNavigateToEdit: (String) -> Unit,
    onCreateNewObjective: () -> Unit,
    onLogout: () -> Unit
) {
    val subjects by utpViewModel.subjects.collectAsState()
    val objectives by utpViewModel.allObjectives.collectAsState()
    val selectedSubjectId by utpViewModel.selectedSubjectId.collectAsState()

    val filteredObjectives = remember(objectives, selectedSubjectId) {
        if (selectedSubjectId == null) objectives
        else objectives.filter { it.subjectId == selectedSubjectId }
    }

    Scaffold(
        topBar = {
            MpazTopBar(
                title = "Jefatura UTP - MPAZ PRO",
                subtitle = "Administración Curricular de 2° Básico",
                onLogout = onLogout
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    utpViewModel.resetForm(selectedSubjectId ?: "sub_lenguaje")
                    onCreateNewObjective()
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Objetivo de Aprendizaje")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Asignaturas Principales (4)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Selector horizontal de asignaturas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedSubjectId == null,
                        onClick = { utpViewModel.selectSubject(null) },
                        label = { Text("Todas") }
                    )
                    subjects.forEach { subject ->
                        FilterChip(
                            selected = selectedSubjectId == subject.id,
                            onClick = { utpViewModel.selectSubject(subject.id) },
                            label = { Text(subject.code) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Objetivos y Unidades Didácticas (${filteredObjectives.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            items(filteredObjectives, key = { it.id }) { objective ->
                UtpObjectiveCard(
                    objective = objective,
                    onEditClick = {
                        utpViewModel.loadObjectiveForEditing(objective)
                        onNavigateToEdit(objective.id)
                    },
                    onQuickApprove = {
                        utpViewModel.quickUpdateStatus(objective.id, ObjectiveStatus.AUTORIZADO)
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun UtpObjectiveCard(
    objective: LearningObjectiveEntity,
    onEditClick: () -> Unit,
    onQuickApprove: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = objective.code,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• ${objective.unitTitle}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                StatusBadge(status = objective.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = objective.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = objective.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Eje: ${objective.curriculumAxis}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (objective.status == ObjectiveStatus.EN_REVISION || objective.status == ObjectiveStatus.BORRADOR) {
                        FilledTonalButton(
                            onClick = onQuickApprove,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Autorizar", style = MaterialTheme.typography.labelSmall)
                        }
                    }

                    IconButton(onClick = onEditClick) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar objetivo",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
