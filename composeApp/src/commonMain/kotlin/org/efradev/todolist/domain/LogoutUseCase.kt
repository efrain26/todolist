package org.efradev.todolist.domain

import org.efradev.todolist.data.local.PreferencesRepository

sealed interface LogoutResult {
    data class Success(val message: String) : LogoutResult
    data class Error(val message: String) : LogoutResult
}

class LogoutUseCase(
    private val preferencesRepository: PreferencesRepository,
    private val stringResProvider: StringResProvider
) {
    suspend operator fun invoke(): Result<LogoutResult> {
        return try {
            preferencesRepository.clearAuthData()
            Result.success(
                LogoutResult.Success(
                    message = stringResProvider("logout_success")
                )
            )
        } catch (e: Exception) {
            Result.success(
                LogoutResult.Error(
                    message = stringResProvider("logout_error")
                )
            )
        }
    }
}