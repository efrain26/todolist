package org.efradev.todolist.domain

actual fun provideStringResProvider(): StringResProvider = { key ->
    // Diccionario simple para pruebas, puedes mapear a Localizable.strings si lo deseas
    when (key) {
        "user_registered" -> "El usuario ya está registrado."
        "user_not_registered" -> "El usuario no está registrado."
        "unexpected_error" -> "Ocurrió un error inesperado."
        else -> key
    }
}

