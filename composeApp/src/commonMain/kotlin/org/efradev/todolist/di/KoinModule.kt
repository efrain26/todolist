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
import org.efradev.todolist.domain.StringResProvider
import org.koin.core.context.startKoin
import org.efradev.todolist.domain.provideStringResProvider
import org.koin.core.module.Module

val appModule = module {
    single {
        val json = Json { ignoreUnknownKeys = true }
        HttpClient {
            install(ContentNegotiation) {
                // TODO Fix API so it serves application/json
                json(json, contentType = ContentType.Any)
            }

//            install(logging) {
//                logger = Logger.DEFAULT
//                level = LogLevel.ALL
//            }
        }
    }
    single<StringResProvider> { provideStringResProvider() }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single { CheckUserExistsUseCase(get(), get()) }
}

val viewModelModule = module {
    factoryOf(::EmailCheckViewModel)
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
