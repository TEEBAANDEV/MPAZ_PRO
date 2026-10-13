package com.example.mpaz_pro.native

import android.content.Context
import android.content.Intent
import com.example.mpaz_pro.data.local.entity.InformePedagogicoEntity

object AyudanteCompartirInforme {

    fun compartirInforme(contexto: Context, informe: InformePedagogicoEntity) {
        val cuerpoInforme = """
            ==============================================
            ESCUELA ESCRITORA MARCELA PAZ - SAN BERNARDO
            INFORME PEDAGÓGICO DE EVALUACIÓN - 2° BÁSICO
            ==============================================
            Estudiante: ${informe.nombreEstudiante}
            Curso: ${informe.nombreCurso}
            Asignatura: ${informe.nombreAsignatura}
            Objetivo: ${informe.codigoObjetivo} - ${informe.tituloObjetivo}
            Fecha de Emisión: ${informe.fechaEmision}
            ----------------------------------------------
            RESULTADOS:
            Puntaje Obtenido: ${informe.puntajeGlobal} de ${informe.puntajeMaximo} pts (${informe.porcentaje}%)
            Diagnóstico de Fortalezas:
            ${informe.fortalezas}
            
            Áreas de Refuerzo Prioritarias:
            ${informe.areasRefuerzo}
            ----------------------------------------------
            OBSERVACIONES PEDAGÓGICAS DEL DOCENTE:
            ${if (informe.observacionDocente.isNotBlank()) informe.observacionDocente else "(Sin observaciones registradas)"}
            
            Validado por Docente: ${if (informe.estaValidadoPorDocente) "SÍ (Firma Digital Activa)" else "NO"}
            ==============================================
            Generado automáticamente por MPAZ PRO 2.0
        """.trimIndent()

        val intentoEnvio = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, cuerpoInforme)
            putExtra(Intent.EXTRA_SUBJECT, "Informe Pedagógico - ${informe.nombreEstudiante} (${informe.codigoObjetivo})")
            type = "text/plain"
        }
        val selector = Intent.createChooser(intentoEnvio, "Descargar / Compartir Informe Pedagógico")
        selector.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        contexto.startActivity(selector)
    }
}
