package org.efradev.todolist.domain

import kotlinx.coroutines.test.runTest
import org.efradev.todolist.data.ShoppingListRepository
import org.efradev.todolist.data.model.ShoppingList
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Test unitario para GetShoppingListsUseCase siguiendo principios de TDD
 * 
 * Este test verifica el comportamiento del use case de obtención de listas de compras
 * siguiendo el patrón Given-When-Then
 */
class GetShoppingListsUseCaseTest {

    private val fakeRepository = FakeShoppingListRepositoryForTests()
    private val fakeStringRes = FakeStringResProviderForTests()
    private val useCase = GetShoppingListsUseCase(fakeRepository, fakeStringRes::getString)

    @Test
    fun `should return list of shopping lists when repository succeeds`() = runTest {
        // Given
        val expectedLists = listOf(
            ShoppingList(
                id = "1",
                name = "Lista de Compras",
                type = "compras",
                createdAt = "2025-06-29T10:00:00",
                userId = "user123",
                items = emptyList()
            ),
            ShoppingList(
                id = "2",
                name = "Lista Simple",
                type = "simple",
                createdAt = "2025-06-29T11:00:00",
                userId = "user123",
                items = emptyList()
            )
        )
        fakeRepository.nextGetListsResult = Result.success(expectedLists)

        // When
        val result = useCase()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedLists, result.getOrNull())
    }

    @Test
    fun `should return empty list when repository returns empty list`() = runTest {
        // Given
        val emptyList = emptyList<ShoppingList>()
        fakeRepository.nextGetListsResult = Result.success(emptyList)

        // When
        val result = useCase()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(emptyList, result.getOrNull())
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun `should return failure when repository fails`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        fakeRepository.nextGetListsResult = Result.failure(exception)

        // When
        val result = useCase()

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `should propagate exception when repository throws unexpected exception`() = runTest {
        // Given
        fakeRepository.shouldThrowException = true

        // When
        val result = useCase()

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is RuntimeException)
    }
}
