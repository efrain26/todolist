package org.efradev.todolist.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.efradev.todolist.domain.AuthState
import org.efradev.todolist.domain.CheckAuthStateUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

class AuthViewModel(
    private val checkAuthStateUseCase: CheckAuthStateUseCase
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthUiState>(AuthUiState.Loading)
    val authState: StateFlow<AuthUiState> = _authState.asStateFlow()

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            checkAuthStateUseCase()
                .onSuccess { authState ->
                    when (authState) {
                        is AuthState.Authenticated -> _authState.value = AuthUiState.Authenticated
                        AuthState.NotAuthenticated -> _authState.value = AuthUiState.NotAuthenticated
                    }
                }
                .onFailure {
                    _authState.value = AuthUiState.Error("Error checking auth state")
                }
        }
    }
}

sealed interface AuthUiState {
    object Loading : AuthUiState
    object Authenticated : AuthUiState
    object NotAuthenticated : AuthUiState
    data class Error(val message: String) : AuthUiState
}
