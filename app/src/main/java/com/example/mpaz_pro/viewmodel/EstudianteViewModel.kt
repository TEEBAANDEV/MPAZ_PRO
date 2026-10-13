package com.example.mpaz_pro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpaz_pro.data.local.entity.*
import com.example.mpaz_pro.data.repository.RepositorioApp
import com.example.mpaz_pro.native.AyudanteTextoAVoz
import com.example.mpaz_pro.native.AyudanteVibracion
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class EstadoDesafio(
    val indiceDesafioActual: Int = 0,
    val indiceOpcionSeleccionada: Int? = null,
    val respuestaVerificada: Boolean = false,
    val esCorrecta: Boolean = false,
    val puntaje: Int = 0,
    val estaCompletado: Boolean = false
)

data class EstadoEvaluacion(
    val indicePreguntaActual: Int = 0,
    val indiceOpcionSeleccionada: Int? = null,
    val respuestasSeleccionadas: MutableMap<Int, Int> = mutableMapOf(),
    val estaFinalizada: Boolean = false,
    val puntajeFinal: Int = 0,
    val puntajeMaximo: Int = 20,
    val cantidadCorrectas: Int = 0,
    val cantidadTotal: Int = 0,
    val intentosRestantes: Int = 2,
    val puedeRendirEvaluacion: Boolean = true
)

