package com.example.mpaz_pro.ui.theme

import androidx.compose.ui.graphics.Color

val MpazBlue = Color(0xFF1E88E5)
val MpazBlueDark = Color(0xFF0D47A1)
val MpazBlueLight = Color(0xFFBBDEFB)

val MpazGreen = Color(0xFF2E7D32)
val MpazGreenLight = Color(0xFFC8E6C9)

val MpazAmber = Color(0xFFFFA000)
val MpazAmberLight = Color(0xFFFFECB3)

val MpazSurface = Color(0xFFF8F9FA)
val MpazBackground = Color(0xFFF4F6F9)
val MpazOnSurface = Color(0xFF212121)

data class EstiloAsignatura(
    val colorPrincipal: Color,
    val colorFondo: Color,
    val colorBorde: Color,
    val emoji: String,
    val nombreCorto: String
)

object ColoresAsignatura {
    val RojoLenguaje = Color(0xFFE53935)
    val AzulMatematica = Color(0xFF1E88E5)
    val VerdeCiencias = Color(0xFF43A047)
    val NaranjaHistoria = Color(0xFFFB8C00)

    fun obtenerEstilo(idAsignatura: String): EstiloAsignatura {
        val idNormalizado = idAsignatura.lowercase().trim()
        return when {
            idNormalizado.contains("lenguaje") -> EstiloAsignatura(
                colorPrincipal = RojoLenguaje,
                colorFondo = RojoLenguaje.copy(alpha = 0.15f),
                colorBorde = RojoLenguaje.copy(alpha = 0.60f),
                emoji = "📖",
                nombreCorto = "Lenguaje"
            )
            idNormalizado.contains("matematica") || idNormalizado.contains("matemática") -> EstiloAsignatura(
                colorPrincipal = AzulMatematica,
                colorFondo = AzulMatematica.copy(alpha = 0.15f),
                colorBorde = AzulMatematica.copy(alpha = 0.60f),
                emoji = "🔢",
                nombreCorto = "Matemática"
            )
            idNormalizado.contains("ciencia") -> EstiloAsignatura(
                colorPrincipal = VerdeCiencias,
                colorFondo = VerdeCiencias.copy(alpha = 0.15f),
                colorBorde = VerdeCiencias.copy(alpha = 0.60f),
                emoji = "🌿",
                nombreCorto = "Ciencias"
            )
            idNormalizado.contains("historia") -> EstiloAsignatura(
                colorPrincipal = NaranjaHistoria,
                colorFondo = NaranjaHistoria.copy(alpha = 0.15f),
                colorBorde = NaranjaHistoria.copy(alpha = 0.60f),
                emoji = "🏛️",
                nombreCorto = "Historia"
            )
            else -> EstiloAsignatura(
                colorPrincipal = Color(0xFF7E57C2),
                colorFondo = Color(0xFF7E57C2).copy(alpha = 0.15f),
                colorBorde = Color(0xFF7E57C2).copy(alpha = 0.60f),
                emoji = "📚",
                nombreCorto = "General"
            )
        }
    }
}
