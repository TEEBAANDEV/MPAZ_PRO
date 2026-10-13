package com.example.mpaz_pro

import android.app.Application
import com.example.mpaz_pro.data.local.AppDatabase
import com.example.mpaz_pro.data.local.DatabaseInitializer
import com.example.mpaz_pro.data.repository.AppRepository
import com.example.mpaz_pro.native.HapticFeedbackHelper
import com.example.mpaz_pro.native.TextToSpeechHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MpazApplication : Application() {

    lateinit var database: AppDatabase
        private set

    lateinit var repository: AppRepository
        private set

    lateinit var ttsHelper: TextToSpeechHelper
        private set

    lateinit var hapticHelper: HapticFeedbackHelper
        private set

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getDatabase(this)
        repository = AppRepository(database)
        ttsHelper = TextToSpeechHelper(this)
        hapticHelper = HapticFeedbackHelper(this)

        // Inicializar datos semilla pedagógicos de prueba en corrutina de fondo
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseInitializer.populateInitialData(database)
        }
    }

    override fun onTerminate() {
        ttsHelper.shutdown()
        super.onTerminate()
    }
}
