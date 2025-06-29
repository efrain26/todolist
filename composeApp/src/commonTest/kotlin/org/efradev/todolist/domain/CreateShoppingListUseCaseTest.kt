package org.efradev.todolist.domain

import kotlinx.coroutines.test.runTest
import org.efradev.todolist.domain.repository.ShoppingListRepository
import org.efradev.todolist.domain.model.DomainShoppingList
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Test unitario para CreateShoppingListUseCase siguiendo principios de TDD
 * 
 * Este test demuestra cómo estructurar tests para el use case de creación de listas
 * siguiendo el patrón Given-When-Then
 */
class CreateShoppingListUseCaseTest {

    // Mock del repositorio (usaremos una implementación fake para el ejemplo)
    private val fakeRepository = FakeShoppingListRepositoryForTests()
    private val useCase = CreateShoppingListUseCase(fakeRepository)

    @Test
    fun `should create shopping list successfully when given valid name and type`() = runTest {
        // Given
        val name = "Mi Lista de Compras"
        val type = "compras"
        val expectedList = DomainShoppingList(
            id = "1",
            name = name,
            type = type,
            createdAt = "2025-06-28T10:00:00",
            userId = "user123",
            items = emptyList()
        )
        fakeRepository.nextCreateListResult = Result.success(expectedList)

        // When
        val result = useCase(name, type)

        // Then
        assertTrue(result.isSuccess)
        val actualResult = result.getOrNull()
        assertTrue(actualResult is CreateShoppingListResult.Success)
        assertEquals(expectedList, (actualResult as CreateShoppingListResult.Success).list)
    }

    @Test
    fun `should return error when given blank name`() = runTest {
        // Given
        val blankName = "   "
        val type = "simple"

        // When
        val result = useCase(blankName, type)

        // Then
        assertTrue(result.isSuccess)
        val actualResult = result.getOrNull()
        assertTrue(actualResult is CreateShoppingListResult.Error)
        assertEquals(
            "El nombre de la lista es requerido", 
            (actualResult as CreateShoppingListResult.Error).message
        )
    }

    @Test
    fun `should return error when repository fails`() = runTest {
        // Given
        val name = "Mi Lista"
        val type = "simple"
        fakeRepository.nextCreateListResult = Result.failure(Exception("Network error"))

        // When
        val result = useCase(name, type)

        // Then
        assertTrue(result.isSuccess)
        val actualResult = result.getOrNull()
        assertTrue(actualResult is CreateShoppingListResult.Error)
        assertTrue((actualResult as CreateShoppingListResult.Error).message.contains("Network error"))
    }

    @Test
    fun `should trim whitespace from name before creating list`() = runTest {
        // Given
        val nameWithWhitespace = "  Mi Lista con Espacios  "
        val type = "simple"
        val expectedList = DomainShoppingList(
            id = "1",
            name = "Mi Lista con Espacios", // Sin espacios al inicio y final
            type = type,
            createdAt = "2025-06-28T10:00:00",
            userId = "user123",
            items = emptyList()
        )
        fakeRepository.nextCreateListResult = Result.success(expectedList)

        // When
        val result = useCase(nameWithWhitespace, type)

        // Then
        assertTrue(result.isSuccess)
        val actualResult = result.getOrNull()
        assertTrue(actualResult is CreateShoppingListResult.Success)
        assertEquals("Mi Lista con Espacios", fakeRepository.lastCreateParams?.name)
    }

    @Test
    fun `should return error when name is empty string`() = runTest {
        // Given
        val emptyName = ""
        val type = "simple"

        // When
        val result = useCase(emptyName, type)

        // Then
        assertTrue(result.isSuccess)
        val actualResult = result.getOrNull()
        assertTrue(actualResult is CreateShoppingListResult.Error)
        assertEquals(
            "El nombre de la lista es requerido", 
            (actualResult as CreateShoppingListResult.Error).message
        )
    }

    @Test
    fun `should use default type when type is not provided`() = runTest {
        // Given
        val name = "Mi Lista"
        val expectedList = DomainShoppingList(
            id = "1",
            name = name,
            type = "simple", // Tipo por defecto
            createdAt = "2025-06-28T10:00:00",
            userId = "user123",
            items = emptyList()
        )
        fakeRepository.nextCreateListResult = Result.success(expectedList)

        // When
        val result = useCase(name) // Sin especificar tipo

        // Then
        assertTrue(result.isSuccess)
        val actualResult = result.getOrNull()
        assertTrue(actualResult is CreateShoppingListResult.Success)
        assertEquals("simple", fakeRepository.lastCreateParams?.type)
    }

    @Test
    fun `should handle unexpected exception during execution`() = runTest {
        // Given
        val name = "Mi Lista"
        val type = "simple"
        fakeRepository.shouldThrowException = true

        // When
        val result = useCase(name, type)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is RuntimeException)
    }

    @Test
    fun `should return error when repository returns failure with null message`() = runTest {
        // Given
        val name = "Mi Lista"
        val type = "simple"
        fakeRepository.nextCreateListResult = Result.failure(Exception())

        // When
        val result = useCase(name, type)

        // Then
        assertTrue(result.isSuccess)
        val actualResult = result.getOrNull()
        assertTrue(actualResult is CreateShoppingListResult.Error)
        assertEquals("Error al crear la lista", (actualResult as CreateShoppingListResult.Error).message)
    }
}


