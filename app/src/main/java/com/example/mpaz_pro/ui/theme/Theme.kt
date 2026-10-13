package com.example.mpaz_pro.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = MpazBlueLight,
    onPrimary = Color.Black,
    secondary = MpazGreenLight,
    tertiary = MpazAmberLight,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
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

@Composable
fun MPAZ_PROTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}