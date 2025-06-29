package org.efradev.todolist.domain.model

/**
 * Domain model for Authentication Data
 * 
 * This represents authentication information in the domain layer.
 * It encapsulates both user information and token data needed for authenticated operations.
 */
data class DomainAuthData(
    val user: DomainUser,
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String
)
