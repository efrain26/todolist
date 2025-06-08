package org.efradev.todolist.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginForm(email: String, onBack: () -> Unit) {
    var password by remember { mutableStateOf("") }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Login para $email")
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            singleLine = true
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = { /* lógica de login */ }) { Text("Ingresar") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onBack) { Text("Volver") }
    }
}

