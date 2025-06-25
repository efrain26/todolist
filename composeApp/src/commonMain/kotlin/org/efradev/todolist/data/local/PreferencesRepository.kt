package org.efradev.todolist.data.local

import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json
import org.efradev.todolist.data.model.AuthResponse

interface PreferencesRepository {
    suspend fun saveAuthData(authResponse: AuthResponse)
    suspend fun getAuthData(): AuthResponse?
    suspend fun clearAuthData()
    suspend fun isUserLoggedIn(): Boolean
}

class PreferencesRepositoryImpl(
    private val settings: Settings,
    private val json: Json
) : PreferencesRepository {

    override suspend fun saveAuthData(authResponse: AuthResponse) {
        settings.putString(KEY_AUTH_DATA, json.encodeToString(AuthResponse.serializer(), authResponse))
    }

    override suspend fun getAuthData(): AuthResponse? {
        val authDataString = settings.getStringOrNull(KEY_AUTH_DATA)
        return authDataString?.let {
            json.decodeFromString(AuthResponse.serializer(), it)
        }
    }

    override suspend fun clearAuthData() {
        settings.remove(KEY_AUTH_DATA)
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return settings.hasKey(KEY_AUTH_DATA)
    }

    companion object {
        private const val KEY_AUTH_DATA = "auth_data"
    }
}

