package org.efradev.todolist

import com.russhwolf.settings.Settings

interface Platform {
    val name: String

    fun createSettings(): Settings
}

expect fun getPlatform(): Platform