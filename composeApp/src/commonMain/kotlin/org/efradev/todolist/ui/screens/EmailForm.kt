package org.efradev.todolist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun EmailForm(
    onSubmit: (String) -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    var email by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mis Listas App",
                modifier = Modifier.padding(bottom = 32.dp),
                style = MaterialTheme.typography.headlineMedium
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { if (email.isNotBlank()) onSubmit(email) },
                enabled = !isLoading
            ) {
                Text(if (isLoading) "Cargando..." else "Continuar")
            }
            if (errorMessage != null) {
                Spacer(Modifier.height(8.dp))
                Text(errorMessage, color = Color.Red)
            }
        }
    }
}
