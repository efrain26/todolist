package org.efradev.todolist.domain

import kotlinx.coroutines.test.runTest
import org.efradev.todolist.data.UserCheckResult
import org.efradev.todolist.data.UserRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Test unitario para CheckUserExistsUseCase y UserCheckResultWithMessage siguiendo principios de TDD
 * 
 * Este test verifica el comportamiento del use case de verificación de usuario existente
 * y cómo se transforman los resultados con mensajes localizados
 * siguiendo el patrón Given-When-Then
 */
class CheckUserExistsUseCaseTest {

    private val fakeUserRepository = FakeUserRepository()
    private val fakeStringRes = FakeStringResProvider()
    private val useCase = CheckUserExistsUseCase(fakeUserRepository, fakeStringRes::getString)

    @Test
    fun `should return registered result when user exists`() = runTest {
        // Given
        val email = "test@example.com"
        fakeUserRepository.nextResult = Result.success(UserCheckResult.Registered)
        fakeStringRes.strings["user_registered"] = "Usuario registrado"

        // When
        val result = useCase(email)

        // Then
        assertTrue(result.isSuccess)
        val userResult = result.getOrNull()
        assertTrue(userResult is UserCheckResultWithMessage.Registered)
        assertEquals("Usuario registrado", (userResult as UserCheckResultWithMessage.Registered).message)
        assertEquals(email, fakeUserRepository.lastEmailChecked)
    }

    @Test
    fun `should return not registered result when user does not exist`() = runTest {
        // Given
        val email = "nonexistent@example.com"
        fakeUserRepository.nextResult = Result.success(UserCheckResult.NotRegistered)
        fakeStringRes.strings["user_not_registered"] = "Usuario no registrado"

        // When
        val result = useCase(email)

        // Then
        assertTrue(result.isSuccess)
        val userResult = result.getOrNull()
        assertTrue(userResult is UserCheckResultWithMessage.NotRegistered)
        assertEquals("Usuario no registrado", (userResult as UserCheckResultWithMessage.NotRegistered).message)
        assertEquals(email, fakeUserRepository.lastEmailChecked)
    }

    @Test
    fun `should return error result when repository returns error with message`() = runTest {
        // Given
        val email = "error@example.com"
        val errorCode = "NETWORK_ERROR"
        val errorMessage = "Network connection failed"
        fakeUserRepository.nextResult = Result.success(UserCheckResult.Error(errorCode, errorMessage))

        // When
        val result = useCase(email)

        // Then
        assertTrue(result.isSuccess)
        val userResult = result.getOrNull()
        assertTrue(userResult is UserCheckResultWithMessage.Error)
        assertEquals(errorMessage, (userResult as UserCheckResultWithMessage.Error).message)
    }

    @Test
    fun `should return error result with default message when repository returns error without message`() = runTest {
        // Given
        val email = "error@example.com"
        fakeUserRepository.nextResult = Result.success(UserCheckResult.Error("CODE", null))
        fakeStringRes.strings["unexpected_error"] = "Error inesperado"

        // When
        val result = useCase(email)

        // Then
        assertTrue(result.isSuccess)
        val userResult = result.getOrNull()
        assertTrue(userResult is UserCheckResultWithMessage.Error)
        assertEquals("Error inesperado", (userResult as UserCheckResultWithMessage.Error).message)
    }

    @Test
    fun `should propagate repository failure`() = runTest {
        // Given
        val email = "test@example.com"
        val exception = RuntimeException("Repository failure")
        fakeUserRepository.nextResult = Result.failure(exception)

        // When
        val result = useCase(email)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `should handle empty email properly`() = runTest {
        // Given
        val email = ""
        fakeUserRepository.nextResult = Result.success(UserCheckResult.Error("INVALID_EMAIL", "Email requerido"))

        // When
        val result = useCase(email)

        // Then
        assertTrue(result.isSuccess)
        val userResult = result.getOrNull()
        assertTrue(userResult is UserCheckResultWithMessage.Error)
        assertEquals("Email requerido", (userResult as UserCheckResultWithMessage.Error).message)
        assertEquals(email, fakeUserRepository.lastEmailChecked)
    }
}

/**
 * Implementación fake del UserRepository para testing
 */
class FakeUserRepository : UserRepository {
    var nextResult: Result<UserCheckResult> = Result.success(UserCheckResult.NotRegistered)
    var lastEmailChecked: String? = null

    override suspend fun checkUser(email: String): Result<UserCheckResult> {
        lastEmailChecked = email
        return nextResult
    }

    override suspend fun registerUser(request: org.efradev.todolist.data.model.RegisterRequest): Result<org.efradev.todolist.data.model.RegisterResponse> {
        TODO("Not needed for this test")
    }

    override suspend fun login(request: org.efradev.todolist.data.model.LoginRequest): Result<org.efradev.todolist.data.model.AuthResponse> {
        TODO("Not needed for this test")
    }
}

/**
 * Implementación fake del StringResProvider para testing
 */
class FakeStringResProvider {
    val strings = mutableMapOf<String, String>()

    fun getString(key: String): String {
        return strings[key] ?: "Missing string: $key"
    }
}
