package org.efradev.todolist.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.efradev.todolist.domain.CheckUserExistsUseCase
import org.efradev.todolist.domain.UserCheckResultWithMessage

sealed class EmailCheckState {
    object Idle : EmailCheckState()
    object Loading : EmailCheckState()
    data class Success(val message: String, val isRegistered: Boolean) : EmailCheckState()
    data class Error(val message: String) : EmailCheckState()
}

class EmailCheckViewModel(
    val checkUserExistsUseCase: CheckUserExistsUseCase
) : ViewModel() {
    var state by mutableStateOf<EmailCheckState>(EmailCheckState.Idle)
        private set

    fun checkEmail(email: String) {
        state = EmailCheckState.Loading
        viewModelScope.launch {
            checkUserExistsUseCase(email).fold(
                onSuccess = { result ->
                    when (result) {
                        is UserCheckResultWithMessage.Registered ->
                            state = EmailCheckState.Success(result.message, isRegistered = true)
                        is UserCheckResultWithMessage.NotRegistered ->
                            state = EmailCheckState.Success(result.message, isRegistered = false)
                        is UserCheckResultWithMessage.Error ->
                            state = EmailCheckState.Error(result.message)
                    }
                },
                onFailure = { error ->
                    state = EmailCheckState.Error(error.message ?: "Error desconocido")
                }
            )
        }
    }
}
