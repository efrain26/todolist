package org.efradev.todolist.data

import org.efradev.todolist.data.model.ShoppingList
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.efradev.todolist.data.local.AuthLocalStorage
import org.efradev.todolist.data.model.CreateShoppingListRequest
import org.efradev.todolist.data.mapper.toDomain
import org.efradev.todolist.data.mapper.createShoppingListRequest
import org.efradev.todolist.data.mapper.toData
import org.efradev.todolist.domain.repository.ShoppingListRepository
import org.efradev.todolist.domain.model.DomainShoppingList
import org.efradev.todolist.domain.model.DomainAddItemRequest

const val BASE_URL = "https://platform-production-dbfb.up.railway.app"

class ShoppingListRepositoryImpl(
    private val client: HttpClient,
    private val authLocalStorage: AuthLocalStorage
) : ShoppingListRepository {

    override suspend fun getShoppingLists(): Result<List<DomainShoppingList>> {
        return try {
            val token = authLocalStorage.getAccessToken()
            val response = client.get("$BASE_URL/api/v1/shopping/lists") {
                header("Authorization", "Bearer $token")
            }
            val shoppingLists = response.body<List<ShoppingList>>()
            Result.success(shoppingLists.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createList(name: String, type: String): Result<DomainShoppingList> {
        return try {
            val token = authLocalStorage.getAccessToken()
            val request = createShoppingListRequest(name, type)
            val response = client.post("$BASE_URL/api/v1/shopping/lists") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val shoppingList = response.body<ShoppingList>()
            Result.success(shoppingList.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getShoppingListDetails(listId: String): Result<DomainShoppingList> {
        return try {
            val token = authLocalStorage.getAccessToken()
            val response = client.get("$BASE_URL/api/v1/shopping/lists/$listId") {
                header("Authorization", "Bearer $token")
            }
            val shoppingList = response.body<ShoppingList>()
            Result.success(shoppingList.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addItemToList(listId: String, item: DomainAddItemRequest): Result<DomainShoppingList> {
        return try {
            val token = authLocalStorage.getAccessToken()
            val request = item.toData()
            val response = client.post("$BASE_URL/api/v1/shopping/lists/$listId/items") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val shoppingList = response.body<ShoppingList>()
            Result.success(shoppingList.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
