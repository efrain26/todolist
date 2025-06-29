package org.efradev.todolist.domain.model

/**
 * Domain model for Shopping Item
 * 
 * Represents an individual item within a shopping list in the domain layer.
 */
data class DomainShoppingItem(
    val name: String,
    val status: String,
    val type: String
)
