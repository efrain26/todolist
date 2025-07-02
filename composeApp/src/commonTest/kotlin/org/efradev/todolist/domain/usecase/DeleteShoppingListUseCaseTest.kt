package org.efradev.todolist.domain.usecase

import kotlinx.coroutines.test.runTest
import org.efradev.todolist.domain.repository.ShoppingListRepository
import org.efradev.todolist.domain.model.DomainShoppingList
import org.efradev.todolist.domain.model.DomainAddItemRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Tests for DeleteShoppingListUseCase
 * Following the established testing patterns in the project
 */
class DeleteShoppingListUseCaseTest {
    
    private val fakeShoppingListRepository = FakeShoppingListRepositoryForDelete()
    private val useCase = DeleteShoppingListUseCase(fakeShoppingListRepository)

    @Test
    fun `should delete list successfully when valid list ID provided`() = runTest {
        // Given
        val listId = "list123"

        // When
        val result = useCase(listId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(listId, fakeShoppingListRepository.lastDeletedListId)
    }

    @Test
    fun `should return failure when list ID is empty`() = runTest {
        // Given
        val listId = ""

        // When
        val result = useCase(listId)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("List ID cannot be empty") == true)
    }

    @Test
    fun `should return failure when list ID is blank`() = runTest {
        // Given
        val listId = "   "

        // When
        val result = useCase(listId)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("List ID cannot be empty") == true)
    }

    @Test
    fun `should propagate repository exception when delete fails`() = runTest {
        // Given
        val listId = "list123"
        fakeShoppingListRepository.shouldThrowException = true

        // When
        val result = useCase(listId)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is RuntimeException)
    }
}

/**
 * Fake repository for testing DeleteShoppingListUseCase
 */
class FakeShoppingListRepositoryForDelete : ShoppingListRepository {
    var lastDeletedListId: String? = null
    var shouldThrowException = false

    override suspend fun getShoppingLists(): Result<List<DomainShoppingList>> = 
        Result.success(emptyList())

    override suspend fun createList(name: String, type: String): Result<DomainShoppingList> = 
        Result.success(
            DomainShoppingList(
                id = "test",
                name = name,
                createdAt = "2025-06-30T12:00:00",
                userId = "user123",
                type = type,
                items = emptyList()
            )
        )

    override suspend fun getShoppingListDetails(listId: String): Result<DomainShoppingList> = 
        Result.success(
            DomainShoppingList(
                id = listId,
                name = "Test List",
                createdAt = "2025-06-30T12:00:00",
                userId = "user123",
                type = "simple",
                items = emptyList()
            )
        )

    override suspend fun addItemToList(listId: String, item: DomainAddItemRequest): Result<DomainShoppingList> {
        return Result.success(
            DomainShoppingList(
                id = listId,
                name = "Test List",
                createdAt = "2025-06-30T12:00:00",
                userId = "user123",
                type = "simple",
                items = emptyList()
            )
        )
    }

    override suspend fun deleteList(listId: String): Result<Unit> {
        if (shouldThrowException) {
            throw RuntimeException("Test exception")
        }
        
        lastDeletedListId = listId
        return Result.success(Unit)
    }
}
