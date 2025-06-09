package org.efradev.todolist.domain

import org.efradev.todolist.data.UserCheckResult
import org.efradev.todolist.data.UserRepository

// Para multiplataforma, define una interfaz para obtener strings de recursos
typealias StringResProvider = (key: String) -> String

data class UserCheckResultWithMessage(val result: UserCheckResult, val message: String)

class CheckUserExistsUseCase(
    private val repository: UserRepository,
    private val stringRes: StringResProvider
) {
    suspend operator fun invoke(email: String): UserCheckResultWithMessage {
        return when (val result = repository.checkUser(email)) {
            is UserCheckResult.Registered -> UserCheckResultWithMessage(result, stringRes("user_registered"))
            is UserCheckResult.NotRegistered -> UserCheckResultWithMessage(result, stringRes("user_not_registered"))
            is UserCheckResult.Error -> UserCheckResultWithMessage(result, result.message ?: stringRes("unexpected_error"))
        }
    }
}
