package org.efradev.todolist.domain.model

/**
 * Domain model for User entity
 * 
 * This represents a user in the domain layer, independent of data sources.
 * It contains only the essential properties needed for business logic.
 */
data class DomainUser(
    val id: String,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String
)
