package org.efradev.todolist

import android.content.Context
import android.os.Build
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

class AndroidPlatform(private val context: Context) : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"

    override fun createSettings(): Settings {
        val delegate = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        return SharedPreferencesSettings(delegate)
    }

    companion object {
        private var instance: AndroidPlatform? = null

        fun init(context: Context) {
            if (instance == null) {
                instance = AndroidPlatform(context)
            }
        }

        fun getInstance(): AndroidPlatform {
            return instance ?: throw IllegalStateException("PlatformSettings must be initialized with context first")
        }
    }
}

actual fun getPlatform(): Platform {
    return AndroidPlatform.getInstance()
}