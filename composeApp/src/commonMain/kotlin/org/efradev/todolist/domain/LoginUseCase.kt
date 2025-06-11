package org.efradev.todolist.domain

import org.efradev.todolist.data.UserRepository

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
        return repository.login(email, password).map { response ->
            LoginResult.Success(
                message = stringRes("login_success"),
            )
        }
    }
}
