package org.efradev.todolist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.efradev.todolist.di.initKoin
import org.efradev.todolist.viewmodel.LoginState
import org.efradev.todolist.viewmodel.LoginViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginForm(
    email: String,
    onBack: () -> Unit,
    onLoginSuccess: (String) -> Unit
) {
    val viewModel: LoginViewModel = koinViewModel<LoginViewModel>()
    var password by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.state) {
        when (val state = viewModel.state) {
            is LoginState.Success -> {
                onLoginSuccess(state.message)
            }
            is LoginState.NavigateToLists -> {
                onLoginSuccess("Login exitoso")
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Login para $email",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (viewModel.state is LoginState.Error) {
            Text(
                text = (viewModel.state as LoginState.Error).message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onBack,
                modifier = Modifier.weight(1f),
                enabled = viewModel.state !is LoginState.Loading
            ) {
                Text("Volver")
            }

            Button(
                onClick = { viewModel.login(email, password) },
                modifier = Modifier.weight(1f),
                enabled = password.isNotBlank() && viewModel.state !is LoginState.Loading
            ) {
                if (viewModel.state is LoginState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Ingresar")
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginFormPreview() {
    initKoin()
    LoginForm(
        email = "usuario@ejemplo.com",
        onBack = {},
        onLoginSuccess = {}
    )
}