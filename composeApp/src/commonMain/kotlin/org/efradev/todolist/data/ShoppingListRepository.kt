package org.efradev.todolist.data

import org.efradev.todolist.data.model.ShoppingList
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header

interface ShoppingListRepository {
    suspend fun getShoppingLists(): Result<List<ShoppingList>>
}
const val BASE_URL = "https://platform-production-dbfb.up.railway.app"
class ShoppingListRepositoryImpl(
    private val client: HttpClient,
    private val authLocalStorage: AuthLocalStorage
) : ShoppingListRepository {

    override suspend fun getShoppingLists(): Result<List<ShoppingList>> {
        return try {
            val token = authLocalStorage.getAccessToken()
            val response = client.get("$BASE_URL/api/v1/shopping/shopping-lists") {
                header("Authorization", "Bearer $token")
            }
            Result.success(response.body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
