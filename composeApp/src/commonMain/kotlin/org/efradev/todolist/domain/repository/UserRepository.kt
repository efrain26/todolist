package org.efradev.todolist.domain.repository

import org.efradev.todolist.domain.model.DomainAuthData
import org.efradev.todolist.domain.model.DomainUserRegistration
import org.efradev.todolist.domain.model.DomainRegistrationResult

/**
 * Domain repository interface for User operations
 * 
 * This interface defines user-related operations in domain terms,
 * using domain models instead of data DTOs.
 * The actual implementation will be in the data layer.
 */
interface UserRepository {
    suspend fun checkUser(email: String): Result<UserCheckResult>
    suspend fun registerUser(userData: DomainUserRegistration): Result<DomainRegistrationResult>
    suspend fun login(email: String, password: String): Result<DomainAuthData>
}

/**
 * Domain representation of User Check Result
 * 
 * This sealed class represents the possible outcomes of checking
 * if a user exists in the system.
 */
sealed class UserCheckResult {
    object Registered : UserCheckResult()
    object NotRegistered : UserCheckResult() 
    data class Error(val code: String?, val message: String?) : UserCheckResult()
}
