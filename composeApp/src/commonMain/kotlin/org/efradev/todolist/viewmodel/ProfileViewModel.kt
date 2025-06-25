package org.efradev.todolist.viewmodel

import kotlinx.coroutines.launch
import org.efradev.todolist.domain.LogoutUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

class ProfileViewModel(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            logoutUseCase()
                .onSuccess { onLogoutComplete() }
        }
    }
}
