package org.efradev.todolist.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.efradev.todolist.domain.RegisterResult
import org.efradev.todolist.domain.RegisterUserUseCase

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val message: String) : RegisterState()
    data class Error(val message: String) : RegisterState()
}

class RegisterViewModel(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {
    var state by mutableStateOf<RegisterState>(RegisterState.Idle)
        private set

    fun register(
        username: String,
        password: String,
        email: String,
        firstName: String,
        lastName: String,
        phoneNumber: String
    ) {
        state = RegisterState.Loading
        viewModelScope.launch {
            registerUserUseCase(
                username = username,
                password = password,
                email = email,
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phoneNumber
            ).fold(
                onSuccess = { result ->
                    when (result) {
                        is RegisterResult.Success -> state = RegisterState.Success(result.message)
                        is RegisterResult.Error -> state = RegisterState.Error(result.message)
                    }
                },
                onFailure = { error ->
                    state = RegisterState.Error(error.message ?: "Error desconocido")
                }
            )
        }
    }
}
