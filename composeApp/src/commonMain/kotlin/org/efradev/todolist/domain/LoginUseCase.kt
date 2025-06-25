package org.efradev.todolist.domain

import org.efradev.todolist.data.UserRepository
import org.efradev.todolist.data.local.PreferencesRepository
import org.efradev.todolist.data.model.LoginRequest
import org.efradev.todolist.data.model.LoginResponse

sealed interface LoginResult {
    val message: String

    data class Success(
        override val message: String
    ) : LoginResult

    data class Error(override val message: String) : LoginResult
}

class LoginUseCase(
    private val repository: UserRepository,
    private val preferencesRepository: PreferencesRepository,
    private val stringResProvider: StringResProvider
) {
    suspend operator fun invoke(email: String, password: String): Result<LoginResult> {

        val request = LoginRequest(
            email = email,
            password = password
        )
        return repository.login(request).fold(
                onSuccess = {
                    preferencesRepository.saveAuthData(it)
                    Result.success(LoginResult.Success(""))
                },
                onFailure = { throwable ->
                    Result.failure(throwable)
                }
            )

    }
}
