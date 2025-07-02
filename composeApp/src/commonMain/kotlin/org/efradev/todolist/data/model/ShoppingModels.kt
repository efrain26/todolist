package org.efradev.todolist.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShoppingList(
    val id: String,
    val name: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("user_id")
    val userId: String,
    val type: String,
    val items: List<ShoppingItem> = emptyList()
)

@Serializable
data class ShoppingItem(
    val name: String,
    val status: String,
    val type: String
)

@Serializable
data class CreateShoppingListRequest(
    val name: String,
    val type: String = "simple"
)

@Serializable
data class AddItemRequest(
    val name: String,
    val status: String = "pendiente",
    val type: String = "simple",
    val price: Double? = null,
    val quantity: Int? = null,
    val url: String? = null,
    val store: String? = null,
    val notes: String? = null,
    val platform: String? = null,
    val genre: String? = null,
    val year: Int? = null,
    val rating: String? = null,
    @SerialName("due_date")
    val dueDate: String? = null,
    val priority: String? = null
)