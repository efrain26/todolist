package org.efradev.todolist.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.efradev.todolist.viewmodel.CreateListUiState

@Composable
fun CreateListBottomSheet(
    uiState: CreateListUiState,
    onDismiss: () -> Unit,
    onCreateList: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var listName by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("simple") }
    var showError by remember { mutableStateOf(false) }

    // Lista de tipos disponibles según la API
    val listTypes = remember {
        listOf(
            CategoryOption("simple", "Simple"),
            CategoryOption("compras", "Compras"),
            CategoryOption("peliculas", "Películas"),
            CategoryOption("tareas", "Tareas"),
            CategoryOption("deseos", "Deseos"),
            CategoryOption("viajes", "Viajes"),
            CategoryOption("ideas", "Ideas")
        )
    }

    // Dialog que simula un bottom sheet modal
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                modifier = modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Header con botón de cerrar
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Título principal
                    OutlinedTextField(
                        value = "Crea una lista",
                        onValueChange = { },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent
                        )
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de entrada para el nombre
                OutlinedTextField(
                    value = listName,
                    onValueChange = { 
                        listName = it
                        showError = false
                    },
                    label = null,
                    placeholder = { Text("Ingresa nombre de lista") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = showError && listName.isBlank()
                )

                if (showError && listName.isBlank()) {
                    Text(
                        text = "El nombre de la lista es requerido",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Carrusel de tipos de lista
                CategoryChipCarousel(
                    categories = listTypes,
                    selectedCategory = selectedType,
                    onCategorySelected = { selectedType = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Botón de crear lista
                Button(
                    onClick = {
                        if (listName.isBlank()) {
                            showError = true
                        } else {
                            onCreateList(listName, selectedType)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState !is CreateListUiState.Loading,
                    shape = RoundedCornerShape(100.dp)
                ) {
                    if (uiState is CreateListUiState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Crear Lista",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }

                // Mostrar errores si los hay
                if (uiState is CreateListUiState.Error) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = uiState.message,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
    }
}
