package org.efradev.todolist.domain

import org.efradev.todolist.data.UserCheckResult
import org.efradev.todolist.data.UserRepository

// Para multiplataforma, define una interfaz para obtener strings de recursos
typealias StringResProvider = (key: String) -> String

sealed interface UserCheckResultWithMessage {
    val message: String

    data class Registered(override val message: String) : UserCheckResultWithMessage
    data class NotRegistered(override val message: String) : UserCheckResultWithMessage
    data class Error(override val message: String) : UserCheckResultWithMessage
}

class CheckUserExistsUseCase(
    private val repository: UserRepository,
    private val stringRes: StringResProvider
) {
    suspend operator fun invoke(email: String): Result<UserCheckResultWithMessage> {
        return repository.checkUser(email).map { result ->
            when (result) {
                is UserCheckResult.Registered ->
                    UserCheckResultWithMessage.Registered(stringRes("user_registered"))
                is UserCheckResult.NotRegistered ->
                    UserCheckResultWithMessage.NotRegistered(stringRes("user_not_registered"))
                is UserCheckResult.Error ->
                    UserCheckResultWithMessage.Error(result.message ?: stringRes("unexpected_error"))
            }
        }
    }
}
