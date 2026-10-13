package com.example.mpaz_pro.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mpaz_pro.data.local.UsuarioEntity
import com.example.mpaz_pro.ui.components.AppDrawer
import com.example.mpaz_pro.ui.components.BarraSuperiorMpaz
import com.example.mpaz_pro.viewmodel.MpazViewModel

@Composable
fun PantallaEstudiante(
    usuarioActual: UsuarioEntity?,
    viewModel: MpazViewModel,
    alCerrarSesion: () -> Unit
) {
    val objetivos by viewModel.objetivosAsignados.collectAsState()
    val estaHablando by viewModel.estaHablando.collectAsState()
    val nombreAlumno = usuarioActual?.nombreCompleto ?: "Lucas"

    var seccionActual by remember { mutableStateOf("Panel Principal") }
    var indiceObjetivoActual by remember { mutableStateOf(0) }
    var respuestaSeleccionada by remember { mutableStateOf<Int?>(null) }
    var mensajeResultado by remember { mutableStateOf<String?>(null) }

    val objetivoActivo = objetivos.getOrNull(indiceObjetivoActual)

    AppDrawer(
        pantallaActual = seccionActual,
        nombreUsuario = nombreAlumno,
        rolUsuario = "Estudiante 2° Básico",
        onPantallaSeleccionada = { seccion ->
            seccionActual = seccion
            if (seccion == "Seguimiento e Informes") {
                viewModel.detenerAudio()
            }
        },
        onCerrarSesion = {
            viewModel.detenerAudio()
            alCerrarSesion()
        }
    ) { abrirMenu ->
        Scaffold(
            topBar = {
                BarraSuperiorMpaz(
                    titulo = "¡Hola, $nombreAlumno! ⭐",
                    subtitulo = "$seccionActual • 2° Básico",
                    alAbrirMenu = { abrirMenu() },
                    alCerrarSesion = {
                        viewModel.detenerAudio()
                        alCerrarSesion()
                    }
                )
            }
        ) { padding ->
            if (seccionActual == "Seguimiento e Informes") {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(56.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Mi Progreso y Medallas",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "¡Excelente dedicación en tus lecturas, $nombreAlumno!",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Resumen de Aprendizaje",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Misiones Asignadas:")
                                Text("${objetivos.size}", fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Apoyo de Audio (TTS):")
                                Text("Activo 🔊", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Vibración Háptica:")
                                Text("Activa 📳", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }

                    Button(
                        onClick = { seccionActual = "Panel Principal" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Volver a Mis Misiones de Aprendizaje")
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                // Selector de Misión / Asignatura
                Text(
                    text = "Elige tu Misión de Aprendizaje:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                if (objetivos.isNotEmpty()) {
                    ScrollableTabRow(
                        selectedTabIndex = indiceObjetivoActual.coerceIn(0, objetivos.size - 1),
                        edgePadding = 0.dp
                    ) {
                        objetivos.forEachIndexed { idx, obj ->
                            Tab(
                                selected = indiceObjetivoActual == idx,
                                onClick = {
                                    viewModel.detenerAudio()
                                    indiceObjetivoActual = idx
                                    respuestaSeleccionada = null
                                    mensajeResultado = null
                                },
                                text = { Text("${obj.codigo}: ${obj.nombreAsignatura}") }
                            )
                        }
                    }
                }

                if (objetivoActivo == null) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                            Text("No hay misiones asignadas en este momento.")
                        }
                    }
                } else {
                    // Tarjeta de Contenido Didáctico con Text-To-Speech Nativo
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = objetivoActivo.titulo,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )

                                // Botón nativo Text-To-Speech (lectura en voz alta para 2° básico)
                                IconButton(
                                    onClick = {
                                        if (estaHablando) {
                                            viewModel.detenerAudio()
                                        } else {
                                            viewModel.reproducirAudio("${objetivoActivo.titulo}. ${objetivoActivo.descripcion}. Pregunta: ${objetivoActivo.pregunta}")
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (estaHablando) Icons.Default.Stop else Icons.Default.VolumeUp,
                                        contentDescription = "Escuchar en voz alta",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = objetivoActivo.descripcion,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    // Desafío Interactivo con Vibración Háptica Nativa
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "🎯 Desafío Interactivo:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = objetivoActivo.pregunta,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            val listaOpciones = objetivoActivo.opciones.split(",").map { it.trim() }

                            listaOpciones.forEachIndexed { opIdx, opcion ->
                                val esSeleccionada = respuestaSeleccionada == opIdx
                                OutlinedButton(
                                    onClick = {
                                        respuestaSeleccionada = opIdx
                                        viewModel.responderPregunta(opIdx, objetivoActivo) { acierto ->
                                            mensajeResultado = if (acierto) "¡Correcto! 🎉 Excelente trabajo." else "¡Casi! 💡 Revisa la lectura e inténtalo de nuevo."
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = if (esSeleccionada) {
                                        ButtonDefaults.outlinedButtonColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer
                                        )
                                    } else ButtonDefaults.outlinedButtonColors()
                                ) {
                                    Text(
                                        text = opcion,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            AnimatedVisibility(visible = mensajeResultado != null) {
                                Text(
                                    text = mensajeResultado ?: "",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
}

