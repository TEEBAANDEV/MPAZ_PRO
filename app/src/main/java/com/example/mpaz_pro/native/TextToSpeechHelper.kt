package com.example.mpaz_pro.native

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class TextToSpeechHelper(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    init {
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("es", "CL"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Fallback a español general
                tts?.setLanguage(Locale("es", "ES"))
            }
            tts?.setPitch(1.05f) // Tono ligeramente más amigable para niños
            tts?.setSpeechRate(0.88f) // Ritmo más pausado y claro para 2° básico
            isInitialized = true
        } else {
            Log.e("TextToSpeechHelper", "Error al inicializar TextToSpeech nativo")
        }
    }

    fun speak(text: String) {
        if (!isInitialized || text.isBlank()) return
        tts?.stop()
        _isSpeaking.value = true
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "mpaz_speech_id")
    }

    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}
