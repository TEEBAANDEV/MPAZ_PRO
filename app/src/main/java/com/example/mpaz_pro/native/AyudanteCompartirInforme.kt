package com.example.mpaz_pro.native

import android.content.Context
import android.content.Intent
import com.example.mpaz_pro.data.local.InformeEntity

object AyudanteCompartirInforme {

    fun compartirInforme(contexto: Context, informe: InformeEntity) {
        val cuerpoInforme = """
            ==============================================
            ESCUELA ESCRITORA MARCELA PAZ - SAN BERNARDO
            INFORME PEDAGÓGICO DE EVALUACIÓN - 2° BÁSICO
            ==============================================
            Estudiante: ${informe.nombreEstudiante}
            Curso: ${informe.curso}
            Asignatura: ${informe.asignatura}
            Fecha: ${informe.fecha}
            ----------------------------------------------
            RESULTADOS:
            Porcentaje de Logro: ${informe.porcentajeLogro}%
            Desafíos Didácticos Completados: ${informe.desafiosCompletados}
            ----------------------------------------------
            OBSERVACIÓN DEL DOCENTE:
            ${if (informe.observacionDocente.isNotBlank()) informe.observacionDocente else "(Sin observación registrada)"}
            ==============================================
            Generado automáticamente por MPAZ PRO 2.º
        """.trimIndent()

        val intentoEnvio = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, cuerpoInforme)
            putExtra(Intent.EXTRA_SUBJECT, "Informe Pedagógico - ${informe.nombreEstudiante}")
            type = "text/plain"
        }
        val selector = Intent.createChooser(intentoEnvio, "Compartir Informe Pedagógico")
        selector.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        contexto.startActivity(selector)
    }
}
