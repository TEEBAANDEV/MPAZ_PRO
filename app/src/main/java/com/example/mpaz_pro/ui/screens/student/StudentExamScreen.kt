package com.example.mpaz_pro.ui.screens.student

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mpaz_pro.data.local.entity.LearningObjectiveEntity
import com.example.mpaz_pro.data.local.entity.UserEntity
import com.example.mpaz_pro.ui.components.AudioSpeakerButton
import com.example.mpaz_pro.ui.components.MpazTopBar
import com.example.mpaz_pro.ui.viewmodel.StudentViewModel

@Composable
fun StudentExamScreen(
    objectiveId: String,
    currentUser: UserEntity?,
    assignedObjectives: List<LearningObjectiveEntity>,
    studentViewModel: StudentViewModel,
    onNavigateHome: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val objective = assignedObjectives.find { it.id == objectiveId }
    val examState by studentViewModel.examState.collectAsState()
    val questions by studentViewModel.currentExamQuestions.collectAsState()
    val isSpeaking by studentViewModel.isSpeaking.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(objectiveId) {
        if (currentUser != null && objective != null) {
            studentViewModel.loadExamForObjective(currentUser.id, objective)
        }
    }

    Scaffold(
        topBar = {
            MpazTopBar(
                title = "Evaluación Final",
                subtitle = "${objective?.code ?: ""} • Intentos: ${examState.attemptsRemaining}",
                onNavigateBack = {
                    studentViewModel.stopAudio()
                    onNavigateBack()
                }
            )
        }
    ) { innerPadding ->
        // Caso: Intentos agotados por parametrización docente
        if (!examState.canTakeExam && !examState.isFinished) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
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
                    onClick = onNavigateHome,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Volver al Inicio")
                }
            }
            return@Scaffold
        }

        // Caso: Prueba finalizada con éxito -> Pantalla de resultado estudiantil
        if (examState.isFinished) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (examState.finalScore >= 12) Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                    modifier = Modifier.size(90.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (examState.finalScore >= 12) Icons.Default.Star else Icons.Default.ThumbUp,
                            contentDescription = null,
                            tint = if (examState.finalScore >= 12) Color(0xFF2E7D32) else Color(0xFFE65100),
                            modifier = Modifier.size(52.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (examState.finalScore >= 12) "¡Felicitaciones! 🎉" else "¡Buen Intento! 👍",
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
                            text = "Respuestas Correctas: ${examState.correctCount} de ${examState.totalCount}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Puntaje Obtenido: ${examState.finalScore} / ${examState.maxScore} pts",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Intentos restantes: ${examState.attemptsRemaining}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onNavigateHome,
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

        if (questions.isEmpty()) {
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

        val currentQuestion = questions[examState.currentQuestionIndex]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Barra de progreso de la prueba
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pregunta ${examState.currentQuestionIndex + 1} de ${questions.size}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Valor: ${currentQuestion.points} pts",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            LinearProgressIndicator(
                progress = { (examState.currentQuestionIndex + 1).toFloat() / questions.size.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.outlineVariant
            )

            // Enunciado
            Text(
                text = currentQuestion.questionText,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp, lineHeight = 28.sp),
                fontWeight = FontWeight.Bold
            )

            // Botón parlante TTS nativo
            AudioSpeakerButton(
                textToSpeak = currentQuestion.audioQuestion,
                onSpeak = { studentViewModel.playAudio(it) },
                onStop = { studentViewModel.stopAudio() },
                isSpeaking = isSpeaking,
                label = "Escuchar la pregunta"
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Alternativas
            currentQuestion.options.forEachIndexed { index, optionText ->
                val isSelected = examState.selectedOptionIndex == index

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(
                        if (isSelected) 2.dp else 1.dp,
                        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { studentViewModel.onExamOptionSelected(index) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { studentViewModel.onExamOptionSelected(index) }
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

            Spacer(modifier = Modifier.height(12.dp))

            // Botón de Siguiente o Finalizar
            if (examState.currentQuestionIndex + 1 < questions.size) {
                Button(
                    onClick = { studentViewModel.nextExamQuestion() },
                    enabled = examState.selectedOptionIndex != null,
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
                    Icon(Icons.Default.NavigateNext, contentDescription = null)
                }
            } else {
                Button(
                    onClick = {
                        if (currentUser != null && objective != null) {
                            studentViewModel.stopAudio()
                            studentViewModel.submitExam(currentUser, objective, "Lenguaje y Comunicación")
                        }
                    },
                    enabled = examState.selectedOptionIndex != null,
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
