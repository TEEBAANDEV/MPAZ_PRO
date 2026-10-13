package com.example.mpaz_pro.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

object AdministradorTema {
    var forzarOscuro by mutableStateOf<Boolean?>(null)
    var altoContraste by mutableStateOf(false)
}

private val DarkColorScheme = darkColorScheme(
    primary = MpazBlueLight,
    onPrimary = Color.Black,
    secondary = MpazGreenLight,
    tertiary = MpazAmberLight,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    surfaceVariant = Color(0xFF2A2A2A),
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = MpazBlue,
    onPrimary = Color.White,
    primaryContainer = MpazBlueLight,
    onPrimaryContainer = MpazBlueDark,
    secondary = MpazGreen,
    onSecondary = Color.White,
    secondaryContainer = MpazGreenLight,
    tertiary = MpazAmber,
    onTertiary = Color.White,
    tertiaryContainer = MpazAmberLight,
    background = MpazBackground,
    surface = Color.White,
    surfaceVariant = Color(0xFFEDF2F7),
    onBackground = MpazOnSurface,
    onSurface = MpazOnSurface
)

private val AltoContrasteDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFEB3B),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF333300),
    onPrimaryContainer = Color(0xFFFFFF00),
    secondary = Color(0xFF00E676),
    onSecondary = Color.Black,
    tertiary = Color(0xFFFF9100),
    onTertiary = Color.Black,
    background = Color(0xFF000000),
    surface = Color(0xFF0A0A0A),
    surfaceVariant = Color(0xFF1F1F1F),
    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFFFFFF),
    outline = Color(0xFFFFFFFF)
)

private val AltoContrasteLightColorScheme = lightColorScheme(
    primary = Color(0xFF0011EE),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD4E0FF),
    onPrimaryContainer = Color(0xFF000080),
    secondary = Color(0xFF007A33),
    onSecondary = Color.White,
    tertiary = Color(0xFFC43E00),
    onTertiary = Color.White,
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFE5E5E5),
    onBackground = Color(0xFF000000),
    onSurface = Color(0xFF000000),
    outline = Color(0xFF000000)
)

@Composable
fun MPAZ_PROTheme(
    darkTheme: Boolean = AdministradorTema.forzarOscuro ?: isSystemInDarkTheme(),
    altoContraste: Boolean = AdministradorTema.altoContraste,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        altoContraste && darkTheme -> AltoContrasteDarkColorScheme
        altoContraste && !darkTheme -> AltoContrasteLightColorScheme
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
