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
