package com.example.mpaz_pro.ui.screens.estudiante

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mpaz_pro.ui.components.BarraSuperiorMpaz
import com.example.mpaz_pro.ui.components.BotonAltavozAudio
import com.example.mpaz_pro.ui.viewmodel.EstudianteViewModel

@Composable
fun PantallaDesafioEstudiante(
    idObjetivo: String,
    viewModel: EstudianteViewModel,
    alNavegarAEvaluacion: (String) -> Unit,
    alNavegarAtras: () -> Unit
) {
    val desafios by viewModel.desafiosActuales.collectAsState()
    val estadoDesafio by viewModel.estadoDesafio.collectAsState()
    val estaHablando by viewModel.estaHablando.collectAsState()
    val estadoDesplazamiento = rememberScrollState()

    LaunchedEffect(idObjetivo) {
        viewModel.cargarDesafiosParaObjetivo(idObjetivo)
    }

    Scaffold(
        topBar = {
            BarraSuperiorMpaz(
                titulo = "Actividades Didácticas",
                subtitulo = "Practica antes de la prueba",
                alNavegarAtras = {
                    viewModel.detenerAudio()
                    alNavegarAtras()
                }
            )
        }
    ) { rellenoInterno ->
        if (desafios.isEmpty()) {
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

        if (estadoDesafio.estaCompletado) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(rellenoInterno)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = Color(0xFFFFB300),
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "¡Excelente Trabajo!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Has completado todas las actividades didácticas preparatorias. Ahora estás listo para rendir la prueba final de este objetivo.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.detenerAudio()
                        alNavegarAEvaluacion(idObjetivo)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "¡Rendir Prueba Final!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = null)
                }
            }
            return@Scaffold
        }

        val desafioActual = desafios[estadoDesafio.indiceDesafioActual]

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
                    text = "Desafío ${estadoDesafio.indiceDesafioActual + 1} de ${desafios.size}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Dificultad: ${desafioActual.dificultad}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            LinearProgressIndicator(
                progress = { (estadoDesafio.indiceDesafioActual + 1).toFloat() / desafios.size.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.outlineVariant
            )

            Text(
                text = desafioActual.enunciado,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp, lineHeight = 28.sp),
                fontWeight = FontWeight.Bold
            )

            BotonAltavozAudio(
                textoParaHablar = desafioActual.audioEnunciado,
                alReproducir = { viewModel.reproducirAudio(it) },
                alDetener = { viewModel.detenerAudio() },
                estaHablando = estaHablando,
                etiqueta = "Escuchar la pregunta"
            )

            Spacer(modifier = Modifier.height(4.dp))

            desafioActual.opciones.forEachIndexed { indice, textoOpcion ->
                val estaSeleccionada = estadoDesafio.indiceOpcionSeleccionada == indice
                val esRespuestaCorrecta = indice == desafioActual.indiceCorrecto
                val estaVerificada = estadoDesafio.respuestaVerificada

                val colorBorde = when {
                    estaVerificada && esRespuestaCorrecta -> Color(0xFF2E7D32)
                    estaVerificada && estaSeleccionada && !estadoDesafio.esCorrecta -> MaterialTheme.colorScheme.error
                    estaSeleccionada -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.outlineVariant
                }

                val colorFondo = when {
                    estaVerificada && esRespuestaCorrecta -> Color(0xFFE8F5E9)
                    estaVerificada && estaSeleccionada && !estadoDesafio.esCorrecta -> Color(0xFFFFEBEE)
                    estaSeleccionada -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    else -> MaterialTheme.colorScheme.surface
                }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colorFondo),
                    border = BorderStroke(if (estaSeleccionada || estaVerificada) 2.dp else 1.dp, colorBorde),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (estaSeleccionada) 3.dp else 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                        .clickable(enabled = !estaVerificada) {
                            viewModel.alSeleccionarOpcionDesafio(indice)
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = estaSeleccionada,
                            onClick = {
                                if (!estaVerificada) viewModel.alSeleccionarOpcionDesafio(indice)
                            }
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

            AnimatedVisibility(
                visible = estadoDesafio.respuestaVerificada,
                enter = fadeIn() + expandVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
                exit = fadeOut()
            ) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (estadoDesafio.esCorrecta) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (estadoDesafio.esCorrecta) Icons.Default.CheckCircle else Icons.Default.Close,
                                contentDescription = null,
                                tint = if (estadoDesafio.esCorrecta) Color(0xFF2E7D32) else Color(0xFFC62828),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (estadoDesafio.esCorrecta) "¡Correcto! 🎉" else "¡Ánimo! Inténtalo de nuevo",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (estadoDesafio.esCorrecta) Color(0xFF2E7D32) else Color(0xFFC62828)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = desafioActual.explicacionRetroalimentacion,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (!estadoDesafio.respuestaVerificada) {
                Button(
                    onClick = { viewModel.verificarRespuestaDesafio() },
                    enabled = estadoDesafio.indiceOpcionSeleccionada != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Comprobar Respuesta",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Button(
                    onClick = { viewModel.siguienteDesafio() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = if (estadoDesafio.indiceDesafioActual + 1 < desafios.size) "Siguiente Actividad" else "¡Ver Resultado!",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = null)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
