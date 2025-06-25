package org.efradev.todolist.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.efradev.todolist.domain.LoginResult
import org.efradev.todolist.domain.LoginUseCase

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(
        val message: String
    ) : LoginState()
    data class Error(val message: String) : LoginState()
    object NavigateToLists : LoginState()
}

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    var state by mutableStateOf<LoginState>(LoginState.Idle)
        private set

    fun login(email: String, password: String) {
        state = LoginState.Loading
        viewModelScope.launch {
            loginUseCase(email, password).fold(
                onSuccess = { result ->
                    when (result) {
                        is LoginResult.Success -> {
                            state = LoginState.NavigateToLists
                        }
                        is LoginResult.Error -> state = LoginState.Error(result.message)
                    }
                },
                onFailure = { error ->
                    state = LoginState.Error(error.message ?: "Error desconocido")
                }
            )
        }
    }
}

sealed interface LoginUiState {
    data object Initial : LoginUiState
    data object Loading : LoginUiState
    data class Error(val message: String) : LoginUiState
    data object NavigateToLists : LoginUiState
}
