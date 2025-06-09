package org.efradev.todolist.data

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import kotlinx.serialization.Serializable
import io.ktor.client.plugins.*

sealed class UserCheckResult {
    object Registered : UserCheckResult()
    object NotRegistered : UserCheckResult()
    data class Error(val code: String?, val message: String?) : UserCheckResult()
}

@Serializable
data class UserCheckResponse(val code: String, val email: String)

interface UserRepository {
    suspend fun checkUser(email: String): UserCheckResult
}

class UserRepositoryImpl(private val client: HttpClient) : UserRepository {
    override suspend fun checkUser(email: String): UserCheckResult {
        return try {
            val response: HttpResponse = client.post("https://platform-production-c248.up.railway.app/api/v1/auth/validate-user") {
                parameter("email", email)
            }
            val body = response.body<UserCheckResponse>()
            when (body.code) {
                "USER_REGISTERED" -> UserCheckResult.Registered
                "USER_NOT_REGISTERED" -> UserCheckResult.NotRegistered
                else -> UserCheckResult.Error(body.code, body.email)
            }
        } catch (e: ResponseException) {
            val errorBody = try { e.response.body<UserCheckResponse>() } catch (_: Exception) { null }
            UserCheckResult.Error(errorBody?.code, errorBody?.email ?: e.message)
        } catch (e: Exception) {
            UserCheckResult.Error(null, e.message)
        }
    }
}
