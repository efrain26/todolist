package org.efradev.todolist

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.efradev.todolist.view.EmailForm
import org.efradev.todolist.view.LoginForm
import org.efradev.todolist.view.RegisterForm
import org.jetbrains.compose.ui.tooling.preview.Preview

fun isEmailRegistered(email: String): Boolean {
    // Lógica dummy: si el email contiene "test" está registrado
    return email.contains("test")
}

@Composable
@Preview
fun App() {
    MaterialTheme {
        var screen by remember { mutableStateOf("email") }
        var email by remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (screen) {
                "email" -> EmailForm { enteredEmail ->
                    email = enteredEmail
                    screen = if (isEmailRegistered(email)) "login" else "register"
                }
                "register" -> RegisterForm(email) { screen = "email" }
                "login" -> LoginForm(email) { screen = "email" }
            }
        }
    }
}