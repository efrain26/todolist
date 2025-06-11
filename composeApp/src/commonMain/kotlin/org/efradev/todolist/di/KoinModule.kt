package org.efradev.todolist.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import org.koin.dsl.module
import org.efradev.todolist.data.UserRepository
import org.efradev.todolist.data.UserRepositoryImpl
import org.efradev.todolist.domain.CheckUserExistsUseCase
import org.efradev.todolist.viewmodel.EmailCheckViewModel
import org.koin.core.module.dsl.factoryOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.efradev.todolist.domain.LoginUseCase
import org.efradev.todolist.domain.StringResProvider
import org.koin.core.context.startKoin
import org.efradev.todolist.domain.provideStringResProvider
import org.koin.core.module.Module
import org.efradev.todolist.domain.RegisterUserUseCase
import org.efradev.todolist.viewmodel.LoginViewModel
import org.efradev.todolist.viewmodel.RegisterViewModel
import org.efradev.todolist.data.AuthLocalStorage
import org.efradev.todolist.data.AuthLocalStorageImpl
import org.efradev.todolist.getPlatform

val appModule = module {
    single {
        val json = Json { ignoreUnknownKeys = true }
        HttpClient {
            install(ContentNegotiation) {
                // TODO Fix API so it serves application/json
                json(json, contentType = ContentType.Any)
            }
        }
    }
    single<StringResProvider> { provideStringResProvider() }
    single { getPlatform().createSettings() }
    single<AuthLocalStorage> { AuthLocalStorageImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single { CheckUserExistsUseCase(get(), get()) }
    single { RegisterUserUseCase(get(), get()) }
    single { LoginUseCase(get(), get()) }
}

val viewModelModule = module {
    factoryOf(::EmailCheckViewModel)
    factoryOf(::RegisterViewModel)
    factoryOf(::LoginViewModel)

}

fun initKoin() {
    startKoin {
        modules(
            appModule,
            viewModelModule
        )
    }
}


fun initKoinWithoutContext(): List<Module> {
    return listOf(
        appModule,
        viewModelModule
    )
}
