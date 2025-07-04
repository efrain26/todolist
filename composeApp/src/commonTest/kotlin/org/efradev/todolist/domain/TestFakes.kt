package org.efradev.todolist.domain

import org.efradev.todolist.domain.repository.ShoppingListRepository
import org.efradev.todolist.domain.repository.UserRepository
import org.efradev.todolist.domain.repository.PreferencesRepository
import org.efradev.todolist.domain.repository.UserCheckResult
import org.efradev.todolist.domain.model.DomainShoppingList
import org.efradev.todolist.domain.model.DomainShoppingItem
import org.efradev.todolist.domain.model.DomainAuthData
import org.efradev.todolist.domain.model.DomainUserRegistration
import org.efradev.todolist.domain.model.DomainRegistrationResult
import org.efradev.todolist.domain.model.DomainUser
import org.efradev.todolist.domain.model.DomainAddItemRequest

/**
 * Clases fake comunes para todos los tests de domain
 * Evita duplicación de código entre archivos de test
 */

/**
 * Implementación fake del ShoppingListRepository para testing
 */
class FakeShoppingListRepositoryForTests : ShoppingListRepository {
    var nextGetListsResult: Result<List<DomainShoppingList>> = Result.success(emptyList())
    var nextCreateListResult: Result<DomainShoppingList> = Result.success(
        DomainShoppingList(
            id = "1",
            name = "Test List",
            type = "simple",
            createdAt = "2025-06-29T10:00:00",
            userId = "user123",
            items = emptyList()
        )
    )
    var nextGetListDetailsResult: Result<DomainShoppingList> = Result.success(
        DomainShoppingList(
            id = "1",
            name = "Test List Details",
            type = "simple",
            createdAt = "2025-06-29T10:00:00",
            userId = "user123",
            items = emptyList()
        )
    )
    var nextAddItemResult: Result<DomainShoppingList> = Result.success(
        DomainShoppingList(
            id = "1",
            name = "Test List",
            type = "simple",
            createdAt = "2025-06-29T10:00:00",
            userId = "user123",
            items = emptyList()
        )
    )
    var shouldThrowException: Boolean = false
    
    data class CreateParams(val name: String, val type: String)
    var lastCreateParams: CreateParams? = null
    var lastRequestedListId: String? = null
    var lastAddItemListId: String? = null
    var lastAddItemRequest: DomainAddItemRequest? = null

    override suspend fun getShoppingLists(): Result<List<DomainShoppingList>> {
        if (shouldThrowException) throw RuntimeException("Test exception")
        return nextGetListsResult
    }

    override suspend fun createList(name: String, type: String): Result<DomainShoppingList> {
        if (shouldThrowException) throw RuntimeException("Test exception")
        lastCreateParams = CreateParams(name, type)
        return nextCreateListResult
    }

    override suspend fun getShoppingListDetails(listId: String): Result<DomainShoppingList> {
        if (shouldThrowException) throw RuntimeException("Test exception")
        lastRequestedListId = listId
        return nextGetListDetailsResult
    }

    override suspend fun addItemToList(listId: String, item: DomainAddItemRequest): Result<DomainShoppingList> {
        if (shouldThrowException) throw RuntimeException("Test exception")
        lastAddItemListId = listId
        lastAddItemRequest = item
        return nextAddItemResult
    }

    override suspend fun deleteList(listId: String): Result<Unit> {
        if (shouldThrowException) throw RuntimeException("Test exception")
        return Result.success(Unit)
    }
}

/**
 * Implementación fake del UserRepository para testing
 */
class FakeUserRepositoryForTests : UserRepository {
    var nextCheckUserResult: Result<UserCheckResult> = Result.success(UserCheckResult.NotRegistered)
    var nextRegisterResult: Result<DomainRegistrationResult> = Result.success(
        DomainRegistrationResult(id = "1", username = "testuser")
    )
    var nextLoginResult: Result<DomainAuthData> = Result.success(
        DomainAuthData(
            user = DomainUser(
                id = "1",
                username = "user",
                email = "test@example.com",
                firstName = "Test",
                lastName = "User",
                phoneNumber = "123"
            ),
            accessToken = "token",
            refreshToken = "refresh",
            tokenType = "Bearer"
        )
    )
    
