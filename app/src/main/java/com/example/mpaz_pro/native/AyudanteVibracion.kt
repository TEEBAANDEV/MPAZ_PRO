package com.example.mpaz_pro.native

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class AyudanteVibracion(contexto: Context) {

    private val vibrador: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val administrador = contexto.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        administrador?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        contexto.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    fun reproducirExito() {
        if (vibrador?.hasVibrator() != true) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrador.vibrate(VibrationEffect.createOneShot(60, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrador.vibrate(60)
        }
    }

    fun reproducirError() {
        if (vibrador?.hasVibrator() != true) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val tiempos = longArrayOf(0, 40, 60, 70)
            val amplitudes = intArrayOf(0, 180, 0, 180)
            vibrador.vibrate(VibrationEffect.createWaveform(tiempos, amplitudes, -1))
        } else {
            @Suppress("DEPRECATION")
            val patron = longArrayOf(0, 40, 60, 70)
            vibrador.vibrate(patron, -1)
        }
    }

    fun reproducirCelebracion() {
        if (vibrador?.hasVibrator() != true) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val tiempos = longArrayOf(0, 70, 70, 70, 70, 140)
            val amplitudes = intArrayOf(0, 200, 0, 220, 0, 255)
            vibrador.vibrate(VibrationEffect.createWaveform(tiempos, amplitudes, -1))
        } else {
            @Suppress("DEPRECATION")
            val patron = longArrayOf(0, 70, 70, 70, 70, 140)
            vibrador.vibrate(patron, -1)
        }
    }
}
