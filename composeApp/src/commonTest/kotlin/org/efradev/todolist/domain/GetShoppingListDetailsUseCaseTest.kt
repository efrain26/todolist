package org.efradev.todolist.domain

import kotlinx.coroutines.test.runTest
import org.efradev.todolist.domain.model.DomainShoppingList
import org.efradev.todolist.domain.model.DomainShoppingItem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetShoppingListDetailsUseCaseTest {

    private val fakeRepository = FakeShoppingListRepository()
    private val useCase = GetShoppingListDetailsUseCase(fakeRepository)

    @Test
    fun `invoke with valid list ID returns success`() = runTest {
        // Arrange
        val listId = "test-list-id"
        val expectedList = DomainShoppingList(
            id = listId,
            name = "Test List",
            createdAt = "2023-06-01T10:00:00Z",
            userId = "user-123",
            type = "simple",
            items = listOf(
                DomainShoppingItem(
                    name = "Test Item",
                    status = "pendiente",
                    type = "simple"
                )
            )
        )
        fakeRepository.setGetShoppingListDetailsResult(Result.success(expectedList))

        // Act
        val result = useCase(listId)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(expectedList, result.getOrNull())
        assertEquals(listId, fakeRepository.lastRequestedListId)
    }

    @Test
    fun `invoke with empty list ID returns failure`() = runTest {
        // Arrange
        val listId = ""

        // Act
        val result = useCase(listId)

        // Assert
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is IllegalArgumentException)
        assertEquals("List ID cannot be empty", exception.message)
    }

    @Test
    fun `invoke with blank list ID returns failure`() = runTest {
        // Arrange
        val listId = "   "

        // Act
        val result = useCase(listId)

        // Assert
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is IllegalArgumentException)
        assertEquals("List ID cannot be empty", exception.message)
    }

    @Test
    fun `invoke when repository returns failure propagates error`() = runTest {
        // Arrange
        val listId = "test-list-id"
        val expectedError = RuntimeException("Network error")
        fakeRepository.setGetShoppingListDetailsResult(Result.failure(expectedError))

        // Act
        val result = useCase(listId)

        // Assert
        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
        assertEquals(listId, fakeRepository.lastRequestedListId)
    }

    @Test
    fun `invoke with valid list ID calls repository with correct parameters`() = runTest {
        // Arrange
        val listId = "specific-list-123"
        val expectedList = DomainShoppingList(
            id = listId,
            name = "Specific List",
            createdAt = "2023-06-01T10:00:00Z",
            userId = "user-123",
            type = "compras",
            items = emptyList()
        )
        fakeRepository.setGetShoppingListDetailsResult(Result.success(expectedList))

        // Act
        useCase(listId)

        // Assert
        assertEquals(listId, fakeRepository.lastRequestedListId)
    }

    @Test
    fun `invoke when repository throws exception returns failure`() = runTest {
        // Arrange
        val listId = "test-list-id"
        val expectedException = RuntimeException("Unexpected error")
        fakeRepository.throwOnGetShoppingListDetails = expectedException

        // Act
        val result = useCase(listId)

        // Assert
        assertTrue(result.isFailure)
        assertEquals(expectedException, result.exceptionOrNull())
    }

    @Test
    fun `invoke with list containing multiple items returns complete list`() = runTest {
        // Arrange
        val listId = "multi-item-list"
        val items = listOf(
            DomainShoppingItem(name = "Item 1", status = "pendiente", type = "simple"),
            DomainShoppingItem(name = "Item 2", status = "completado", type = "simple"),
            DomainShoppingItem(name = "Item 3", status = "en_progreso", type = "simple")
        )
        val expectedList = DomainShoppingList(
            id = listId,
            name = "Multi Item List",
            createdAt = "2023-06-01T10:00:00Z",
            userId = "user-123",
            type = "simple",
            items = items
        )
        fakeRepository.setGetShoppingListDetailsResult(Result.success(expectedList))

        // Act
        val result = useCase(listId)

        // Assert
        assertTrue(result.isSuccess)
        val actualList = result.getOrNull()!!
        assertEquals(3, actualList.items.size)
        assertEquals("Item 1", actualList.items[0].name)
        assertEquals("Item 2", actualList.items[1].name)
        assertEquals("Item 3", actualList.items[2].name)
    }
}
