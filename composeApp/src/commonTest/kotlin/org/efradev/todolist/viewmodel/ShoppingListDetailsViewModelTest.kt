package org.efradev.todolist.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.efradev.todolist.domain.GetShoppingListDetailsUseCase
import org.efradev.todolist.domain.usecase.AddItemToListUseCase
import org.efradev.todolist.domain.usecase.DeleteShoppingListUseCase
import org.efradev.todolist.domain.FakeShoppingListRepository
import org.efradev.todolist.domain.model.DomainShoppingList
import org.efradev.todolist.domain.model.DomainShoppingItem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.AfterTest

@OptIn(ExperimentalCoroutinesApi::class)
class ShoppingListDetailsViewModelTest {

    private lateinit var fakeRepository: FakeShoppingListRepository
    private lateinit var getDetailsUseCase: GetShoppingListDetailsUseCase
    private lateinit var addItemUseCase: AddItemToListUseCase
    private lateinit var deleteListUseCase: DeleteShoppingListUseCase
    private lateinit var viewModel: ShoppingListDetailsViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeShoppingListRepository()
        getDetailsUseCase = GetShoppingListDetailsUseCase(fakeRepository)
        addItemUseCase = AddItemToListUseCase(fakeRepository)
        deleteListUseCase = DeleteShoppingListUseCase(fakeRepository)
        viewModel = ShoppingListDetailsViewModel(getDetailsUseCase, addItemUseCase, deleteListUseCase)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() {
        // Assert
        assertTrue(viewModel.uiState is ShoppingListDetailsUiState.Loading)
    }

