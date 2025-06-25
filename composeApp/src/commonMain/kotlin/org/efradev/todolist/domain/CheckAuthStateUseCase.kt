package org.efradev.todolist.domain

import org.efradev.todolist.data.local.PreferencesRepository
import org.efradev.todolist.data.model.AuthResponse

class CheckAuthStateUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(): Result<AuthState> {
        return try {
            val isLoggedIn = preferencesRepository.isUserLoggedIn()
            if (isLoggedIn) {
                val authData = preferencesRepository.getAuthData()
                if (authData != null) {
                    Result.success(AuthState.Authenticated(authData))
                } else {
                    Result.success(AuthState.NotAuthenticated)
                }
            } else {
                Result.success(AuthState.NotAuthenticated)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

sealed interface AuthState {
    data class Authenticated(val authData: AuthResponse) : AuthState
    object NotAuthenticated : AuthState
}
