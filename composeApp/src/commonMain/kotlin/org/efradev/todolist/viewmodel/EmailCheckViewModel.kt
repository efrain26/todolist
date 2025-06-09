package org.efradev.todolist.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.efradev.todolist.domain.CheckUserExistsUseCase
import org.efradev.todolist.data.UserCheckResult

sealed class EmailCheckState {
    object Idle : EmailCheckState()
    object Loading : EmailCheckState()
    data class Result(val result: UserCheckResult, val message: String) : EmailCheckState()
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
            try {
                val resultWithMsg = checkUserExistsUseCase(email)
                state = EmailCheckState.Result(resultWithMsg.result, resultWithMsg.message)
            } catch (e: Exception) {
                e.printStackTrace()
                state = EmailCheckState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
