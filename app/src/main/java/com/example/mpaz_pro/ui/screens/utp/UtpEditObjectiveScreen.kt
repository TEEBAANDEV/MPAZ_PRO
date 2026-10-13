package com.example.mpaz_pro.ui.screens.utp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mpaz_pro.data.local.entity.ObjectiveStatus
import com.example.mpaz_pro.ui.components.MpazTextField
import com.example.mpaz_pro.ui.components.MpazTopBar
import com.example.mpaz_pro.ui.components.StatusBadge
import com.example.mpaz_pro.ui.viewmodel.UtpViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UtpEditObjectiveScreen(
    utpViewModel: UtpViewModel,
    onNavigateBack: () -> Unit
) {
    val formState by utpViewModel.formState.collectAsState()
    val scrollState = rememberScrollState()
    var statusExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            MpazTopBar(
                title = if (formState.id.isNotBlank()) "Editar Objetivo" else "Nuevo Objetivo",
                subtitle = "Jefatura UTP - Marcela Paz",
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
            Text(
                text = "Datos Curriculares y Trazabilidad",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Formulario Completo con Validación Visual por Campo (IE 2.1.2 & IE 2.2.1)
            MpazTextField(
                value = formState.codeInput,
                onValueChange = { utpViewModel.onCodeChanged(it) },
                label = "Código Curricular Oficial",
                placeholder = "ej: OA 04",
                leadingIcon = Icons.Default.Tag,
                errorMessage = formState.codeError
            )

            MpazTextField(
                value = formState.titleInput,
                onValueChange = { utpViewModel.onTitleChanged(it) },
                label = "Título del Objetivo de Aprendizaje",
                placeholder = "ej: Lectura guiada de cuentos breves",
                leadingIcon = Icons.Default.Bookmark,
                errorMessage = formState.titleError
            )

            MpazTextField(
                value = formState.descriptionInput,
                onValueChange = { utpViewModel.onDescriptionChanged(it) },
                label = "Descripción del Objetivo (Mineduc)",
                placeholder = "Describa detalladamente el aprendizaje esperado para 2° básico",
                leadingIcon = Icons.Default.Description,
                isSingleLine = false,
                errorMessage = formState.descriptionError
            )

            MpazTextField(
                value = formState.unitInput,
                onValueChange = { utpViewModel.onUnitChanged(it) },
                label = "Unidad Didáctica",
                placeholder = "ej: Unidad 1: Mundos de Papel",
                leadingIcon = Icons.Default.Layers
            )

            MpazTextField(
                value = formState.axisInput,
                onValueChange = { utpViewModel.onAxisChanged(it) },
                label = "Eje Curricular",
                placeholder = "ej: Lectura y Comprensión",
                leadingIcon = Icons.Default.Book
            )

            MpazTextField(
                value = formState.referenceTextInput,
                onValueChange = { utpViewModel.onReferenceTextChanged(it) },
                label = "Texto de Referencia / Licencia Mineduc",
                placeholder = "ej: Guía docente Mineduc 2° Básico",
                leadingIcon = Icons.Default.Book
            )

            // Selector de Estado Curricular
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Estado del Contenido Pedagógico:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = statusExpanded,
                        onExpandedChange = { statusExpanded = !statusExpanded }
                    ) {
                        OutlinedTextField(
                            value = formState.status.name,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = statusExpanded,
                            onDismissRequest = { statusExpanded = false }
                        ) {
                            ObjectiveStatus.values().forEach { st ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(st.name)
                                            StatusBadge(status = st)
                                        }
                                    },
                                    onClick = {
                                        utpViewModel.onStatusChanged(st)
                                        statusExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = formState.isSuccessMessage != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = formState.isSuccessMessage ?: "",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { utpViewModel.saveObjective(onNavigateBack) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Guardar Cambios Curriculares",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
