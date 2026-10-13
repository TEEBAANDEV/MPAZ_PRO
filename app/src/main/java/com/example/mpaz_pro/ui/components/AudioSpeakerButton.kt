package com.example.mpaz_pro.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AudioSpeakerButton(
    textToSpeak: String,
    onSpeak: (String) -> Unit,
    onStop: () -> Unit,
    isSpeaking: Boolean,
    modifier: Modifier = Modifier,
    label: String = "Escuchar audio"
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val animatedScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(450, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    val scale = if (isSpeaking) animatedScale else 1.0f

    FilledTonalButton(
        onClick = {
            if (isSpeaking) {
                onStop()
            } else {
                onSpeak(textToSpeak)
            }
        },
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = if (isSpeaking) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isSpeaking) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = label,
                modifier = Modifier
                    .size(24.dp)
                    .scale(scale)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isSpeaking) "Detener audio" else label,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
