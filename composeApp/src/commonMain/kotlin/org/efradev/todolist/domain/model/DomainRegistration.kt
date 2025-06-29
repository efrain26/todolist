package org.efradev.todolist.domain.model

/**
 * Domain model for User Registration Data
 * 
 * This represents the data needed for user registration in the domain layer.
 * It contains only the business-relevant fields without implementation details.
 */
data class DomainUserRegistration(
    val username: String,
    val password: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String
)

/**
 * Domain model for Registration Result
 * 
 * Represents the result of a user registration operation in domain terms.
 */
data class DomainRegistrationResult(
    val id: String,
    val username: String?
)
