package org.efradev.todolist.domain

import org.efradev.todolist.domain.repository.UserRepository
import org.efradev.todolist.domain.repository.PreferencesRepository

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
        return repository.login(email, password).fold(
            onSuccess = { authData ->
                preferencesRepository.saveAuthData(authData)
                Result.success(LoginResult.Success(""))
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )
    }
}
