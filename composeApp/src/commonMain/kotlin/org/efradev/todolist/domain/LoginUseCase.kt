package org.efradev.todolist.domain

import org.efradev.todolist.data.UserRepository
import org.efradev.todolist.data.model.LoginRequest

sealed interface LoginResult {
    val message: String

    data class Success(
        override val message: String
    ) : LoginResult

    data class Error(override val message: String) : LoginResult
}

class LoginUseCase(
    private val repository: UserRepository,
    private val stringRes: StringResProvider
) {
    suspend operator fun invoke(email: String, password: String): Result<LoginResult> {

        val request = LoginRequest(
            email = email,
            password = password
        )
        return repository.login(request).map { response ->
            LoginResult.Success(
                message = stringRes("login_success"),
            )
        }
    }
}
