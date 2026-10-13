package com.example.mpaz_pro.native

import android.content.Context
import android.content.Intent
import com.example.mpaz_pro.data.local.entity.PedagogicalReportEntity

object ShareReportHelper {

    fun shareReport(context: Context, report: PedagogicalReportEntity) {
        val reportBody = """
            ==============================================
            ESCUELA ESCRITORA MARCELA PAZ - SAN BERNARDO
            INFORME PEDAGÓGICO DE EVALUACIÓN - 2° BÁSICO
            ==============================================
            Estudiante: ${report.studentName}
            Curso: ${report.courseName}
            Asignatura: ${report.subjectName}
            Objetivo: ${report.objectiveCode} - ${report.objectiveTitle}
            Fecha de Emisión: ${report.emissionDate}
            ----------------------------------------------
            RESULTADOS:
            Puntaje Obtenido: ${report.globalScore} de ${report.maxScore} pts (${report.percentage}%)
            Diagnóstico de Fortalezas:
            ${report.strengths}
            
            Áreas de Refuerzo Prioritarias:
            ${report.reinforcementAreas}
            ----------------------------------------------
            OBSERVACIONES PEDAGÓGICAS DEL DOCENTE:
            ${if (report.teacherObservation.isNotBlank()) report.teacherObservation else "(Sin observaciones registradas)"}
            
            Validado por Docente: ${if (report.isValidatedByTeacher) "SÍ (Firma Digital Activa)" else "NO"}
            ==============================================
            Generado automáticamente por MPAZ PRO 2.0
        """.trimIndent()

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, reportBody)
            putExtra(Intent.EXTRA_SUBJECT, "Informe Pedagógico - ${report.studentName} (${report.objectiveCode})")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Descargar / Compartir Informe Pedagógico")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }
}
