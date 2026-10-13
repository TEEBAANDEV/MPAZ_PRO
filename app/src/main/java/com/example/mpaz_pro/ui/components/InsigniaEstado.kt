package com.example.mpaz_pro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun InsigniaEstado(
    estado: String,
    modificador: Modifier = Modifier
) {
    val esAutorizado = estado.equals("AUTORIZADO", ignoreCase = true)
    val colorFondo = if (esAutorizado) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
    val colorTexto = if (esAutorizado) Color(0xFF2E7D32) else Color(0xFFE65100)

    Box(
        modifier = modificador
            .background(color = colorFondo, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = estado,
            color = colorTexto,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}
