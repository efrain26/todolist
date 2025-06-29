package org.efradev.todolist.domain

import org.efradev.todolist.domain.repository.UserRepository
import org.efradev.todolist.domain.model.DomainUserRegistration

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
        val userData = DomainUserRegistration(
            username = username,
            password = password,
            email = email,
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber
        )

        return repository.registerUser(userData).map { result ->
            RegisterResult.Success(result.username?.takeIf { it.isNotEmpty() } ?: stringResProvider("register_success"))
        }
    }
}
