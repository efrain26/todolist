package org.efradev.todolist.domain

import org.efradev.todolist.domain.repository.PreferencesRepository
import org.efradev.todolist.domain.model.DomainAuthData

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
    data class Authenticated(val authData: DomainAuthData) : AuthState
    object NotAuthenticated : AuthState
}
