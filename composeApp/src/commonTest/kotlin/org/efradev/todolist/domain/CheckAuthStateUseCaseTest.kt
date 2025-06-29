package org.efradev.todolist.domain

import kotlinx.coroutines.test.runTest
import org.efradev.todolist.data.local.PreferencesRepository
import org.efradev.todolist.data.model.AuthResponse
import org.efradev.todolist.data.model.User
import org.efradev.todolist.data.model.Token
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Test unitario para CheckAuthStateUseCase siguiendo principios de TDD
 * 
 * Este test verifica el comportamiento del use case de verificación de estado de autenticación
 * siguiendo el patrón Given-When-Then
 */
class CheckAuthStateUseCaseTest {

    private val fakePreferencesRepository = FakePreferencesRepository()
    private val useCase = CheckAuthStateUseCase(fakePreferencesRepository)

    @Test
    fun `should return authenticated state when user is logged in and has auth data`() = runTest {
        // Given
        val expectedUser = AuthResponse(
            user = User(
                id = "user123",
                username = "testuser",
                email = "test@example.com",
                firstName = "Test",
                lastName = "User",
                phoneNumber = "1234567890"
            ),
            token = Token(
                accessToken = "test-access-token",
                refreshToken = "test-refresh-token",
                tokenType = "Bearer"
            )
        )
        fakePreferencesRepository.isLoggedIn = true
        fakePreferencesRepository.authData = expectedUser

        // When
        val result = useCase()

        // Then
        assertTrue(result.isSuccess)
        val authState = result.getOrNull()
        assertTrue(authState is AuthState.Authenticated)
        assertEquals(expectedUser, (authState as AuthState.Authenticated).authData)
    }

    @Test
    fun `should return not authenticated when user is not logged in`() = runTest {
        // Given
        fakePreferencesRepository.isLoggedIn = false
        fakePreferencesRepository.authData = null

        // When
        val result = useCase()

        // Then
        assertTrue(result.isSuccess)
        val authState = result.getOrNull()
        assertTrue(authState is AuthState.NotAuthenticated)
    }

    @Test
    fun `should return not authenticated when user is logged in but has no auth data`() = runTest {
        // Given
        fakePreferencesRepository.isLoggedIn = true
        fakePreferencesRepository.authData = null

        // When
        val result = useCase()

        // Then
        assertTrue(result.isSuccess)
        val authState = result.getOrNull()
        assertTrue(authState is AuthState.NotAuthenticated)
    }

    @Test
    fun `should return failure when preferences repository throws exception`() = runTest {
        // Given
        fakePreferencesRepository.shouldThrowException = true

        // When
        val result = useCase()

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is RuntimeException)
    }
}

/**
 * Implementación fake del PreferencesRepository para testing
 */
class FakePreferencesRepository : PreferencesRepository {
    var isLoggedIn: Boolean = false
    var authData: AuthResponse? = null
    var shouldThrowException: Boolean = false

    override suspend fun saveAuthData(authResponse: AuthResponse) {
        if (shouldThrowException) throw RuntimeException("Test exception")
        this.authData = authResponse
        this.isLoggedIn = true
    }

    override suspend fun getAuthData(): AuthResponse? {
        if (shouldThrowException) throw RuntimeException("Test exception")
        return authData
    }

    override suspend fun clearAuthData() {
        if (shouldThrowException) throw RuntimeException("Test exception")
        this.authData = null
        this.isLoggedIn = false
    }

    override suspend fun isUserLoggedIn(): Boolean {
        if (shouldThrowException) throw RuntimeException("Test exception")
        return isLoggedIn
    }
}
