package com.example.mpaz_pro

import com.example.mpaz_pro.data.local.AppDao
import com.example.mpaz_pro.data.local.UsuarioEntity
import com.example.mpaz_pro.data.remote.*
import com.example.mpaz_pro.data.repository.RepositorioApp
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class RepositorioAppTest {

    private lateinit var dao: AppDao
    private lateinit var mpazApi: MpazApiService
    private lateinit var externalApi: ExternalApiService
    private lateinit var repositorio: RepositorioApp

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        mpazApi = mockk()
        externalApi = mockk()
        TokenManager.limpiarToken()
        repositorio = RepositorioApp(dao, mpazApi, externalApi)
    }

    @Test
    fun `iniciarSesion guarda token JWT cuando el microservicio responde OK`() = runTest {
        val authResp = AuthResponse("jwt-token-123", "docente", "Prof. Camila", "DOCENTE")
        coEvery { mpazApi.login(LoginRequest("docente", "1234")) } returns Response.success(authResp)
        coEvery { dao.iniciarSesion("docente", "1234") } returns UsuarioEntity("docente", "1234", "Prof. Camila", "DOCENTE")

        val usuario = repositorio.iniciarSesion("docente", "1234")

        assertNotNull(usuario)
        assertEquals("DOCENTE", usuario?.rol)
        assertEquals("jwt-token-123", TokenManager.jwtToken)
    }

    @Test
    fun `iniciarSesion hace fallback a Room cuando el microservicio falla`() = runTest {
        coEvery { mpazApi.login(any()) } throws RuntimeException("Connection refused")
        coEvery { dao.iniciarSesion("utp", "1234") } returns UsuarioEntity("utp", "1234", "Jefatura UTP", "UTP")

        val usuario = repositorio.iniciarSesion("utp", "1234")

        assertNotNull(usuario)
        assertEquals("UTP", usuario?.rol)
        assertNull(TokenManager.jwtToken)
        coVerify(exactly = 1) { dao.iniciarSesion("utp", "1234") }
    }

    @Test
    fun `obtenerFraseMotivacionalExterna retorna success cuando la API responde 200`() = runTest {
        val dto = FraseExternaDto(1, "Aprender es crecer", "Autor")
        coEvery { externalApi.obtenerFraseMotivacional() } returns Response.success(dto)

        val result = repositorio.obtenerFraseMotivacionalExterna()

        assertTrue(result.isSuccess)
        assertEquals("Aprender es crecer", result.getOrNull()?.quote)
    }

    @Test
    fun `obtenerFraseMotivacionalExterna retorna failure cuando la API responde error`() = runTest {
        coEvery { externalApi.obtenerFraseMotivacional() } returns Response.error(500, "Error".toResponseBody())

        val result = repositorio.obtenerFraseMotivacionalExterna()

        assertTrue(result.isFailure)
    }

    @Test
    fun `obtenerObjetivosRemotos retorna lista desde el microservicio`() = runTest {
        val lista = listOf(ObjetivoRemotoDto(1L, "OA 01", "Lectura", "Desc", "Lenguaje", "PUBLICADO"))
        coEvery { mpazApi.obtenerObjetivos() } returns Response.success(lista)

        val result = repositorio.obtenerObjetivosRemotos()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
    }

    @Test
    fun `crearObjetivoRemoto retorna objetivo creado`() = runTest {
        val nuevo = ObjetivoRemotoDto(null, "OA 02", "Escritura", "Desc", "Lenguaje", "BORRADOR")
        val creado = nuevo.copy(id = 10L)
        coEvery { mpazApi.crearObjetivo(nuevo) } returns Response.success(creado)

        val result = repositorio.crearObjetivoRemoto(nuevo)

        assertTrue(result.isSuccess)
        assertEquals(10L, result.getOrNull()?.id)
    }
}
