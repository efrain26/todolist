package org.efradev.todolist.ui.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.efradev.todolist.di.initKoin
import org.efradev.todolist.di.initKoinWithoutContext
import org.efradev.todolist.ui.screens.LoginForm
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun LoginFormPreview() {
    initKoinWithoutContext()
    MaterialTheme {
        LoginForm(
            email = "usuario@ejemplo.com",
            onBack = {},
            onLoginSuccess = {}
        )
    }
}

@Preview
@Composable
fun LoginFormLongEmailPreview() {
    MaterialTheme {
        LoginForm(
            email = "usuario.con.correo.muy.largo@ejemplo.com",
            onBack = {},
            onLoginSuccess = {}
        )
    }
}
