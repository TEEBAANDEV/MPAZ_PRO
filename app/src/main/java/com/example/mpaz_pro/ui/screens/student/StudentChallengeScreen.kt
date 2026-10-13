package com.example.mpaz_pro.ui.screens.student

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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
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
fun StudentChallengeScreen(
    objectiveId: String,
    studentViewModel: StudentViewModel,
    onNavigateToExam: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val challenges by studentViewModel.currentChallenges.collectAsState()
    val challengeState by studentViewModel.challengeState.collectAsState()
    val isSpeaking by studentViewModel.isSpeaking.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(objectiveId) {
        studentViewModel.loadChallengesForObjective(objectiveId)
    }

    Scaffold(
        topBar = {
            MpazTopBar(
                title = "Actividades Didácticas",
                subtitle = "Practica antes de la prueba",
                onNavigateBack = {
                    studentViewModel.stopAudio()
                    onNavigateBack()
                }
            )
        }
    ) { innerPadding ->
        if (challenges.isEmpty()) {
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

        if (challengeState.isCompleted) {
            // Pantalla de felicitación por completar desafíos
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
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
                        studentViewModel.stopAudio()
                        onNavigateToExam(objectiveId)
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
                    Icon(Icons.Default.NavigateNext, contentDescription = null)
                }
            }
            return@Scaffold
        }

        val currentChallenge = challenges[challengeState.currentChallengeIndex]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Indicador de avance del desafío
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Desafío ${challengeState.currentChallengeIndex + 1} de ${challenges.size}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Dificultad: ${currentChallenge.difficulty}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            LinearProgressIndicator(
                progress = { (challengeState.currentChallengeIndex + 1).toFloat() / challenges.size.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.outlineVariant
            )

            // Enunciado
            Text(
                text = currentChallenge.prompt,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp, lineHeight = 28.sp),
                fontWeight = FontWeight.Bold
            )

            // Botón de audio nativo para escuchar la instrucción
            AudioSpeakerButton(
                textToSpeak = currentChallenge.audioPrompt,
                onSpeak = { studentViewModel.playAudio(it) },
                onStop = { studentViewModel.stopAudio() },
                isSpeaking = isSpeaking,
                label = "Escuchar la pregunta"
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Alternativas grandes para niños de 2° básico
            currentChallenge.options.forEachIndexed { index, optionText ->
                val isSelected = challengeState.selectedOptionIndex == index
                val isCorrectAnswer = index == currentChallenge.correctIndex
                val isChecked = challengeState.isAnswerChecked

                val borderColor = when {
                    isChecked && isCorrectAnswer -> Color(0xFF2E7D32)
                    isChecked && isSelected && !challengeState.isCorrect -> MaterialTheme.colorScheme.error
                    isSelected -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.outlineVariant
                }

                val containerColor = when {
                    isChecked && isCorrectAnswer -> Color(0xFFE8F5E9)
                    isChecked && isSelected && !challengeState.isCorrect -> Color(0xFFFFEBEE)
                    isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    else -> MaterialTheme.colorScheme.surface
                }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = containerColor),
                    border = BorderStroke(if (isSelected || isChecked) 2.dp else 1.dp, borderColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                        .clickable(enabled = !isChecked) {
                            studentViewModel.onChallengeOptionSelected(index)
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = {
                                if (!isChecked) studentViewModel.onChallengeOptionSelected(index)
                            }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = optionText,
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            // Retroalimentación animada ante acierto/error (IE 2.2.2 & IE 2.4.1 háptico)
            AnimatedVisibility(
                visible = challengeState.isAnswerChecked,
                enter = fadeIn() + expandVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
                exit = fadeOut()
            ) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (challengeState.isCorrect) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (challengeState.isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                                contentDescription = null,
                                tint = if (challengeState.isCorrect) Color(0xFF2E7D32) else Color(0xFFC62828),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (challengeState.isCorrect) "¡Correcto! 🎉" else "¡Ánimo! Inténtalo de nuevo",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (challengeState.isCorrect) Color(0xFF2E7D32) else Color(0xFFC62828)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = currentChallenge.feedbackExplanation,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón de Comprobar o Continuar
            if (!challengeState.isAnswerChecked) {
                Button(
                    onClick = { studentViewModel.checkChallengeAnswer() },
                    enabled = challengeState.selectedOptionIndex != null,
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
                    onClick = { studentViewModel.nextChallenge() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = if (challengeState.currentChallengeIndex + 1 < challenges.size) "Siguiente Actividad" else "¡Ver Resultado!",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.NavigateNext, contentDescription = null)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
