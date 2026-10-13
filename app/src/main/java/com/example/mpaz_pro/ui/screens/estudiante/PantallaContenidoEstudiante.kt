package com.example.mpaz_pro.ui.screens.estudiante

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mpaz_pro.ui.components.BarraSuperiorMpaz
import com.example.mpaz_pro.ui.components.BotonAltavozAudio
import com.example.mpaz_pro.viewmodel.EstudianteViewModel

@Composable
fun PantallaContenidoEstudiante(
    idObjetivo: String,
    viewModel: EstudianteViewModel,
    alNavegarADesafios: (String) -> Unit,
    alNavegarAtras: () -> Unit
) {
    val contenido by viewModel.contenidoActual.collectAsState()
    val estaHablando by viewModel.estaHablando.collectAsState()
    val estadoDesplazamiento = rememberScrollState()

    LaunchedEffect(idObjetivo) {
        viewModel.cargarContenidoParaObjetivo(idObjetivo)
    }

    Scaffold(
        topBar = {
            BarraSuperiorMpaz(
                titulo = "Lección Guiada",
                subtitulo = "Lectura y Explicación Didáctica",
                alNavegarAtras = {
                    viewModel.detenerAudio()
                    alNavegarAtras()
                }
            )
        }
    ) { rellenoInterno ->
        if (contenido == null) {
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

        val leccion = contenido!!

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(rellenoInterno)
                .verticalScroll(estadoDesplazamiento)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = leccion.titulo,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )

            // Recurso Nativo: Text-To-Speech en voz alta
            BotonAltavozAudio(
                textoParaHablar = leccion.guionAudio,
                alReproducir = { viewModel.reproducirAudio(it) },
                alDetener = { viewModel.detenerAudio() },
                estaHablando = estaHablando,
                etiqueta = "Escuchar el cuento en voz alta",
                modificador = Modifier.fillMaxWidth()
            )

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AutoStories,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Lectura Guiada",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = leccion.explicacionEscrita,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp, lineHeight = 26.sp),
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Ejemplo Guiado:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = leccion.ejemploGuiado,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }

            Text(
                text = "📌 Instrucción: ${leccion.instrucciones}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.detenerAudio()
                    alNavegarADesafios(idObjetivo)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "¡Ir a los Desafíos y Actividades!",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = null, modifier = Modifier.size(26.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

