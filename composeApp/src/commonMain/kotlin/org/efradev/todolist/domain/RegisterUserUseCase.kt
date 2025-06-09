package org.efradev.todolist.domain

import org.efradev.todolist.data.UserRegisterRepository
import org.efradev.todolist.data.model.RegisterRequest

class RegisterUserUseCase(
    private val repository: UserRegisterRepository,
    private val stringResProvider: StringResProvider
) {
    suspend operator fun invoke(
        username: String,
        password: String,
        email: String,
        firstName: String,
        lastName: String,
        phoneNumber: String
    ): Result<String> {
        val request = RegisterRequest(
            username = username,
            password = password,
            email = email,
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber
        )

        return repository.registerUser(request).map { response ->
            response.message ?: stringResProvider("register_success")
        }
    }
}
