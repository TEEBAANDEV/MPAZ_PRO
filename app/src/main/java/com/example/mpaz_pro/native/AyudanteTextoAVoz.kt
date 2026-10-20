package com.example.mpaz_pro.native

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
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
            val localeChile = Locale.Builder().setLanguage("es").setRegion("CL").build()
            val localeEspana = Locale.Builder().setLanguage("es").setRegion("ES").build()

            val resultado = tts?.setLanguage(localeChile)
            if (resultado == TextToSpeech.LANG_MISSING_DATA || resultado == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.setLanguage(localeEspana)
            }
            tts?.setPitch(1.0f)
            tts?.setSpeechRate(1.20f)

            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _estaHablando.value = true
                }

                override fun onDone(utteranceId: String?) {
                    _estaHablando.value = false
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _estaHablando.value = false
                }
            })

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
