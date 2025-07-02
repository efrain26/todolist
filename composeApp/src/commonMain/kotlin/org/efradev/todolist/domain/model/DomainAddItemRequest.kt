package org.efradev.todolist.domain.model

/**
 * Domain model for adding a new item to a shopping list
 */
data class DomainAddItemRequest(
    val name: String,
    val listType: String = "simple",
    val price: Double? = null,
    val quantity: Int? = null,
    val url: String? = null,
    val store: String? = null,
    val notes: String? = null,
    val platform: String? = null,
    val genre: String? = null,
    val year: Int? = null,
    val rating: String? = null,
    val dueDate: String? = null,
    val priority: String? = null
)
