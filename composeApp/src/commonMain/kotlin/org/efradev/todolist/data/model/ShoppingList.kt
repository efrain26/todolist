package org.efradev.todolist.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShoppingList(
    val id: String,
    val name: String,
    @SerialName("items_count")
    val itemsCount: Int
)
