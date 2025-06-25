package org.efradev.todolist.data.local

import kotlinx.serialization.json.Json

actual fun getPreferencesRepository(): PreferencesRepository {
    return IosPreferencesRepository(Json { ignoreUnknownKeys = true })
}
