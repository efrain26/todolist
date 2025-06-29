package org.efradev.todolist.ui.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.efradev.todolist.ui.screens.RegisterForm
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun RegisterFormPreview() {
    MaterialTheme {
        RegisterForm(
            email = "usuario@ejemplo.com",
            onBack = {},
            onRegisterSuccess = {}
        )
    }
}

@Preview
@Composable
fun RegisterFormLongEmailPreview() {
    MaterialTheme {
        RegisterForm(
            email = "usuario.con.correo.electronico.muy.largo@ejemplo.com",
            onBack = {},
            onRegisterSuccess = {}
        )
    }
}
