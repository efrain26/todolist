package org.efradev.todolist.domain

import org.efradev.todolist.data.UserRepository
import org.efradev.todolist.data.model.RegisterRequest

sealed interface RegisterResult {
    data class Success(val message: String) : RegisterResult
    data class Error(val message: String) : RegisterResult
}

class RegisterUserUseCase(
    private val repository: UserRepository,
    private val stringResProvider: StringResProvider
) {
    suspend operator fun invoke(
        username: String,
        password: String,
        email: String,
        firstName: String,
        lastName: String,
        phoneNumber: String
    ): Result<RegisterResult> {
        val request = RegisterRequest(
            username = username,
            password = password,
            email = email,
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber
        )

        return repository.registerUser(request).map { response ->
            RegisterResult.Success(response.username ?: stringResProvider("register_success"))
        }
    }
}
