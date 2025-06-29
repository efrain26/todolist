package org.efradev.todolist.data.local

import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json
import org.efradev.todolist.data.model.AuthResponse
import org.efradev.todolist.data.mapper.toDomain
import org.efradev.todolist.data.mapper.toDataResponse
import org.efradev.todolist.domain.repository.PreferencesRepository
import org.efradev.todolist.domain.model.DomainAuthData

class PreferencesRepositoryImpl(
    private val settings: Settings,
    private val json: Json
) : PreferencesRepository {

    override suspend fun saveAuthData(authData: DomainAuthData) {
        val authResponse = authData.toDataResponse()
        settings.putString(KEY_AUTH_DATA, json.encodeToString(AuthResponse.serializer(), authResponse))
    }

    override suspend fun getAuthData(): DomainAuthData? {
        val authDataString = settings.getStringOrNull(KEY_AUTH_DATA)
        return authDataString?.let {
            val authResponse = json.decodeFromString(AuthResponse.serializer(), it)
            authResponse.toDomain()
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

