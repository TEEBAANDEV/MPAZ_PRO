package com.example.mpaz_pro.ui.screens.estudiante

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mpaz_pro.data.local.entity.ObjetivoAprendizajeEntity
import com.example.mpaz_pro.data.local.entity.UsuarioEntity
import com.example.mpaz_pro.ui.components.BarraSuperiorMpaz
import com.example.mpaz_pro.ui.components.BotonAltavozAudio
import com.example.mpaz_pro.viewmodel.EstudianteViewModel

@Composable
fun PantallaEvaluacionEstudiante(
    idObjetivo: String,
    usuarioActual: UsuarioEntity?,
    objetivosAsignados: List<ObjetivoAprendizajeEntity>,
    viewModel: EstudianteViewModel,
    alNavegarAInicio: () -> Unit,
    alNavegarAtras: () -> Unit
) {
    val objetivo = objetivosAsignados.find { it.id == idObjetivo }
    val estadoEvaluacion by viewModel.estadoEvaluacion.collectAsState()
    val preguntas by viewModel.preguntasEvaluacionActuales.collectAsState()
    val estaHablando by viewModel.estaHablando.collectAsState()
    val estadoDesplazamiento = rememberScrollState()

    LaunchedEffect(idObjetivo) {
        if (usuarioActual != null && objetivo != null) {
            viewModel.cargarEvaluacionParaObjetivo(usuarioActual.id, objetivo)
        }
    }

    Scaffold(
        topBar = {
            BarraSuperiorMpaz(
                titulo = "Evaluación Final",
                subtitulo = "${objetivo?.codigo ?: ""} • Intentos: ${estadoEvaluacion.intentosRestantes}",
                alNavegarAtras = {
                    viewModel.detenerAudio()
                    alNavegarAtras()
                }
            )
        }
    ) { rellenoInterno ->
        if (!estadoEvaluacion.puedeRendirEvaluacion && !estadoEvaluacion.estaFinalizada) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(rellenoInterno)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(72.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Límite de Intentos Alcanzado",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Has utilizado todos los intentos autorizados por tu docente para esta evaluación. Tu profesor revisará tus respuestas en el informe pedagógico.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = alNavegarAInicio,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Volver al Inicio")
                }
            }
            return@Scaffold
        }

        if (estadoEvaluacion.estaFinalizada) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(rellenoInterno)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (estadoEvaluacion.puntajeFinal >= 12) Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                    modifier = Modifier.size(90.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (estadoEvaluacion.puntajeFinal >= 12) Icons.Default.Star else Icons.Default.ThumbUp,
                            contentDescription = null,
                            tint = if (estadoEvaluacion.puntajeFinal >= 12) Color(0xFF2E7D32) else Color(0xFFE65100),
                            modifier = Modifier.size(52.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (estadoEvaluacion.puntajeFinal >= 12) "¡Felicitaciones! 🎉" else "¡Buen Intento! 👍",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Tu prueba fue enviada exitosamente a tu profesor.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Respuestas Correctas: ${estadoEvaluacion.cantidadCorrectas} de ${estadoEvaluacion.cantidadTotal}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Puntaje Obtenido: ${estadoEvaluacion.puntajeFinal} / ${estadoEvaluacion.puntajeMaximo} pts",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Intentos restantes: ${estadoEvaluacion.intentosRestantes}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = alNavegarAInicio,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Volver a Mis Misiones",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            return@Scaffold
        }

        if (preguntas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(rellenoInterno),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        val preguntaActual = preguntas[estadoEvaluacion.indicePreguntaActual]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(rellenoInterno)
                .verticalScroll(estadoDesplazamiento)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pregunta ${estadoEvaluacion.indicePreguntaActual + 1} de ${preguntas.size}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Valor: ${preguntaActual.puntaje} pts",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            LinearProgressIndicator(
                progress = { (estadoEvaluacion.indicePreguntaActual + 1).toFloat() / preguntas.size.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.outlineVariant
            )

            Text(
                text = preguntaActual.textoPregunta,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp, lineHeight = 28.sp),
                fontWeight = FontWeight.Bold
            )

            BotonAltavozAudio(
                textoParaHablar = preguntaActual.audioPregunta,
                alReproducir = { viewModel.reproducirAudio(it) },
                alDetener = { viewModel.detenerAudio() },
                estaHablando = estaHablando,
                etiqueta = "Escuchar la pregunta"
            )

            Spacer(modifier = Modifier.height(4.dp))

            preguntaActual.opciones.forEachIndexed { indice, textoOpcion ->
                val estaSeleccionada = estadoEvaluacion.indiceOpcionSeleccionada == indice

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (estaSeleccionada) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(
                        if (estaSeleccionada) 2.dp else 1.dp,
                        if (estaSeleccionada) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.alSeleccionarOpcionEvaluacion(indice) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = estaSeleccionada,
                            onClick = { viewModel.alSeleccionarOpcionEvaluacion(indice) }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = textoOpcion,
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                            fontWeight = if (estaSeleccionada) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (estadoEvaluacion.indicePreguntaActual + 1 < preguntas.size) {
                Button(
                    onClick = { viewModel.siguientePreguntaEvaluacion() },
                    enabled = estadoEvaluacion.indiceOpcionSeleccionada != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Siguiente Pregunta",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = null)
                }
            } else {
                Button(
                    onClick = {
                        if (usuarioActual != null && objetivo != null) {
                            viewModel.detenerAudio()
                            viewModel.entregarEvaluacion(usuarioActual, objetivo, "Lenguaje y Comunicación")
                        }
                    },
                    enabled = estadoEvaluacion.indiceOpcionSeleccionada != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Finalizar y Entregar Prueba",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