class EstudianteViewModel(
    private val repositorio: RepositorioApp,
    private val ayudanteVoz: AyudanteTextoAVoz,
    private val ayudanteVibracion: AyudanteVibracion
) : ViewModel() {

    val objetivosAsignados: StateFlow<List<ObjetivoAprendizajeEntity>> =
        repositorio.obtenerObjetivosAsignadosParaEstudiantes()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val estaHablando: StateFlow<Boolean> = ayudanteVoz.estaHablando

    private val _contenidoActual = MutableStateFlow<ContenidoDidacticoEntity?>(null)
    val contenidoActual: StateFlow<ContenidoDidacticoEntity?> = _contenidoActual.asStateFlow()

    private val _desafiosActuales = MutableStateFlow<List<DesafioEntity>>(emptyList())
    val desafiosActuales: StateFlow<List<DesafioEntity>> = _desafiosActuales.asStateFlow()

    private val _estadoDesafio = MutableStateFlow(EstadoDesafio())
    val estadoDesafio: StateFlow<EstadoDesafio> = _estadoDesafio.asStateFlow()

    private val _preguntasEvaluacionActuales = MutableStateFlow<List<PreguntaEvaluacionEntity>>(emptyList())
    val preguntasEvaluacionActuales: StateFlow<List<PreguntaEvaluacionEntity>> = _preguntasEvaluacionActuales.asStateFlow()

    private val _estadoEvaluacion = MutableStateFlow(EstadoEvaluacion())
    val estadoEvaluacion: StateFlow<EstadoEvaluacion> = _estadoEvaluacion.asStateFlow()

    fun cargarContenidoParaObjetivo(idObjetivo: String) {
        ayudanteVoz.detener()
        viewModelScope.launch {
            val contenido = repositorio.obtenerPrimerContenidoPorObjetivo(idObjetivo)
            _contenidoActual.value = contenido
        }
    }

    fun reproducirAudio(texto: String) {
        ayudanteVoz.hablar(texto)
    }

    fun detenerAudio() {
        ayudanteVoz.detener()
    }

    // --- ACTIVIDADES / DESAF├ìOS ---
    fun cargarDesafiosParaObjetivo(idObjetivo: String) {
        ayudanteVoz.detener()
        _estadoDesafio.value = EstadoDesafio()
        viewModelScope.launch {
            repositorio.obtenerDesafiosPorObjetivo(idObjetivo).collect { lista ->
                _desafiosActuales.value = lista
            }
        }
    }

    fun alSeleccionarOpcionDesafio(indice: Int) {
        if (_estadoDesafio.value.respuestaVerificada) return
        _estadoDesafio.update { it.copy(indiceOpcionSeleccionada = indice) }
    }

    fun verificarRespuestaDesafio() {
        val estado = _estadoDesafio.value
        val desafios = _desafiosActuales.value
        if (desafios.isEmpty() || estado.indiceOpcionSeleccionada == null) return

        val desafioActual = desafios[estado.indiceDesafioActual]
        val esCorrecta = estado.indiceOpcionSeleccionada == desafioActual.indiceCorrecto

        if (esCorrecta) {
            ayudanteVibracion.reproducirExito()
        } else {
            ayudanteVibracion.reproducirError()
        }

        _estadoDesafio.update {
            it.copy(
                respuestaVerificada = true,
                esCorrecta = esCorrecta,
                puntaje = if (esCorrecta) it.puntaje + 10 else it.puntaje
            )
        }
    }

    fun siguienteDesafio() {
        val estado = _estadoDesafio.value
        val desafios = _desafiosActuales.value
        if (estado.indiceDesafioActual + 1 < desafios.size) {
            _estadoDesafio.update {
                it.copy(
                    indiceDesafioActual = it.indiceDesafioActual + 1,
                    indiceOpcionSeleccionada = null,
                    respuestaVerificada = false,
                    esCorrecta = false
                )
            }
        } else {
            _estadoDesafio.update { it.copy(estaCompletado = true) }
            ayudanteVibracion.reproducirCelebracion()
        }
    }

    // --- EVALUACI├ôN FINAL ---
    fun cargarEvaluacionParaObjetivo(idEstudiante: String, objetivo: ObjetivoAprendizajeEntity) {
        ayudanteVoz.detener()
        viewModelScope.launch {
            val progresoExistente = repositorio.obtenerProgresoPorObjetivo(idEstudiante, objetivo.id)
            val intentosUsados = progresoExistente?.intentosUsados ?: 0
            val restantes = (objetivo.intentosMaximos - intentosUsados).coerceAtLeast(0)
            val puedeRendir = restantes > 0

            _estadoEvaluacion.value = EstadoEvaluacion(
                intentosRestantes = restantes,
                puedeRendirEvaluacion = puedeRendir
            )

            repositorio.obtenerPreguntasPorObjetivo(objetivo.id).collect { preguntas ->
                _preguntasEvaluacionActuales.value = preguntas
            }
        }
    }

    fun alSeleccionarOpcionEvaluacion(indice: Int) {
        _estadoEvaluacion.update { it.copy(indiceOpcionSeleccionada = indice) }
    }

    fun siguientePreguntaEvaluacion() {
        val estado = _estadoEvaluacion.value
        val preguntas = _preguntasEvaluacionActuales.value
        if (estado.indiceOpcionSeleccionada == null) return

        estado.respuestasSeleccionadas[estado.indicePreguntaActual] = estado.indiceOpcionSeleccionada!!

        if (estado.indicePreguntaActual + 1 < preguntas.size) {
            val siguienteIndice = estado.indicePreguntaActual + 1
            _estadoEvaluacion.update {
                it.copy(
                    indicePreguntaActual = siguienteIndice,
                    indiceOpcionSeleccionada = estado.respuestasSeleccionadas[siguienteIndice]
                )
            }
        }
    }

    fun entregarEvaluacion(
        estudiante: UsuarioEntity,
        objetivo: ObjetivoAprendizajeEntity,
        nombreAsignatura: String
    ) {
        val estado = _estadoEvaluacion.value
        val preguntas = _preguntasEvaluacionActuales.value
        if (estado.indiceOpcionSeleccionada != null) {
            estado.respuestasSeleccionadas[estado.indicePreguntaActual] = estado.indiceOpcionSeleccionada!!
        }

        var cantidadCorrectas = 0
        var puntajeTotal = 0
        preguntas.forEachIndexed { idx, p ->
            val elegida = estado.respuestasSeleccionadas[idx]
            if (elegida != null && elegida == p.indiceCorrecto) {
                cantidadCorrectas++
                puntajeTotal += p.puntaje
            }
        }
        val maximoPuntos = preguntas.sumOf { it.puntaje }.coerceAtLeast(20)
        val porcentajeCalculado = ((puntajeTotal.toDouble() / maximoPuntos.toDouble()) * 100).toInt()

        viewModelScope.launch {
            val progresoExistente = repositorio.obtenerProgresoPorObjetivo(estudiante.id, objetivo.id)
            val nuevosIntentosUsados = (progresoExistente?.intentosUsados ?: 0) + 1
            val formatoFecha = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val marcaTiempo = formatoFecha.format(Date())

            val progresoActualizado = ProgresoEstudianteEntity(
                id = "prog_${estudiante.id}_${objetivo.id}",
                idEstudiante = estudiante.id,
                idObjetivo = objetivo.id,
                contenidoCompletado = true,
                desafiosCompletados = 2,
                totalDesafios = 2,
                intentosUsados = nuevosIntentosUsados,
                ultimoPuntaje = puntajeTotal,
                puntajeMaximo = maximoPuntos,
                fechaUltimoIntento = marcaTiempo,
                porcentaje = porcentajeCalculado,
                textoEstado = if (porcentajeCalculado >= 60) "Aprobado" else "Requiere Refuerzo"
            )
            repositorio.guardarProgreso(progresoActualizado)

            // Guardar reporte pedag├│gico reservado para el docente
            val informe = InformePedagogicoEntity(
                id = "rep_${estudiante.id}_${objetivo.id}",
                idEstudiante = estudiante.id,
                nombreEstudiante = estudiante.nombreCompleto,
                nombreCurso = estudiante.curso,
                nombreAsignatura = nombreAsignatura,
                codigoObjetivo = objetivo.codigo,
                tituloObjetivo = objetivo.titulo,
                puntajeGlobal = puntajeTotal,
                puntajeMaximo = maximoPuntos,
                porcentaje = porcentajeCalculado,
                fortalezas = if (porcentajeCalculado >= 60) "Logra resolver con ├®xito los ejercicios del objetivo evaluado." else "Participa activamente en la resoluci├│n guiada.",
                areasRefuerzo = if (porcentajeCalculado < 60) "Reforzar comprensi├│n de instrucciones y repasar contenidos clave con el docente." else "Mantener pr├íctica constante.",
                observacionDocente = "",
                fechaEmision = marcaTiempo,
                estaValidadoPorDocente = false
            )
            repositorio.guardarInforme(informe)

            val restantes = (objetivo.intentosMaximos - nuevosIntentosUsados).coerceAtLeast(0)
            _estadoEvaluacion.update {
                it.copy(
                    estaFinalizada = true,
                    puntajeFinal = puntajeTotal,
                    puntajeMaximo = maximoPuntos,
                    cantidadCorrectas = cantidadCorrectas,
                    cantidadTotal = preguntas.size,
                    intentosRestantes = restantes,
                    puedeRendirEvaluacion = restantes > 0
                )
            }

            if (porcentajeCalculado >= 60) {
                ayudanteVibracion.reproducirCelebracion()
            } else {
                ayudanteVibracion.reproducirError()
            }
        }
    }

    override fun onCleared() {
        ayudanteVoz.detener()
        super.onCleared()
    }
}
