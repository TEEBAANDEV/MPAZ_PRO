package com.example.mpaz_pro.ui.screens.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mpaz_pro.ui.components.AudioSpeakerButton
import com.example.mpaz_pro.ui.components.MpazTopBar
import com.example.mpaz_pro.ui.viewmodel.StudentViewModel

@Composable
fun StudentContentScreen(
    objectiveId: String,
    studentViewModel: StudentViewModel,
    onNavigateToChallenges: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val content by studentViewModel.currentContent.collectAsState()
    val isSpeaking by studentViewModel.isSpeaking.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(objectiveId) {
        studentViewModel.loadContentForObjective(objectiveId)
    }

    Scaffold(
        topBar = {
            MpazTopBar(
                title = "Lección Guiada",
                subtitle = "Lectura y Explicación Didáctica",
                onNavigateBack = {
                    studentViewModel.stopAudio()
                    onNavigateBack()
                }
            )
        }
    ) { innerPadding ->
        if (content == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        val lesson = content!!

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título de la lección
            Text(
                text = lesson.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )

            // Botón de audio nativo Text-To-Speech (IE 2.4.1)
            AudioSpeakerButton(
                textToSpeak = lesson.audioScript,
                onSpeak = { studentViewModel.playAudio(it) },
                onStop = { studentViewModel.stopAudio() },
                isSpeaking = isSpeaking,
                label = "Escuchar el cuento en voz alta",
                modifier = Modifier.fillMaxWidth()
            )

            // Tarjeta de lectura principal adaptada a lectores iniciales
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
                        text = lesson.writtenExplanation,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp, lineHeight = 26.sp),
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Ejemplo Guiado Pedagógico
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
                            text = lesson.guidedExample,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }

            // Instrucciones breves
            Text(
                text = "📌 Instrucción: ${lesson.instructions}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botón de continuar a los desafíos
            Button(
                onClick = {
                    studentViewModel.stopAudio()
                    onNavigateToChallenges(objectiveId)
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
                Icon(Icons.Default.NavigateNext, contentDescription = null, modifier = Modifier.size(26.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
