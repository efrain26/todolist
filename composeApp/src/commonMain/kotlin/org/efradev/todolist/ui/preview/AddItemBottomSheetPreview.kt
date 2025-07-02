package org.efradev.todolist.ui.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.efradev.todolist.ui.components.AddItemBottomSheet
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun AddItemBottomSheetPreview() {
    MaterialTheme {
        AddItemBottomSheet(
            isVisible = true,
            onDismiss = {},
            onAddItem = { _, _ -> },
            listType = "simple"
        )
    }
}

@Preview
@Composable
fun AddItemBottomSheetComprasPreview() {
    MaterialTheme {
        AddItemBottomSheet(
            isVisible = true,
            onDismiss = {},
            onAddItem = { _, _ -> },
            listType = "compras"
        )
    }
}

@Preview
@Composable
fun AddItemBottomSheetTareasPreview() {
    MaterialTheme {
        AddItemBottomSheet(
            isVisible = true,
            onDismiss = {},
            onAddItem = { _, _ -> },
            listType = "tareas"
        )
    }
}
