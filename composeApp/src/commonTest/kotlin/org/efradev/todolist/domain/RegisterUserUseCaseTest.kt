package org.efradev.todolist.domain

import kotlinx.coroutines.test.runTest
import org.efradev.todolist.domain.repository.UserRepository
import org.efradev.todolist.domain.model.DomainRegistrationResult
import org.efradev.todolist.domain.model.DomainUserRegistration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Test unitario para RegisterUserUseCase siguiendo principios de TDD
 * 
 * Este test verifica el comportamiento del use case de registro de usuario
 * siguiendo el patrón Given-When-Then
 */
class RegisterUserUseCaseTest {

    private val fakeUserRepository = FakeUserRepositoryForTests()
    private val fakeStringRes = FakeStringResProviderForTests()
    private val useCase = RegisterUserUseCase(fakeUserRepository, fakeStringRes::getString)

    @Test
    fun `should return success when registration is successful`() = runTest {
        // Given
        val username = "testuser"
        val password = "password123"
        val email = "test@example.com"
        val firstName = "Test"
        val lastName = "User"
        val phoneNumber = "1234567890"
        
        val expectedResponse = DomainRegistrationResult(
            id = "123",
            username = "testuser"
        )
        fakeUserRepository.nextRegisterResult = Result.success(expectedResponse)

        // When
        val result = useCase(username, password, email, firstName, lastName, phoneNumber)

        // Then
        assertTrue(result.isSuccess)
        val registerResult = result.getOrNull()
        assertTrue(registerResult is RegisterResult.Success)
        assertEquals("testuser", (registerResult as RegisterResult.Success).message)
        
        // Verificar que se creó la request correctamente
        val capturedRegistration = fakeUserRepository.lastRegistrationData
        assertEquals(username, capturedRegistration?.username)
        assertEquals(password, capturedRegistration?.password)
        assertEquals(email, capturedRegistration?.email)
        assertEquals(firstName, capturedRegistration?.firstName)
        assertEquals(lastName, capturedRegistration?.lastName)
        assertEquals(phoneNumber, capturedRegistration?.phoneNumber)
    }

    @Test
    fun `should use default message when response username is null`() = runTest {
        // Given
        val username = "testuser"
        val password = "password123"
        val email = "test@example.com"
        val firstName = "Test"
        val lastName = "User"
        val phoneNumber = "1234567890"
        
        val responseWithNullUsername = DomainRegistrationResult(
            id = "123",
            username = null
        )
        fakeUserRepository.nextRegisterResult = Result.success(responseWithNullUsername)
        fakeStringRes.strings["register_success"] = "Registro exitoso"

        // When
        val result = useCase(username, password, email, firstName, lastName, phoneNumber)

        // Then
        assertTrue(result.isSuccess)
        val registerResult = result.getOrNull()
        assertTrue(registerResult is RegisterResult.Success)
        assertEquals("Registro exitoso", (registerResult as RegisterResult.Success).message)
    }

    @Test
    fun `should propagate repository failure when registration fails`() = runTest {
        // Given
        val username = "testuser"
        val password = "password123"
        val email = "test@example.com"
        val firstName = "Test"
        val lastName = "User"
        val phoneNumber = "1234567890"
        
        val exception = RuntimeException("Email already exists")
        fakeUserRepository.nextRegisterResult = Result.failure(exception)

        // When
        val result = useCase(username, password, email, firstName, lastName, phoneNumber)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `should create register request with all provided parameters`() = runTest {
        // Given
        val username = "newuser"
        val password = "securepassword"
        val email = "newuser@example.com"
        val firstName = "New"
        val lastName = "User"
        val phoneNumber = "9876543210"
        
        fakeUserRepository.nextRegisterResult = Result.success(DomainRegistrationResult("456", "newuser"))

        // When
        val result = useCase(username, password, email, firstName, lastName, phoneNumber)

        // Then
        assertTrue(result.isSuccess)
        val capturedRegistration = fakeUserRepository.lastRegistrationData
        assertEquals(username, capturedRegistration?.username)
        assertEquals(password, capturedRegistration?.password)
        assertEquals(email, capturedRegistration?.email)
        assertEquals(firstName, capturedRegistration?.firstName)
        assertEquals(lastName, capturedRegistration?.lastName)
        assertEquals(phoneNumber, capturedRegistration?.phoneNumber)
    }

    @Test
    fun `should handle empty or blank parameters correctly`() = runTest {
        // Given
        val username = ""
        val password = "   "
        val email = ""
        val firstName = ""
        val lastName = ""
        val phoneNumber = ""
        
        fakeUserRepository.nextRegisterResult = Result.success(DomainRegistrationResult("789", ""))

        // When
        val result = useCase(username, password, email, firstName, lastName, phoneNumber)

        // Then
        assertTrue(result.isSuccess)
        val capturedRegistration = fakeUserRepository.lastRegistrationData
        assertEquals(username, capturedRegistration?.username)
        assertEquals(password, capturedRegistration?.password)
        assertEquals(email, capturedRegistration?.email)
        assertEquals(firstName, capturedRegistration?.firstName)
        assertEquals(lastName, capturedRegistration?.lastName)
        assertEquals(phoneNumber, capturedRegistration?.phoneNumber)
    }

    @Test
    fun `should use response username as success message when available`() = runTest {
        // Given
        val responseUsername = "successful_user"
        fakeUserRepository.nextRegisterResult = Result.success(
            DomainRegistrationResult("999", responseUsername)
        )

        // When
        val result = useCase("user", "pass", "email", "first", "last", "phone")

        // Then
        assertTrue(result.isSuccess)
        val registerResult = result.getOrNull()
        assertTrue(registerResult is RegisterResult.Success)
        assertEquals(responseUsername, (registerResult as RegisterResult.Success).message)
    }
}
