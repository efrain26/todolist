package org.efradev.todolist.domain.repository

import org.efradev.todolist.domain.model.DomainAuthData

/**
 * Domain repository interface for Preferences/Local Storage operations
 * 
 * This interface defines local storage operations in domain terms,
 * using domain models instead of data DTOs.
 * The actual implementation will be in the data layer.
 */
interface PreferencesRepository {
    suspend fun saveAuthData(authData: DomainAuthData)
    suspend fun getAuthData(): DomainAuthData?
    suspend fun clearAuthData()
    suspend fun isUserLoggedIn(): Boolean
}
