package org.efradev.todolist.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.http.ContentType
import kotlinx.serialization.json.Json
import org.efradev.todolist.data.auth.TokenAuthenticator

object HttpClientFactory {
    fun create(
        useTokenAuthenticator: Boolean = true,
        json: Json = Json { ignoreUnknownKeys = true }
    ): HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(json, contentType = ContentType.Any)
        }

        if (useTokenAuthenticator) {
            install(TokenAuthenticator) {
                excludePaths = listOf("/api/v1/auth/login", "/api/v1/auth/refresh")
            }
        }
    }
}