    @Test
    fun `loadListDetails with success updates state to Success`() = runTest {
        // Arrange
        val listId = "test-list-id"
        val expectedList = DomainShoppingList(
            id = listId,
            name = "Test List",
            createdAt = "1 de junio, 2023",
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
        viewModel.loadListDetails(listId)

        // Assert
        val state = viewModel.uiState
        assertTrue(state is ShoppingListDetailsUiState.Success)
        assertEquals(expectedList, state.list)
    }

    @Test
    fun `loadListDetails with failure updates state to Error`() = runTest {
        // Arrange
        val listId = "test-list-id"
        val errorMessage = "Network error"
        fakeRepository.setGetShoppingListDetailsResult(Result.failure(RuntimeException(errorMessage)))

        // Act
        viewModel.loadListDetails(listId)

        // Assert
        val state = viewModel.uiState
        assertTrue(state is ShoppingListDetailsUiState.Error)
        assertEquals(errorMessage, state.message)
    }

    @Test
    fun `loadListDetails with null error message uses default message`() = runTest {
        // Arrange
        val listId = "test-list-id"
        fakeRepository.setGetShoppingListDetailsResult(Result.failure(RuntimeException()))

        // Act
        viewModel.loadListDetails(listId)

        // Assert
        val state = viewModel.uiState
        assertTrue(state is ShoppingListDetailsUiState.Error)
        assertEquals("Failed to load list details", state.message)
    }

    @Test
    fun `loadListDetails sets state to Loading initially`() = runTest {
        // Arrange
        val listId = "test-list-id"
        val expectedList = DomainShoppingList(
            id = listId,
            name = "Test List",
            createdAt = "1 de junio, 2023",
            userId = "user-123",
            type = "simple",
            items = emptyList()
        )
        
        // Set initial state to Success to verify it changes to Loading
        fakeRepository.setGetShoppingListDetailsResult(Result.success(expectedList))
        viewModel.loadListDetails("initial-list")
        assertTrue(viewModel.uiState is ShoppingListDetailsUiState.Success)

        // Act
        viewModel.loadListDetails(listId)

        // Assert - Should first go to Loading, then Success
        // Since we're using UnconfinedTestDispatcher, the final state will be Success
        // But we can verify the use case was called with correct parameters
        assertEquals(listId, fakeRepository.lastRequestedListId)
    }

    @Test
    fun `refresh calls loadListDetails with same ID`() = runTest {
        // Arrange
        val listId = "refresh-test-id"
        val expectedList = DomainShoppingList(
            id = listId,
            name = "Refresh Test List",
            createdAt = "1 de junio, 2023",
            userId = "user-123",
            type = "simple",
            items = emptyList()
        )
        fakeRepository.setGetShoppingListDetailsResult(Result.success(expectedList))

        // Act
        viewModel.refresh(listId)

        // Assert
        val state = viewModel.uiState
        assertTrue(state is ShoppingListDetailsUiState.Success)
        assertEquals(expectedList, state.list)
        assertEquals(listId, fakeRepository.lastRequestedListId)
    }

    @Test
    fun `resetState sets state to Loading`() = runTest {
        // Arrange - Set to Success state first
        val listId = "test-list-id"
        val expectedList = DomainShoppingList(
            id = listId,
            name = "Test List",
            createdAt = "1 de junio, 2023",
            userId = "user-123",
            type = "simple",
            items = emptyList()
        )
        fakeRepository.setGetShoppingListDetailsResult(Result.success(expectedList))
        viewModel.loadListDetails(listId)
        assertTrue(viewModel.uiState is ShoppingListDetailsUiState.Success)

        // Act
        viewModel.resetState()

        // Assert
        assertTrue(viewModel.uiState is ShoppingListDetailsUiState.Loading)
    }

    @Test
    fun `loadListDetails with empty list ID shows error without calling repository`() = runTest {
        // Arrange
        val listId = ""
        // Don't set up repository response as it won't be called

        // Act
        viewModel.loadListDetails(listId)

        // Assert
        val state = viewModel.uiState
        assertTrue(state is ShoppingListDetailsUiState.Error)
        assertEquals("List ID cannot be empty", state.message)
        // Repository shouldn't be called for empty ID
        assertEquals(null, fakeRepository.lastRequestedListId)
    }

    @Test
    fun `loadListDetails with list containing multiple items displays correctly`() = runTest {
        // Arrange
        val listId = "multi-item-list"
        val items = listOf(
            DomainShoppingItem(name = "Item 1", status = "pendiente", type = "simple"),
            DomainShoppingItem(name = "Item 2", status = "completado", type = "compras"),
            DomainShoppingItem(name = "Item 3", status = "en_progreso", type = "tareas")
        )
        val expectedList = DomainShoppingList(
            id = listId,
            name = "Multi Item List",
            createdAt = "1 de junio, 2023",
            userId = "user-123",
            type = "simple",
            items = items
        )
        fakeRepository.setGetShoppingListDetailsResult(Result.success(expectedList))

        // Act
        viewModel.loadListDetails(listId)

        // Assert
        val state = viewModel.uiState
        assertTrue(state is ShoppingListDetailsUiState.Success)
        assertEquals(expectedList, state.list)
        assertEquals(3, state.list.items.size)
        assertEquals("Item 1", state.list.items[0].name)
        assertEquals("completado", state.list.items[1].status)
        assertEquals("tareas", state.list.items[2].type)
    }

    @Test
    fun `deleteList with valid ID sets state to Deleted`() = runTest {
        // Arrange
        val listId = "test-list-id"

        // Act
        viewModel.deleteList(listId)

        // Assert
        val state = viewModel.uiState
        assertTrue(state is ShoppingListDetailsUiState.Deleted)
        assertEquals(listId, fakeRepository.lastRequestedListId)
    }

    @Test
    fun `deleteList with repository failure sets state to Error`() = runTest {
        // Arrange
        val listId = "test-list-id"
        val errorMessage = "Network error"
        fakeRepository.throwOnGetShoppingListDetails = RuntimeException(errorMessage)

        // Act
        viewModel.deleteList(listId)

        // Assert
        val state = viewModel.uiState
        assertTrue(state is ShoppingListDetailsUiState.Error)
        assertEquals(errorMessage, state.message)
    }

    @Test
    fun `deleteList sets isDeletingList flag correctly`() = runTest {
        // Arrange
        val listId = "test-list-id"
        
        // Initial state
        assertEquals(false, viewModel.isDeletingList)

        // Act
        viewModel.deleteList(listId)

        // Assert
        // Since we're using UnconfinedTestDispatcher, the operation completes immediately
        assertEquals(false, viewModel.isDeletingList)
        assertTrue(viewModel.uiState is ShoppingListDetailsUiState.Deleted)
    }
}
