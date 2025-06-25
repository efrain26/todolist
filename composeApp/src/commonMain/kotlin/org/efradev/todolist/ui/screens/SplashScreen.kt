package org.efradev.todolist.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.efradev.todolist.viewmodel.AuthUiState
import org.efradev.todolist.viewmodel.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreen(
    onAuthenticated: () -> Unit,
    onNotAuthenticated: () -> Unit
) {
    val viewModel: AuthViewModel = koinViewModel<AuthViewModel>()
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthUiState.Authenticated -> onAuthenticated()
            is AuthUiState.NotAuthenticated -> onNotAuthenticated()
            is AuthUiState.Error -> onNotAuthenticated()
            AuthUiState.Loading -> { /* Mostrar loading */ }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
