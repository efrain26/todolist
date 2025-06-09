package org.efradev.todolist.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.efradev.todolist.data.model.RegisterRequest
import org.efradev.todolist.data.model.RegisterResponse

interface UserRegisterRepository {
    suspend fun registerUser(request: RegisterRequest): Result<RegisterResponse>
}

class UserRegisterRepositoryImpl(private val client: HttpClient) : UserRegisterRepository {
    override suspend fun registerUser(request: RegisterRequest): Result<RegisterResponse> {
        return try {
            val response = client.post("https://platform-production-c248.up.railway.app/api/v1/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            Result.success(response.body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
