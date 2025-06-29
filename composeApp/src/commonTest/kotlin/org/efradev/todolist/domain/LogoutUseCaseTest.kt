package org.efradev.todolist.domain

import kotlinx.coroutines.test.runTest
import org.efradev.todolist.domain.repository.PreferencesRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Test unitario para LogoutUseCase siguiendo principios de TDD
 * 
 * Este test verifica el comportamiento del use case de logout
 * siguiendo el patrón Given-When-Then
 */
class LogoutUseCaseTest {

    private val fakePreferencesRepository = FakePreferencesRepositoryForTests()
    private val fakeStringRes = FakeStringResProviderForTests()
    private val useCase = LogoutUseCase(fakePreferencesRepository, fakeStringRes::getString)

    @Test
    fun `should return success when logout completes successfully`() = runTest {
        // Given
        fakeStringRes.strings["logout_success"] = "Sesión cerrada exitosamente"
        fakePreferencesRepository.shouldThrowException = false

        // When
        val result = useCase()

        // Then
        assertTrue(result.isSuccess)
        val logoutResult = result.getOrNull()
        assertTrue(logoutResult is LogoutResult.Success)
        assertEquals("Sesión cerrada exitosamente", (logoutResult as LogoutResult.Success).message)
        assertTrue(fakePreferencesRepository.clearAuthDataWasCalled)
    }

    @Test
    fun `should return error when preferences repository throws exception`() = runTest {
        // Given
        fakeStringRes.strings["logout_error"] = "Error al cerrar sesión"
        fakePreferencesRepository.shouldThrowException = true

        // When
        val result = useCase()

        // Then
        assertTrue(result.isSuccess)
        val logoutResult = result.getOrNull()
        assertTrue(logoutResult is LogoutResult.Error)
        assertEquals("Error al cerrar sesión", (logoutResult as LogoutResult.Error).message)
        assertTrue(fakePreferencesRepository.clearAuthDataWasCalled)
    }

    @Test
    fun `should handle missing string resources gracefully`() = runTest {
        // Given
        // Crear una instancia sin valores por defecto
        val emptyStringRes = FakeStringResProviderForTests()
        emptyStringRes.strings.clear() // Limpiar valores por defecto
        val testUseCase = LogoutUseCase(fakePreferencesRepository, emptyStringRes::getString)
        fakePreferencesRepository.shouldThrowException = false

        // When
        val result = testUseCase()

        // Then
        assertTrue(result.isSuccess)
        val logoutResult = result.getOrNull()
        assertTrue(logoutResult is LogoutResult.Success)
        assertEquals("String not found: logout_success", (logoutResult as LogoutResult.Success).message)
    }

    @Test
    fun `should clear auth data even when exception occurs`() = runTest {
        // Given
        fakeStringRes.strings["logout_error"] = "Error al cerrar sesión"
        fakePreferencesRepository.shouldThrowException = true

        // When
        val result = useCase()

        // Then
        assertTrue(result.isSuccess)
        assertTrue(fakePreferencesRepository.clearAuthDataWasCalled)
        val logoutResult = result.getOrNull()
        assertTrue(logoutResult is LogoutResult.Error)
    }
}
