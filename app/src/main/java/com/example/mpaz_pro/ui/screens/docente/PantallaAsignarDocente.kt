package com.example.mpaz_pro.ui.screens.docente

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
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mpaz_pro.ui.components.BarraSuperiorMpaz
import com.example.mpaz_pro.ui.components.CampoTextoMpaz
import com.example.mpaz_pro.ui.viewmodel.DocenteViewModel

@Composable
fun PantallaAsignarDocente(
    viewModel: DocenteViewModel,
    alNavegarAtras: () -> Unit
) {
    val estadoFormulario by viewModel.formularioAsignacion.collectAsState()
    val estadoDesplazamiento = rememberScrollState()

    Scaffold(
        topBar = {
            BarraSuperiorMpaz(
                titulo = "Asignar Objetivo al Curso",
                subtitulo = "${estadoFormulario.codigoObjetivo} - 2° Básico A",
                alNavegarAtras = alNavegarAtras
            )
        }
    ) { rellenoInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(rellenoInterno)
                .verticalScroll(estadoDesplazamiento)
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
                        text = "${estadoFormulario.codigoObjetivo}: ${estadoFormulario.tituloObjetivo}",
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
                            text = "El contenido y la evaluación estarán visibles para el perfil estudiante.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = estadoFormulario.estaAsignado,
                        onCheckedChange = { viewModel.alAlternarAsignacion(it) }
                    )
                }
            }

            // Campos con Validación Visual por Campo Desacoplada
            CampoTextoMpaz(
                valor = estadoFormulario.entradaIntentosMaximos,
                alCambiarValor = { viewModel.alCambiarIntentosMaximos(it) },
                etiqueta = "Número Máximo de Intentos para la Evaluación",
                marcadorPosicion = "ej: 2",
                iconoInicio = Icons.Default.Repeat,
                opcionesTeclado = KeyboardOptions(keyboardType = KeyboardType.Number),
                mensajeError = estadoFormulario.errorIntentosMaximos
            )

            CampoTextoMpaz(
                valor = estadoFormulario.entradaFechaInicio,
                alCambiarValor = { viewModel.alCambiarFechaInicio(it) },
                etiqueta = "Fecha de Inicio de Disponibilidad",
                marcadorPosicion = "AAAA-MM-DD (ej: 2026-03-25)",
                iconoInicio = Icons.Default.CalendarToday,
                mensajeError = estadoFormulario.errorFechaInicio
            )

            CampoTextoMpaz(
                valor = estadoFormulario.entradaFechaFin,
                alCambiarValor = { viewModel.alCambiarFechaFin(it) },
                etiqueta = "Fecha Límite de Rendición",
                marcadorPosicion = "AAAA-MM-DD (ej: 2026-04-30)",
                iconoInicio = Icons.Default.CalendarToday,
                mensajeError = estadoFormulario.errorFechaFin
            )

            AnimatedVisibility(
                visible = estadoFormulario.mensajeExito != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = estadoFormulario.mensajeExito ?: "",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { viewModel.guardarAsignacion(alNavegarAtras) },
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
