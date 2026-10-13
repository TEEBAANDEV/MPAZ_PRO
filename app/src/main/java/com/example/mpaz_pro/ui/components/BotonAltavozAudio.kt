package com.example.mpaz_pro.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp

@Composable
fun BotonAltavozAudio(
    textoParaHablar: String,
    alReproducir: (String) -> Unit,
    alDetener: () -> Unit,
    estaHablando: Boolean,
    modificador: Modifier = Modifier,
    etiqueta: String = "Escuchar audio"
) {
    val transicionInfinita = rememberInfiniteTransition(label = "pulso")
    val escalaAnimada by transicionInfinita.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(450, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "escala"
    )
    val escala = if (estaHablando) escalaAnimada else 1.0f

    FilledTonalButton(
        onClick = {
            if (estaHablando) {
                alDetener()
            } else {
                alReproducir(textoParaHablar)
            }
        },
        modifier = modificador,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = if (estaHablando) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (estaHablando) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = etiqueta,
                modifier = Modifier
                    .size(24.dp)
                    .scale(escala)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (estaHablando) "Detener audio" else etiqueta,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
