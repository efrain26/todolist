package org.efradev.todolist.domain

import org.efradev.todolist.data.ShoppingListRepository
import org.efradev.todolist.data.UserRepository
import org.efradev.todolist.data.local.PreferencesRepository
import org.efradev.todolist.data.model.AuthResponse
import org.efradev.todolist.data.model.LoginRequest
import org.efradev.todolist.data.model.RegisterRequest
import org.efradev.todolist.data.model.RegisterResponse
import org.efradev.todolist.data.model.ShoppingList
import org.efradev.todolist.data.model.Token
import org.efradev.todolist.data.model.User
import org.efradev.todolist.data.UserCheckResult

/**
 * Clases fake comunes para todos los tests de domain
 * Evita duplicación de código entre archivos de test
 */

/**
 * Implementación fake del ShoppingListRepository para testing
 */
class FakeShoppingListRepositoryForTests : ShoppingListRepository {
    var nextGetListsResult: Result<List<ShoppingList>> = Result.success(emptyList())
    var nextCreateListResult: Result<ShoppingList> = Result.success(
        ShoppingList(
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

    override suspend fun getShoppingLists(): Result<List<ShoppingList>> {
        if (shouldThrowException) throw RuntimeException("Test exception")
        return nextGetListsResult
    }

    override suspend fun createList(name: String, type: String): Result<ShoppingList> {
        if (shouldThrowException) throw RuntimeException("Test exception")
        lastCreateParams = CreateParams(name, type)
        return nextCreateListResult
    }
}

/**
 * Implementación fake del UserRepository para testing
 */
class FakeUserRepositoryForTests : UserRepository {
    var nextCheckUserResult: Result<UserCheckResult> = Result.success(UserCheckResult.NotRegistered)
    var nextRegisterResult: Result<RegisterResponse> = Result.success(RegisterResponse(1, "testuser"))
    var nextLoginResult: Result<AuthResponse> = Result.success(
        AuthResponse(
            user = User("1", "user", "test@example.com", "Test", "User", "123"),
            token = Token("token", "refresh", "Bearer")
        )
    )
    
    var lastEmailChecked: String? = null
    var lastRegisterRequest: RegisterRequest? = null
    var lastLoginRequest: LoginRequest? = null

    override suspend fun checkUser(email: String): Result<UserCheckResult> {
        lastEmailChecked = email
        return nextCheckUserResult
    }

    override suspend fun registerUser(request: RegisterRequest): Result<RegisterResponse> {
        lastRegisterRequest = request
        return nextRegisterResult
    }

    override suspend fun login(request: LoginRequest): Result<AuthResponse> {
        lastLoginRequest = request
        return nextLoginResult
    }
}

/**
 * Implementación fake del PreferencesRepository para testing
 */
class FakePreferencesRepositoryForTests : PreferencesRepository {
    var isLoggedIn: Boolean = false
    var authData: AuthResponse? = null
    var shouldThrowException: Boolean = false
    var saveAuthDataWasCalled: Boolean = false
    var clearAuthDataWasCalled: Boolean = false

    override suspend fun saveAuthData(authResponse: AuthResponse) {
        saveAuthDataWasCalled = true
        if (shouldThrowException) throw RuntimeException("Test exception")
        this.authData = authResponse
        this.isLoggedIn = true
    }

    override suspend fun getAuthData(): AuthResponse? {
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

    fun getString(key: String): String {
        return strings[key] ?: "Missing string: $key"
    }
}
