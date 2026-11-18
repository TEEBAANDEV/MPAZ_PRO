package com.example.mpaz_pro

import com.example.mpaz_pro.data.local.UsuarioEntity
import com.example.mpaz_pro.data.repository.RepositorioApp
import com.example.mpaz_pro.viewmodel.AutenticacionViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AutenticacionViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repositorio: RepositorioApp
    private lateinit var viewModel: AutenticacionViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repositorio = mockk(relaxed = true)
        viewModel = AutenticacionViewModel(repositorio)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `validar retorna false cuando usuario y clave estan vacios`() {
        val esValido = viewModel.validar()
        assertFalse(esValido)
        assertNotNull(viewModel.errores.value.rut)
        assertNotNull(viewModel.errores.value.clave)
    }

    @Test
    fun `validar retorna true cuando usuario y clave tienen datos`() {
        viewModel.alCambiarUsuario("docente")
        viewModel.alCambiarClave("1234")
        assertTrue(viewModel.validar())
        assertNull(viewModel.errores.value.rut)
        assertNull(viewModel.errores.value.clave)
    }

    @Test
    fun `iniciarSesion exitoso actualiza usuarioActual y ejecuta callback`() = runTest {
        val usuario = UsuarioEntity("docente", "1234", "Prof. Camila", "DOCENTE")
        coEvery { repositorio.iniciarSesion("docente", "1234") } returns usuario

        viewModel.alCambiarUsuario("docente")
        viewModel.alCambiarClave("1234")
        var rolRetornado: String? = null
        viewModel.iniciarSesion { rol -> rolRetornado = rol }
        advanceUntilIdle()

        assertEquals("DOCENTE", rolRetornado)
        assertEquals(usuario, viewModel.estadoUi.value.usuarioActual)
        assertNull(viewModel.estadoUi.value.errorGeneral)
    }

    @Test
    fun `iniciarSesion fallido muestra errorGeneral`() = runTest {
        coEvery { repositorio.iniciarSesion("malo", "0000") } returns null

        viewModel.alCambiarUsuario("malo")
        viewModel.alCambiarClave("0000")
        viewModel.iniciarSesion {}
        advanceUntilIdle()

        assertNotNull(viewModel.estadoUi.value.errorGeneral)
        assertNull(viewModel.estadoUi.value.usuarioActual)
    }

    @Test
    fun `cerrarSesion limpia el estado del formulario y usuarioActual`() {
        viewModel.alCambiarUsuario("docente")
        viewModel.cerrarSesion()

        assertEquals("", viewModel.formulario.value.rut)
        assertNull(viewModel.estadoUi.value.usuarioActual)
    }
}
