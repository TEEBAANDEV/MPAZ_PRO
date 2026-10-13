package com.example.mpaz_pro.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpaz_pro.data.local.InformeEntity
import com.example.mpaz_pro.data.local.ObjetivoEntity
import com.example.mpaz_pro.data.model.FormularioObjetivoErrores
import com.example.mpaz_pro.data.model.FormularioObjetivoModel
import com.example.mpaz_pro.data.model.FormularioObservacionErrores
import com.example.mpaz_pro.data.model.FormularioObservacionModel
import com.example.mpaz_pro.data.repository.RepositorioApp
import com.example.mpaz_pro.native.AyudanteCompartirInforme
import com.example.mpaz_pro.native.AyudanteTextoAVoz
import com.example.mpaz_pro.native.AyudanteVibracion
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel principal y simplificado para la lógica pedagógica de MPAZ PRO.
 * Conecta UTP, Docente y Estudiante con la base de datos Room y recursos nativos.
 */
class MpazViewModel(
    private val repositorio: RepositorioApp,
    private val ayudanteTextoAVoz: AyudanteTextoAVoz,
    private val ayudanteVibracion: AyudanteVibracion
) : ViewModel() {

    // --- Flujos de datos Room ---
    val todosLosObjetivos: StateFlow<List<ObjetivoEntity>> = repositorio.obtenerTodosLosObjetivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val objetivosAsignados: StateFlow<List<ObjetivoEntity>> = repositorio.obtenerObjetivosAsignados()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val informes: StateFlow<List<InformeEntity>> = repositorio.obtenerTodosLosInformes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val estaHablando: StateFlow<Boolean> = ayudanteTextoAVoz.estaHablando

    // --- 1. RECURSOS NATIVOS: Text-To-Speech y Vibración ---
    fun reproducirAudio(texto: String) {
        ayudanteTextoAVoz.hablar(texto)
    }

    fun detenerAudio() {
        ayudanteTextoAVoz.detener()
    }

    fun vibrarAcierto() {
        ayudanteVibracion.reproducirExito()
    }

    fun vibrarError() {
        ayudanteVibracion.reproducirError()
    }

    // --- 2. PERFIL UTP: Gestión de Objetivos Curriculares ---
    private val _formularioObjetivo = MutableStateFlow(FormularioObjetivoModel())
    val formularioObjetivo: StateFlow<FormularioObjetivoModel> = _formularioObjetivo.asStateFlow()

    private val _erroresObjetivo = MutableStateFlow(FormularioObjetivoErrores())
    val erroresObjetivo: StateFlow<FormularioObjetivoErrores> = _erroresObjetivo.asStateFlow()

    fun prepararNuevoObjetivo(asignatura: String = "lenguaje", nombreAsignatura: String = "Lenguaje y Comunicación") {
        _formularioObjetivo.value = FormularioObjetivoModel(
            idObjetivo = "obj_${System.currentTimeMillis()}",
            idAsignatura = asignatura,
            nombreAsignatura = nombreAsignatura
        )
        _erroresObjetivo.value = FormularioObjetivoErrores()
    }

    fun actualizarCodigoObjetivo(valor: String) {
        _formularioObjetivo.value = _formularioObjetivo.value.copy(codigo = valor)
    }

    fun actualizarTituloObjetivo(valor: String) {
        _formularioObjetivo.value = _formularioObjetivo.value.copy(titulo = valor)
    }

    fun actualizarDescripcionObjetivo(valor: String) {
        _formularioObjetivo.value = _formularioObjetivo.value.copy(descripcion = valor)
    }

    fun actualizarPreguntaObjetivo(valor: String) {
        _formularioObjetivo.value = _formularioObjetivo.value.copy(pregunta = valor)
    }

    fun validarFormularioObjetivo(): Boolean {
        val form = _formularioObjetivo.value
        val errCodigo = if (form.codigo.isBlank()) "El código es obligatorio (ej: OA 05)" else null
        val errTitulo = if (form.titulo.length < 3) "El título debe tener al menos 3 caracteres" else null
        val errDesc = if (form.descripcion.length < 5) "La descripción debe tener al menos 5 caracteres" else null
        val errPregunta = if (form.pregunta.isBlank()) "Debe incluir una pregunta para el estudiante" else null

        val errores = FormularioObjetivoErrores(errCodigo, errTitulo, errDesc, errPregunta)
        _erroresObjetivo.value = errores
        return errores == FormularioObjetivoErrores()
    }

    fun guardarObjetivo(alTerminar: () -> Unit) {
        if (!validarFormularioObjetivo()) return

        val form = _formularioObjetivo.value
        viewModelScope.launch {
            val nuevo = ObjetivoEntity(
                id = form.idObjetivo,
                idAsignatura = form.idAsignatura,
                nombreAsignatura = form.nombreAsignatura,
                codigo = form.codigo.trim().uppercase(),
                titulo = form.titulo.trim(),
                descripcion = form.descripcion.trim(),
                pregunta = form.pregunta.trim(),
                opciones = if (form.opciones.isBlank()) "Opción A,Opción B,Opción C" else form.opciones,
                indiceCorrecto = form.indiceCorrecto,
                estado = "AUTORIZADO",
                asignadoAlCurso = true
            )
            repositorio.guardarObjetivo(nuevo)
            alTerminar()
        }
    }

    fun autorizarObjetivo(id: String) {
        viewModelScope.launch {
            repositorio.actualizarEstadoObjetivo(id, "AUTORIZADO")
        }
    }

    // --- 3. PERFIL DOCENTE: Asignación y Observación Pedagógica ---
    private val _formularioObs = MutableStateFlow(FormularioObservacionModel())
    val formularioObs: StateFlow<FormularioObservacionModel> = _formularioObs.asStateFlow()

    private val _erroresObs = MutableStateFlow(FormularioObservacionErrores())
    val erroresObs: StateFlow<FormularioObservacionErrores> = _erroresObs.asStateFlow()

    fun alternarAsignacionObjetivo(id: String, asignado: Boolean) {
        viewModelScope.launch {
            repositorio.actualizarAsignacionObjetivo(id, asignado)
        }
    }

    fun cargarInformeParaObservacion(idInforme: String, obsActual: String) {
        _formularioObs.value = FormularioObservacionModel(idInforme, obsActual)
        _erroresObs.value = FormularioObservacionErrores()
    }

    fun actualizarTextoObservacion(valor: String) {
        _formularioObs.value = _formularioObs.value.copy(observacion = valor)
    }

    fun guardarObservacionDocente(alTerminar: () -> Unit) {
        val form = _formularioObs.value
        if (form.observacion.isBlank()) {
            _erroresObs.value = FormularioObservacionErrores("La observación no puede estar vacía")
            return
        }

        viewModelScope.launch {
            repositorio.actualizarObservacionInforme(form.idInforme, form.observacion.trim())
            alTerminar()
        }
    }

    fun compartirInformeNativo(contexto: Context, informe: InformeEntity) {
        AyudanteCompartirInforme.compartirInforme(contexto, informe)
    }

    // --- 4. PERFIL ESTUDIANTE: Respuesta Interactiva y Progreso ---
    fun responderPregunta(
        indiceSeleccionado: Int,
        objetivo: ObjetivoEntity,
        alVerificar: (Boolean) -> Unit
    ) {
        val esCorrecto = indiceSeleccionado == objetivo.indiceCorrecto
        if (esCorrecto) {
            vibrarAcierto()
            reproducirAudio("¡Excelente! Respuesta correcta.")
        } else {
            vibrarError()
            reproducirAudio("¡Casi! Inténtalo de nuevo.")
        }
        alVerificar(esCorrecto)
    }

    override fun onCleared() {
        super.onCleared()
        detenerAudio()
    }
}
