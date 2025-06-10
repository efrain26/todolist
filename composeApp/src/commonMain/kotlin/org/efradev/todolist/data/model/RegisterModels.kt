package org.efradev.todolist.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String
)

@Serializable
data class RegisterResponse(
    val id: Int,
    val username: String,
)

@Serializable
data class UserData(
    val id: String,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String
)

@Serializable
data class UserCheckResponse(
    val code: String,
    val email: String
)