    var lastEmailChecked: String? = null
    var lastRegistrationData: DomainUserRegistration? = null
    var lastLoginCredentials: Pair<String, String>? = null

    override suspend fun checkUser(email: String): Result<UserCheckResult> {
        lastEmailChecked = email
        return nextCheckUserResult
    }

    override suspend fun registerUser(userData: DomainUserRegistration): Result<DomainRegistrationResult> {
        lastRegistrationData = userData
        return nextRegisterResult
    }

    override suspend fun login(email: String, password: String): Result<DomainAuthData> {
        lastLoginCredentials = Pair(email, password)
        return nextLoginResult
    }
}

/**
 * Implementación fake del PreferencesRepository para testing
 */
class FakePreferencesRepositoryForTests : PreferencesRepository {
    var isLoggedIn: Boolean = false
    var authData: DomainAuthData? = null
    var shouldThrowException: Boolean = false
    var saveAuthDataWasCalled: Boolean = false
    var clearAuthDataWasCalled: Boolean = false

    override suspend fun saveAuthData(authData: DomainAuthData) {
        saveAuthDataWasCalled = true
        if (shouldThrowException) throw RuntimeException("Test exception")
        this.authData = authData
        this.isLoggedIn = true
    }

    override suspend fun getAuthData(): DomainAuthData? {
        if (shouldThrowException) throw RuntimeException("Test exception")
        return authData
    }

    override suspend fun clearAuthData() {
        clearAuthDataWasCalled = true
        if (shouldThrowException) throw RuntimeException("Test exception")
        this.authData = null
        this.isLoggedIn = false
    }

    override suspend fun isUserLoggedIn(): Boolean {
        if (shouldThrowException) throw RuntimeException("Test exception")
        return isLoggedIn
    }
}

/**
 * Implementación fake del StringResProvider para testing
 */
class FakeStringResProviderForTests {
    val strings = mutableMapOf<String, String>()
    
    init {
        // Valores por defecto para strings comunes
        strings["user_registered"] = "Usuario registrado"
        strings["user_not_registered"] = "Usuario no registrado"
        strings["unexpected_error"] = "Error inesperado"
        strings["register_success"] = "Registro exitoso"
        strings["logout_success"] = "Sesión cerrada"
        strings["logout_error"] = "Error al cerrar sesión"
    }

    fun getString(key: String): String {
        return strings[key] ?: "String not found: $key"
    }
}

/**
 * Implementación fake del ShoppingListRepository específica para GetShoppingListDetailsUseCase
 */
class FakeShoppingListRepository : ShoppingListRepository {
    var getShoppingListDetailsResult: Result<DomainShoppingList>? = null
    var throwOnGetShoppingListDetails: Exception? = null
    var lastRequestedListId: String? = null

    fun setGetShoppingListDetailsResult(result: Result<DomainShoppingList>) {
        getShoppingListDetailsResult = result
        throwOnGetShoppingListDetails = null
    }

    override suspend fun getShoppingLists(): Result<List<DomainShoppingList>> {
        return Result.success(emptyList())
    }

    override suspend fun createList(name: String, type: String): Result<DomainShoppingList> {
        return Result.success(DomainShoppingList("", "", "", "", "", emptyList()))
    }

    override suspend fun getShoppingListDetails(listId: String): Result<DomainShoppingList> {
        lastRequestedListId = listId
        
        throwOnGetShoppingListDetails?.let { throw it }
        
        return getShoppingListDetailsResult ?: Result.failure(
            RuntimeException("No result set for getShoppingListDetails")
        )
    }

    override suspend fun addItemToList(listId: String, item: DomainAddItemRequest): Result<DomainShoppingList> {
        lastRequestedListId = listId
        
        throwOnGetShoppingListDetails?.let { throw it }
        
        // Return a default successful result for addItemToList
        return Result.success(
            DomainShoppingList(
                id = listId,
                name = "Test List",
                createdAt = "2023-06-01T10:00:00Z",
                userId = "user-123",
                type = "simple",
                items = listOf(
                    DomainShoppingItem(
                        name = item.name,
                        status = "pendiente",
                        type = item.listType
                    )
                )
            )
        )
    }

    override suspend fun deleteList(listId: String): Result<Unit> {
        lastRequestedListId = listId
        
        throwOnGetShoppingListDetails?.let { throw it }
        
        return Result.success(Unit)
    }
}
