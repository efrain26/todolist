package org.efradev.todolist.domain

import kotlinx.coroutines.test.runTest
import org.efradev.todolist.domain.repository.UserRepository
import org.efradev.todolist.domain.repository.PreferencesRepository
import org.efradev.todolist.domain.model.DomainAuthData
import org.efradev.todolist.domain.model.DomainUser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Test unitario para LoginUseCase siguiendo principios de TDD
 * 
 * Este test verifica el comportamiento del use case de login
 * siguiendo el patrón Given-When-Then
 */
class LoginUseCaseTest {

    private val fakeUserRepository = FakeUserRepositoryForTests()
    private val fakePreferencesRepository = FakePreferencesRepositoryForTests()
    private val fakeStringRes = FakeStringResProviderForTests()
    private val useCase = LoginUseCase(
        fakeUserRepository, 
        fakePreferencesRepository, 
        fakeStringRes::getString
    )

    @Test
    fun `should return success when login is successful`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val expectedAuthData = DomainAuthData(
            user = DomainUser(
                id = "user123",
                username = "testuser",
                email = email,
                firstName = "Test",
                lastName = "User",
                phoneNumber = "1234567890"
            ),
            accessToken = "access-token-123",
            refreshToken = "refresh-token-123",
            tokenType = "Bearer"
        )
        fakeUserRepository.nextLoginResult = Result.success(expectedAuthData)

        // When
        val result = useCase(email, password)

        // Then
        assertTrue(result.isSuccess)
        val loginResult = result.getOrNull()
        assertTrue(loginResult is LoginResult.Success)
        assertEquals("", (loginResult as LoginResult.Success).message)
        
        // Verificar que se guardaron los datos de auth
        assertEquals(expectedAuthData, fakePreferencesRepository.authData)
        
        // Verificar que se llamó con los parámetros correctos
        val lastCredentials = fakeUserRepository.lastLoginCredentials
        assertEquals(email, lastCredentials?.first)
        assertEquals(password, lastCredentials?.second)
    }

    @Test
    fun `should return failure when repository login fails`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "wrongpassword"
        val exception = RuntimeException("Invalid credentials")
        fakeUserRepository.nextLoginResult = Result.failure(exception)

        // When
        val result = useCase(email, password)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        
        // Verificar que NO se guardaron datos de auth
        assertEquals(null, fakePreferencesRepository.authData)
    }

    @Test
    fun `should create correct login request with email and password`() = runTest {
        // Given
        val email = "user@test.com"
        val password = "mypassword"
        val authData = DomainAuthData(
            user = DomainUser("1", "user", email, "First", "Last", "123"),
            accessToken = "token",
            refreshToken = "refresh",
            tokenType = "Bearer"
        )
        fakeUserRepository.nextLoginResult = Result.success(authData)

        // When
        val result = useCase(email, password)

        // Then
        assertTrue(result.isSuccess)
        val capturedCredentials = fakeUserRepository.lastLoginCredentials
        assertEquals(email, capturedCredentials?.first)
        assertEquals(password, capturedCredentials?.second)
    }

    @Test
    fun `should handle empty email and password`() = runTest {
        // Given
        val emptyEmail = ""
        val emptyPassword = ""
        val authData = DomainAuthData(
            user = DomainUser("1", "user", emptyEmail, "First", "Last", "123"),
            accessToken = "token",
            refreshToken = "refresh", 
            tokenType = "Bearer"
        )
        fakeUserRepository.nextLoginResult = Result.success(authData)

        // When
        val result = useCase(emptyEmail, emptyPassword)

        // Then
        assertTrue(result.isSuccess)
        val capturedCredentials = fakeUserRepository.lastLoginCredentials
        assertEquals(emptyEmail, capturedCredentials?.first)
        assertEquals(emptyPassword, capturedCredentials?.second)
    }

    @Test  
    fun `should save auth data when login succeeds`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val expectedAuth = DomainAuthData(
            user = DomainUser("1", "testuser", email, "Test", "User", "123"),
            accessToken = "access",
            refreshToken = "refresh",
            tokenType = "Bearer"
        )
        fakeUserRepository.nextLoginResult = Result.success(expectedAuth)

        // When
        val result = useCase(email, password)

        // Then
        assertTrue(result.isSuccess)
        assertTrue(fakePreferencesRepository.saveAuthDataWasCalled)
        assertEquals(expectedAuth, fakePreferencesRepository.authData)
    }
}
