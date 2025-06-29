package org.efradev.todolist.data.mapper

import org.efradev.todolist.data.model.AuthResponse
import org.efradev.todolist.data.model.User
import org.efradev.todolist.data.model.Token
import org.efradev.todolist.data.model.RegisterRequest
import org.efradev.todolist.data.model.RegisterResponse
import org.efradev.todolist.domain.model.DomainAuthData
import org.efradev.todolist.domain.model.DomainUser
import org.efradev.todolist.domain.model.DomainUserRegistration
import org.efradev.todolist.domain.model.DomainRegistrationResult

/**
 * Mappers for converting between Data layer DTOs and Domain models
 * 
 * These mappers ensure clean separation between data representation (DTOs)
 * and domain representation (Domain models), following Clean Architecture principles.
 */

// AuthResponse (Data) → DomainAuthData (Domain)
fun AuthResponse.toDomain(): DomainAuthData = DomainAuthData(
    user = user.toDomain(),
    accessToken = token.accessToken,
    refreshToken = token.refreshToken,
    tokenType = token.tokenType
)

// User (Data) → DomainUser (Domain)
fun User.toDomain(): DomainUser = DomainUser(
    id = id,
    username = username,
    email = email,
    firstName = firstName,
    lastName = lastName,
    phoneNumber = phoneNumber
)

// DomainUserRegistration (Domain) → RegisterRequest (Data)
fun DomainUserRegistration.toDataRequest(): RegisterRequest = RegisterRequest(
    username = username,
    password = password,
    email = email,
    firstName = firstName,
    lastName = lastName,
    phoneNumber = phoneNumber
)

// RegisterResponse (Data) → DomainRegistrationResult (Domain)
fun RegisterResponse.toDomain(): DomainRegistrationResult = DomainRegistrationResult(
    id = id.toString(),
    username = username ?: ""
)

// DomainAuthData (Domain) → AuthResponse (Data) - For saving to preferences
fun DomainAuthData.toDataResponse(): AuthResponse = AuthResponse(
    user = user.toDataUser(),
    token = Token(
        accessToken = accessToken,
        refreshToken = refreshToken,
        tokenType = tokenType
    )
)

// DomainUser (Domain) → User (Data)
fun DomainUser.toDataUser(): User = User(
    id = id,
    username = username,
    email = email,
    firstName = firstName,
    lastName = lastName,
    phoneNumber = phoneNumber
)
