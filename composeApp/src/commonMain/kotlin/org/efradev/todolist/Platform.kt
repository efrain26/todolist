package org.efradev.todolist

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform