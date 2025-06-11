package org.efradev.todolist.data

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.efradev.todolist.data.model.RegisterRequest
import org.efradev.todolist.data.model.RegisterResponse
import org.efradev.todolist.data.model.UserCheckResponse
import org.efradev.todolist.data.model.LoginResponse

sealed class UserCheckResult {
    object Registered : UserCheckResult()
    object NotRegistered : UserCheckResult()
    data class Error(val code: String?, val message: String?) : UserCheckResult()
}


interface UserRepository {
    suspend fun checkUser(email: String): Result<UserCheckResult>

    suspend fun registerUser(request: RegisterRequest): Result<RegisterResponse>

    suspend fun login(email: String, password: String): Result<LoginResponse>
}

class UserRepositoryImpl(
    private val client: HttpClient,
    private val authLocalStorage: AuthLocalStorage
) : UserRepository {

    override suspend fun checkUser(email: String): Result<UserCheckResult> {
        return try {
            val response: HttpResponse = client.post("https://platform-production-c248.up.railway.app/api/v1/auth/validate-user") {
                parameter("email", email)
            }
            val body = response.body<UserCheckResponse>()
            val result = when (body.code) {
                "USER_REGISTERED" -> UserCheckResult.Registered
                "USER_NOT_REGISTERED" -> UserCheckResult.NotRegistered
                else -> UserCheckResult.Error(body.code, body.email)
            }
            Result.success(result)
        } catch (e: ResponseException) {
            val errorBody = try { e.response.body<UserCheckResponse>() } catch (_: Exception) { null }
            Result.success(UserCheckResult.Error(errorBody?.code, errorBody?.email ?: e.message))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerUser(request: RegisterRequest): Result<RegisterResponse> {
        return try {
            val response = client.post("https://platform-production-c248.up.railway.app/api/v1/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            Result.success(response.body())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = client.post("https://platform-production-c248.up.railway.app/api/v1/auth/login") {
                contentType(ContentType.Application.FormUrlEncoded)
                setBody("username=${email}&password=${password}")
            }
            val loginResponse = response.body<LoginResponse>()
            // Guardar los tokens
            authLocalStorage.saveTokens(
                accessToken = loginResponse.accessToken,
                refreshToken = loginResponse.refreshToken
            )
            Result.success(loginResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
