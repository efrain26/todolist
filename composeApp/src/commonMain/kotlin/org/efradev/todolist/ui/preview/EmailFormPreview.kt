package org.efradev.todolist.ui.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.efradev.todolist.ui.screens.EmailForm
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun EmailFormPreview() {
    MaterialTheme {
        EmailForm(
            onSubmit = {}
        )
    }
}

@Preview
@Composable
fun EmailFormEmptyPreview() {
    MaterialTheme {
        EmailForm(
            onSubmit = {}
        )
    }
}
