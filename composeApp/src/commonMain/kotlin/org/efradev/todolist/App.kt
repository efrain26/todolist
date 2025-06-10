package org.efradev.todolist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.efradev.todolist.di.initKoin
import org.efradev.todolist.view.EmailForm
import org.efradev.todolist.view.LoginForm
import org.efradev.todolist.view.RegisterForm
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.efradev.todolist.viewmodel.EmailCheckViewModel
import org.efradev.todolist.viewmodel.EmailCheckState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    MaterialTheme {
        var screen by remember { mutableStateOf("email") }
        var email by remember { mutableStateOf("") }
        val viewModel: EmailCheckViewModel = koinViewModel<EmailCheckViewModel>()
        val state = viewModel.state
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (screen) {
                "email" -> EmailForm(
                    onSubmit = { enteredEmail ->
                        email = enteredEmail
                        viewModel.checkEmail(email)
                    },
                    isLoading = state is EmailCheckState.Loading,
                    errorMessage = (state as? EmailCheckState.Error)?.message
                        ?: (state as? EmailCheckState.Error)?.message
                )
                "register" -> RegisterForm(
                    email = email,
                    onBack = { screen = "email" },
                    onRegisterSuccess = { screen = "login" }
                )
                "login" -> LoginForm(
                    email = email,
                    onBack = { screen = "email" },
                    onLoginSuccess = { screen = "login" }
                )
            }
            // Navegación automática según el estado
            LaunchedEffect(state) {
                when (state) {
                    is EmailCheckState.Success -> {
                        if (state.isRegistered) {
                            screen = "login"
                        } else {
                            screen = "register"
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    initKoin()
    App(
    )
}