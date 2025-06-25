package org.efradev.todolist.data.local

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.efradev.todolist.data.model.AuthResponse
import platform.Foundation.NSUserDefaults

class IosPreferencesRepository(
    private val json: Json
) : PreferencesRepository {
    private val defaults = NSUserDefaults.standardUserDefaults

    override suspend fun saveAuthData(authResponse: AuthResponse) {
        defaults.setObject(json.encodeToString(authResponse), KEY_AUTH_DATA)
    }

    override suspend fun getAuthData(): AuthResponse? {
        return defaults.stringForKey(KEY_AUTH_DATA)?.let {
            json.decodeFromString<AuthResponse>(it)
        }
    }

    override suspend fun clearAuthData() {
        defaults.removeObjectForKey(KEY_AUTH_DATA)
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return getAuthData() != null
    }

    companion object {
        private const val KEY_AUTH_DATA = "auth_data"
    }
}
