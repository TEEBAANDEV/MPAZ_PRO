package com.example.mpaz_pro.native

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class AyudanteTextoAVoz(contexto: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var estaInicializado = false

    private val _estaHablando = MutableStateFlow(false)
    val estaHablando: StateFlow<Boolean> = _estaHablando.asStateFlow()

    init {
        tts = TextToSpeech(contexto.applicationContext, this)
    }

    override fun onInit(estado: Int) {
        if (estado == TextToSpeech.SUCCESS) {
            val resultado = tts?.setLanguage(Locale("es", "CL"))
            if (resultado == TextToSpeech.LANG_MISSING_DATA || resultado == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.setLanguage(Locale("es", "ES"))
            }
            tts?.setPitch(1.05f)
            tts?.setSpeechRate(0.88f)
            estaInicializado = true
        } else {
            Log.e("AyudanteTextoAVoz", "Error al inicializar TextToSpeech nativo")
        }
    }

    fun hablar(texto: String) {
        if (!estaInicializado || texto.isBlank()) return
        tts?.stop()
        _estaHablando.value = true
        tts?.speak(texto, TextToSpeech.QUEUE_FLUSH, null, "mpaz_audio_id")
    }

    fun detener() {
        tts?.stop()
        _estaHablando.value = false
    }

    fun cerrar() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        estaInicializado = false
    }
}
