package org.efradev.todolist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.efradev.todolist.domain.model.DomainShoppingList
import org.efradev.todolist.domain.model.DomainShoppingItem
import org.efradev.todolist.viewmodel.ShoppingListDetailsUiState
import org.efradev.todolist.viewmodel.ShoppingListDetailsViewModel
import org.efradev.todolist.ui.components.AddItemBottomSheet
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListDetailsScreen(
    listId: String,
    onBackClick: () -> Unit = {},
    onListDeleted: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val viewModel: ShoppingListDetailsViewModel = koinViewModel<ShoppingListDetailsViewModel>()
    var showDropdownMenu by remember { mutableStateOf(false) }
    var showAddItemBottomSheet by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    
    // Load list details when the screen is first composed
    LaunchedEffect(listId) {
        viewModel.loadListDetails(listId)
    }

    // Handle navigation after deletion
    LaunchedEffect(viewModel.uiState) {
        if (viewModel.uiState is ShoppingListDetailsUiState.Deleted) {
            onListDeleted()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") }, // Empty title to match Figma design
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Share functionality */ }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share"
                        )
                    }
                    
                    // More options with dropdown menu
                    Box {
                        IconButton(onClick = { showDropdownMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options"
                            )
                        }
                        
                        DropdownMenu(
                            expanded = showDropdownMenu,
                            onDismissRequest = { showDropdownMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Eliminar") },
                                onClick = {
                                    showDropdownMenu = false
                                    showDeleteConfirmDialog = true
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete"
                                    )
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            BottomActionButtons(
                onAddItemClick = { showAddItemBottomSheet = true }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        when (val state = viewModel.uiState) {
            is ShoppingListDetailsUiState.Loading -> {
                LoadingContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
            is ShoppingListDetailsUiState.Success -> {
                ListDetailsContent(
                    list = state.list,
                    onRefresh = { viewModel.refresh(listId) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
            is ShoppingListDetailsUiState.Error -> {
                ErrorContent(
                    message = state.message,
                    onRetry = { viewModel.loadListDetails(listId) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
            is ShoppingListDetailsUiState.Deleted -> {
                // This state is handled in LaunchedEffect for navigation
                // Show loading while navigating
                LoadingContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
        }
    }
    
    // Add Item Bottom Sheet
    val currentListType = when (val state = viewModel.uiState) {
        is ShoppingListDetailsUiState.Success -> state.list.type
        else -> "simple"
    }
    
    AddItemBottomSheet(
        isVisible = showAddItemBottomSheet,
        onDismiss = { showAddItemBottomSheet = false },
        onAddItem = { itemName, listType ->
            viewModel.addItem(listId, itemName, listType)
        },
        listType = currentListType
    )

    // Delete confirmation dialog
    if (showDeleteConfirmDialog) {
        DeleteConfirmationDialog(
            onConfirm = {
                showDeleteConfirmDialog = false
                viewModel.deleteList(listId)
            },
            onDismiss = {
                showDeleteConfirmDialog = false
            },
            isDeleting = viewModel.isDeletingList
        )
    }
}

@Composable
internal fun LoadingContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
internal fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Button(
            onClick = onRetry,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Retry")
        }
    }
}

@Composable
internal fun ListDetailsContent(
    list: DomainShoppingList,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        // Fixed header sections
        HeaderSection(
            list = list,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        ItemsSectionHeader(
            itemCount = list.items.size,
            modifier = Modifier.padding(16.dp)
        )
        
        // Scrollable items list
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // List items
            items(list.items) { item ->
                ListItemCard(
                    item = item
                )
            }

            // Empty state for items if needed
            if (list.items.isEmpty()) {
                item {
                    EmptyItemsState(
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
internal fun HeaderSection(
    list: DomainShoppingList,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // Text content
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Title and supporting text
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = list.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = "${list.createdAt}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
internal fun DescriptionSection(
    list: DomainShoppingList,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),

    ) {
        Text(
            text = "PUBLISHED DATE",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "This is a detailed description of the ${list.name} list. " +
                    "It contains ${list.items.size} items and was created on ${list.createdAt}. " +
                    "You can manage and organize your items here.",
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
        )
    }
}

@Composable
internal fun ItemsSectionHeader(
    itemCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Items ($itemCount)",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
internal fun ListItemCard(
    item: DomainShoppingItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Item content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Title and description
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Status: ${item.status}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
internal fun EmptyItemsState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No items yet",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Add your first item to get started",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

/**
 * Bottom action buttons that remain fixed at the bottom
 * Matches the Figma design with two buttons
 */
@Composable
internal fun BottomActionButtons(
    onAddItemClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Primary action button (Edit)
            Button(
                onClick = { /* TODO: Edit functionality */ },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Editar Lista")
            }
            
            // Secondary action button (Add Item)
            OutlinedButton(
                onClick = onAddItemClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Agregar Item")
            }
        }
    }
}

/**
 * Dialog for confirming list deletion
 * Follows Material Design guidelines for destructive actions
 */
@Composable
internal fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isDeleting: Boolean,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = if (isDeleting) { {} } else onDismiss,
        title = {
            Text(
                text = "Eliminar lista",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                text = "¿Estás seguro de que quieres eliminar esta lista? Esta acción no se puede deshacer.",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isDeleting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                if (isDeleting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onError
                    )
                } else {
                    Text(
                        text = "Eliminar",
                        color = MaterialTheme.colorScheme.onError
                    )
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isDeleting
            ) {
                Text("Cancelar")
            }
        },
        modifier = modifier
    )
}
