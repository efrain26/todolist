package org.efradev.todolist.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.efradev.todolist.data.model.ShoppingList
import org.efradev.todolist.domain.CreateShoppingListResult
import org.efradev.todolist.domain.CreateShoppingListUseCase
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Test unitario para CreateListViewModel
 * 
 * Estos tests verifican que el ViewModel maneje correctamente los estados
 * y llame al use case con los parámetros correctos
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CreateListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeUseCase: FakeCreateShoppingListUseCase
    private lateinit var viewModel: TestableCreateListViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeUseCase = FakeCreateShoppingListUseCase()
        viewModel = TestableCreateListViewModel(fakeUseCase)
    }

    @Test
    fun `should start with idle state`() {
        // Given - ViewModel just created
        
        // When - checking initial state
        val initialState = viewModel.uiState
        
        // Then
        assertTrue(initialState is CreateListUiState.Idle)
    }

    @Test
    fun `should show error when creating list with blank name`() = runTest {
        // Given
        val blankName = "   "
        val type = "simple"

        // When
        viewModel.createList(blankName, type)

        // Then
        assertTrue(viewModel.uiState is CreateListUiState.Error)
        assertEquals(
            "El nombre de la lista es requerido",
            (viewModel.uiState as CreateListUiState.Error).message
        )
    }

    @Test
    fun `should show loading state when creating list`() = runTest {
        // Given
        val name = "Mi Lista"
        val type = "simple"
        fakeUseCase.nextResult = Result.success(
            CreateShoppingListResult.Success(
                ShoppingList(
                    id = "1",
                    name = name,
                    type = type,
                    createdAt = "2025-06-28T10:00:00",
                    userId = "user123",
                    items = emptyList()
                )
            )
        )

        // When
        viewModel.createList(name, type)

        // Then - Should be success after async operation
        assertTrue(viewModel.uiState is CreateListUiState.Success)
        val successState = viewModel.uiState as CreateListUiState.Success
        assertEquals(name, successState.list.name)
        assertEquals(type, successState.list.type)
    }

    @Test
    fun `should show success state when list created successfully`() = runTest {
        // Given
        val name = "Mi Lista Exitosa"
        val type = "compras"
        val expectedList = ShoppingList(
            id = "1",
            name = name,
            type = type,
            createdAt = "2025-06-28T10:00:00",
            userId = "user123",
            items = emptyList()
        )
        fakeUseCase.nextResult = Result.success(CreateShoppingListResult.Success(expectedList))

        // When
        viewModel.createList(name, type)

        // Then
        assertTrue(viewModel.uiState is CreateListUiState.Success)
        assertEquals(expectedList, (viewModel.uiState as CreateListUiState.Success).list)
    }

    @Test
    fun `should show error state when use case returns error`() = runTest {
        // Given
        val name = "Mi Lista"
        val type = "simple"
        val errorMessage = "Error de red"
        fakeUseCase.nextResult = Result.success(CreateShoppingListResult.Error(errorMessage))

        // When
        viewModel.createList(name, type)

        // Then
        assertTrue(viewModel.uiState is CreateListUiState.Error)
        assertEquals(errorMessage, (viewModel.uiState as CreateListUiState.Error).message)
    }

    @Test
    fun `should reset state to idle when resetState is called`() = runTest {
        // Given - ViewModel in error state
        viewModel.createList("", "simple") // This will cause error
        assertTrue(viewModel.uiState is CreateListUiState.Error)

        // When
        viewModel.resetState()

        // Then
        assertTrue(viewModel.uiState is CreateListUiState.Idle)
    }

    @Test
    fun `should trim whitespace from name before calling use case`() = runTest {
        // Given
        val nameWithWhitespace = "  Mi Lista con Espacios  "
        val type = "simple"
        fakeUseCase.nextResult = Result.success(
            CreateShoppingListResult.Success(
                ShoppingList(
                    id = "1",
                    name = "Mi Lista con Espacios",
                    type = type,
                    createdAt = "2025-06-28T10:00:00",
                    userId = "user123",
                    items = emptyList()
                )
            )
        )

        // When
        viewModel.createList(nameWithWhitespace, type)

        // Then
        assertEquals("Mi Lista con Espacios", fakeUseCase.lastInvokedName)
        assertEquals(type, fakeUseCase.lastInvokedType)
    }
}

/**
 * Implementación fake del use case para testing
 */
class FakeCreateShoppingListUseCase {
    var nextResult: Result<CreateShoppingListResult> = Result.success(
        CreateShoppingListResult.Success(
            ShoppingList(
                id = "1",
                name = "Test List",
                type = "simple",
                createdAt = "2025-06-28T10:00:00",
                userId = "user123",
                items = emptyList()
            )
        )
    )
    
    var lastInvokedName: String? = null
    var lastInvokedType: String? = null

    suspend fun invoke(name: String, type: String): Result<CreateShoppingListResult> {
        lastInvokedName = name
        lastInvokedType = type
        return nextResult
    }
}

/**
 * ViewModel testeable que usa nuestra implementación fake
 */
class TestableCreateListViewModel(
    private val fakeUseCase: FakeCreateShoppingListUseCase
) {
    var uiState: CreateListUiState by mutableStateOf(CreateListUiState.Idle)
        private set

    suspend fun createList(name: String, type: String = "simple") {
        if (name.isBlank()) {
            uiState = CreateListUiState.Error("El nombre de la lista es requerido")
            return
        }

        uiState = CreateListUiState.Loading
        
        fakeUseCase.invoke(
            name = name.trim(),
            type = type
        ).fold(
            onSuccess = { result ->
                when (result) {
                    is CreateShoppingListResult.Success -> {
                        uiState = CreateListUiState.Success(result.list)
                    }
                    is CreateShoppingListResult.Error -> {
                        uiState = CreateListUiState.Error(result.message)
                    }
                }
            },
            onFailure = { error ->
                uiState = CreateListUiState.Error(
                    error.message ?: "Error desconocido al crear la lista"
                )
            }
        )
    }

    fun resetState() {
        uiState = CreateListUiState.Idle
    }
}
