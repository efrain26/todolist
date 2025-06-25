package org.efradev.todolist.data.auth

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.efradev.todolist.data.BASE_URL
import org.efradev.todolist.data.local.AuthLocalStorage
import org.efradev.todolist.data.model.RefreshTokenRequest
import org.efradev.todolist.data.model.Token

class AuthManager(
    private val client: HttpClient,
    private val authLocalStorage: AuthLocalStorage
) {
    private val mutex = Mutex()
    private var isRefreshing = false

    suspend fun refreshToken(): Result<Token> = mutex.withLock {
        if (isRefreshing) {
            return@withLock Result.failure(Exception("Token refresh already in progress"))
        }

        return try {
            isRefreshing = true
            val refreshToken = authLocalStorage.getRefreshToken() ?: throw Exception("No refresh token available")

            val response = client.post("$BASE_URL/api/v1/auth/refresh") {
                contentType(ContentType.Application.Json)
                setBody(RefreshTokenRequest(refreshToken))
            }

            val token = response.body<Token>()
            authLocalStorage.saveTokens(token.accessToken, token.refreshToken)
            Result.success(token)
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            isRefreshing = false
        }
    }

    fun getCurrentToken(): String? = authLocalStorage.getAccessToken()
}
