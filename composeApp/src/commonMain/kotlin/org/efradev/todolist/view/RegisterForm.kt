package org.efradev.todolist.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RegisterForm(email: String, onBack: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Registro para $email")
        // Aquí puedes agregar más campos de registro
        Spacer(Modifier.height(16.dp))
        Button(onClick = onBack) { Text("Volver") }
    }
}

