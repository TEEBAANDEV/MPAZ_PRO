package com.example.mpaz_pro.ui.screens.docente

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mpaz_pro.ui.components.BarraSuperiorMpaz
import com.example.mpaz_pro.ui.components.CampoTextoMpaz
import com.example.mpaz_pro.ui.viewmodel.DocenteViewModel

@Composable
fun PantallaDetalleInformeDocente(
    idInforme: String,
    viewModel: DocenteViewModel,
    alNavegarAtras: () -> Unit
) {
    val contexto = LocalContext.current
    val informes by viewModel.informes.collectAsState()
    val informe = informes.find { it.id == idInforme }
    val estadoObs by viewModel.formularioObservacion.collectAsState()
    val estadoDesplazamiento = rememberScrollState()

    Scaffold(
        topBar = {
            BarraSuperiorMpaz(
                titulo = "Informe Pedagógico",
                subtitulo = informe?.nombreEstudiante ?: "Detalle",
                alNavegarAtras = alNavegarAtras
            )
        }
    ) { rellenoInterno ->
        if (informe == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(rellenoInterno),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("Informe no encontrado.")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(rellenoInterno)
                .verticalScroll(estadoDesplazamiento)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = informe.nombreEstudiante,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${informe.nombreCurso} • Escuela Marcela Paz (San Bernardo)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "${informe.nombreAsignatura} (${informe.codigoObjetivo})",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = informe.tituloObjetivo,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Rendimiento Consolidado: ${informe.puntajeGlobal}/${informe.puntajeMaximo} pts (${informe.porcentaje}%)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (informe.porcentaje >= 60) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
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
                        text = informe.fortalezas,
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
                        text = informe.areasRefuerzo,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Formulario de Observaciones Docente
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

                    CampoTextoMpaz(
                        valor = estadoObs.entradaObservacion,
                        alCambiarValor = { viewModel.alCambiarObservacion(it) },
                        etiqueta = "Observación Pedagógica del Profesor",
                        marcadorPosicion = "Escriba aquí sus observaciones sobre el avance del estudiante...",
                        iconoInicio = Icons.AutoMirrored.Filled.Comment,
                        esLineaSimple = false,
                        mensajeError = estadoObs.errorObservacion
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.guardarObservacion {} },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar y Validar Observación")
                    }

                    AnimatedVisibility(
                        visible = estadoObs.seGuardoConExito,
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

            // Recurso Nativo: Compartir / Exportar Informe
            OutlinedButton(
                onClick = { viewModel.compartirInformeDeFormaNativa(contexto, informe) },
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
