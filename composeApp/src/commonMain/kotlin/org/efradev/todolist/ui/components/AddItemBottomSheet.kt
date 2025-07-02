package org.efradev.todolist.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Bottom sheet for adding new items to a shopping list
 * Following the UI patterns from Figma design
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onAddItem: (itemName: String, listType: String) -> Unit,
    listType: String = "simple",
    modifier: Modifier = Modifier
) {
    var itemName by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(listType) }
    
    val listTypes = listOf(
        "simple" to "Simple",
        "compras" to "Compras", 
        "peliculas" to "Películas",
        "tareas" to "Tareas",
        "deseos" to "Deseos",
        "viajes" to "Viajes",
        "ideas" to "Ideas"
    )
    
    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            modifier = modifier,
            dragHandle = {
                // Custom drag handle to match Figma design
                Surface(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .size(width = 32.dp, height = 4.dp),
                    shape = RoundedCornerShape(100.dp),
                    color = MaterialTheme.colorScheme.outline
                ) {}
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Header section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Agregar item",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Medium
                    )
                    
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                }
                
                // Input field
                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text("Agrega un nuevo item") },
                    placeholder = { Text("Escribe el nombre del item") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                // Filter chips for list types
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Tipo de lista",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(listTypes) { (type, label) ->
                            FilterChip(
                                onClick = { selectedType = type },
                                label = { Text(label) },
                                selected = selectedType == type,
                                modifier = Modifier.height(32.dp)
                            )
                        }
                    }
                }
                
                // Add button
                Button(
                    onClick = {
                        if (itemName.isNotBlank()) {
                            onAddItem(itemName.trim(), selectedType)
                            itemName = ""
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = itemName.isNotBlank()
                ) {
                    Text("Agregar item")
                }
            }
        }
    }
}
