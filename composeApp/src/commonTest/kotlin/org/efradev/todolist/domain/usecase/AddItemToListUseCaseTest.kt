package org.efradev.todolist.domain.usecase

import kotlinx.coroutines.test.runTest
import org.efradev.todolist.domain.model.DomainShoppingList
import org.efradev.todolist.domain.model.DomainShoppingItem
import org.efradev.todolist.domain.model.DomainAddItemRequest
import org.efradev.todolist.domain.repository.ShoppingListRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Tests for AddItemToListUseCase
 * Following the established testing patterns in the project
 */
class AddItemToListUseCaseTest {
    
    private val fakeShoppingListRepository = FakeShoppingListRepositoryForAddItem()
    private val useCase = AddItemToListUseCase(fakeShoppingListRepository)

    @Test
    fun `should add item successfully when valid parameters provided`() = runTest {
        // Given
        val listId = "list123"
        val itemName = "Test Item"
        val listType = "simple"
        val expectedList = DomainShoppingList(
            id = listId,
            name = "Test List",
            createdAt = "2025-06-30T12:00:00",
            userId = "user123",
            type = listType,
            items = listOf(
                DomainShoppingItem(
                    name = itemName,
                    status = "pendiente",
                    type = listType
                )
            )
        )
        fakeShoppingListRepository.nextAddItemResult = Result.success(expectedList)

        // When
        val result = useCase(listId, itemName, listType)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedList, result.getOrNull())
        assertEquals(listId, fakeShoppingListRepository.lastAddItemListId)
        assertEquals(itemName, fakeShoppingListRepository.lastAddItemRequest?.name)
        assertEquals(listType, fakeShoppingListRepository.lastAddItemRequest?.listType)
    }

    @Test
    fun `should return failure when list id is blank`() = runTest {
        // Given
        val listId = ""
        val itemName = "Test Item"

        // When
        val result = useCase(listId, itemName)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("List ID cannot be empty") == true)
    }

    @Test
    fun `should return failure when item name is blank`() = runTest {
        // Given
        val listId = "list123"
        val itemName = ""

        // When
        val result = useCase(listId, itemName)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("Item name cannot be empty") == true)
    }

    @Test
    fun `should return failure when item name is only whitespace`() = runTest {
        // Given
        val listId = "list123"
        val itemName = "   "

        // When
        val result = useCase(listId, itemName)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("Item name cannot be empty") == true)
    }

    @Test
    fun `should use default list type when not specified`() = runTest {
        // Given
        val listId = "list123"
        val itemName = "Test Item"
        val expectedList = DomainShoppingList(
            id = listId,
            name = "Test List",
            createdAt = "2025-06-30T12:00:00",
            userId = "user123", 
            type = "simple",
            items = emptyList()
        )
        fakeShoppingListRepository.nextAddItemResult = Result.success(expectedList)

        // When
        val result = useCase(listId, itemName) // No listType specified

        // Then
        assertTrue(result.isSuccess)
        assertEquals("simple", fakeShoppingListRepository.lastAddItemRequest?.listType)
    }

    @Test
    fun `should include additional fields for shopping list items`() = runTest {
        // Given
        val listId = "list123"
        val itemName = "Milk"
        val listType = "compras"
        val price = 2.99
        val quantity = 2
        val store = "SuperMarket"
        val expectedList = DomainShoppingList(
            id = listId,
            name = "Shopping List",
            createdAt = "2025-06-30T12:00:00",
            userId = "user123",
            type = listType,
            items = emptyList()
        )
        fakeShoppingListRepository.nextAddItemResult = Result.success(expectedList)

        // When
        val result = useCase(
            listId = listId,
            itemName = itemName,
            listType = listType,
            price = price,
            quantity = quantity,
            store = store
        )

        // Then
        assertTrue(result.isSuccess)
        val request = fakeShoppingListRepository.lastAddItemRequest
        assertEquals(itemName, request?.name)
        assertEquals(listType, request?.listType)
        assertEquals(price, request?.price)
        assertEquals(quantity, request?.quantity)
        assertEquals(store, request?.store)
    }

    @Test
    fun `should propagate repository failure when repository throws exception`() = runTest {
        // Given
        val listId = "list123"
        val itemName = "Test Item"
        val exception = RuntimeException("Network error")
        fakeShoppingListRepository.nextAddItemResult = Result.failure(exception)

        // When
        val result = useCase(listId, itemName)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `should handle exception during execution`() = runTest {
        // Given
        val listId = "list123"
        val itemName = "Test Item"
        fakeShoppingListRepository.shouldThrowException = true

        // When
        val result = useCase(listId, itemName)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is RuntimeException)
    }
}

/**
 * Fake repository for testing AddItemToListUseCase
 */
class FakeShoppingListRepositoryForAddItem : ShoppingListRepository {
    var nextAddItemResult: Result<DomainShoppingList> = Result.success(
        DomainShoppingList(
            id = "default",
            name = "Default List",
            createdAt = "2025-06-30T12:00:00",
            userId = "user123",
            type = "simple",
            items = emptyList()
        )
    )
    var lastAddItemListId: String? = null
    var lastAddItemRequest: DomainAddItemRequest? = null
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
        if (shouldThrowException) {
            throw RuntimeException("Test exception")
        }
        
        lastAddItemListId = listId
        lastAddItemRequest = item
        return nextAddItemResult
    }
}
