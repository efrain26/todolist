package org.efradev.todolist.ui.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.efradev.todolist.ui.components.CreateListBottomSheet
import org.efradev.todolist.viewmodel.CreateListUiState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun CreateListBottomSheetPreview() {
    MaterialTheme {
        CreateListBottomSheet(
            uiState = CreateListUiState.Idle,
            onDismiss = { },
            onCreateList = { _, _ -> }
        )
    }
}

@Preview
@Composable
fun CreateListBottomSheetLoadingPreview() {
    MaterialTheme {
        CreateListBottomSheet(
            uiState = CreateListUiState.Loading,
            onDismiss = { },
            onCreateList = { _, _ -> }
        )
    }
}

@Preview
@Composable
fun CreateListBottomSheetErrorPreview() {
    MaterialTheme {
        CreateListBottomSheet(
            uiState = CreateListUiState.Error("Error al crear la lista. Inténtalo nuevamente."),
            onDismiss = { },
            onCreateList = { _, _ -> }
        )
    }
}
