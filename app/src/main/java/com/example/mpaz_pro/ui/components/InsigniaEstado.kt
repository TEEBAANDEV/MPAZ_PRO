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
import com.example.mpaz_pro.data.local.entity.EstadoObjetivo

@Composable
fun InsigniaEstado(
    estado: EstadoObjetivo,
    modificador: Modifier = Modifier
) {
    val (colorFondo, colorTexto, etiqueta) = when (estado) {
        EstadoObjetivo.BORRADOR -> Triple(Color(0xFFECEFF1), Color(0xFF455A64), "Borrador")
        EstadoObjetivo.EN_REVISION -> Triple(Color(0xFFFFF3E0), Color(0xFFE65100), "En Revisión")
        EstadoObjetivo.AUTORIZADO -> Triple(Color(0xFFE3F2FD), Color(0xFF1565C0), "Autorizado")
        EstadoObjetivo.PUBLICADO -> Triple(Color(0xFFE8F5E9), Color(0xFF2E7D32), "Publicado")
        EstadoObjetivo.DESHABILITADO -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), "Deshabilitado")
    }

    Box(
        modifier = modificador
            .background(color = colorFondo, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = etiqueta,
            color = colorTexto,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}
