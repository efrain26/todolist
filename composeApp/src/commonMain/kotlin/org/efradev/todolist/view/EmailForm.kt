package org.efradev.todolist.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun EmailForm(onSubmit: (String) -> Unit) {
    var email by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = { if (email.isNotBlank()) onSubmit(email) }) {
                Text("Continuar")
            }
        }
    }
}

@Preview
@Composable
fun EmailFormPreview() {
    EmailForm(onSubmit = {})
}
