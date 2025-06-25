package org.efradev.todolist.data.local

import com.russhwolf.settings.Settings

interface AuthLocalStorage {
    fun saveTokens(accessToken: String, refreshToken: String)
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun clearTokens()
}

class AuthLocalStorageImpl(private val settings: Settings) : AuthLocalStorage {
    companion object {
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
    }

    override fun saveTokens(accessToken: String, refreshToken: String) {
        settings.putString(ACCESS_TOKEN_KEY, accessToken)
        settings.putString(REFRESH_TOKEN_KEY, refreshToken)
    }

    override fun getAccessToken(): String? {
        return settings.getStringOrNull(ACCESS_TOKEN_KEY)
    }

    override fun getRefreshToken(): String? {
        return settings.getStringOrNull(REFRESH_TOKEN_KEY)
    }

    override fun clearTokens() {
        settings.remove(ACCESS_TOKEN_KEY)
        settings.remove(REFRESH_TOKEN_KEY)
    }
}
