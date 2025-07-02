package org.efradev.todolist.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import org.koin.dsl.module
import org.efradev.todolist.domain.repository.UserRepository
import org.efradev.todolist.data.UserRepositoryImpl
import org.efradev.todolist.domain.CheckUserExistsUseCase
import org.efradev.todolist.viewmodel.EmailCheckViewModel
import org.koin.core.module.dsl.factoryOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.efradev.todolist.domain.LoginUseCase
import org.koin.core.context.startKoin
import org.efradev.todolist.domain.provideStringResProvider
import org.koin.core.module.Module
import org.efradev.todolist.domain.RegisterUserUseCase
import org.efradev.todolist.viewmodel.LoginViewModel
import org.efradev.todolist.viewmodel.RegisterViewModel
import org.efradev.todolist.data.local.AuthLocalStorage
import org.efradev.todolist.data.local.AuthLocalStorageImpl
import org.efradev.todolist.getPlatform
import org.efradev.todolist.domain.repository.ShoppingListRepository
import org.efradev.todolist.data.ShoppingListRepositoryImpl
import org.efradev.todolist.domain.GetShoppingListsUseCase
import org.efradev.todolist.viewmodel.ShoppingListsViewModel
import org.efradev.todolist.domain.repository.PreferencesRepository
import org.efradev.todolist.data.local.PreferencesRepositoryImpl
import org.efradev.todolist.domain.CheckAuthStateUseCase
import org.efradev.todolist.viewmodel.AuthViewModel
import org.efradev.todolist.domain.LogoutUseCase
import org.efradev.todolist.viewmodel.ProfileViewModel
import org.efradev.todolist.data.auth.AuthManager
import org.efradev.todolist.domain.CreateShoppingListUseCase
import org.efradev.todolist.viewmodel.CreateListViewModel
import org.efradev.todolist.domain.GetShoppingListDetailsUseCase
import org.efradev.todolist.domain.usecase.AddItemToListUseCase
import org.efradev.todolist.domain.usecase.DeleteShoppingListUseCase
import org.efradev.todolist.viewmodel.ShoppingListDetailsViewModel
import org.koin.core.qualifier.named

val appModule = module {
    // Json configuration
    single { Json { ignoreUnknownKeys = true } }

    // HttpClient instances
    single(qualifier = named("authClient")) {
        HttpClientFactory.create(useTokenAuthenticator = false, json = get())
    }

    single { AuthManager(get(qualifier = named("authClient")), get()) }

    single { HttpClientFactory.create(json = get()) }

    single { provideStringResProvider() }
    single { getPlatform().createSettings() }

    //Repositories
    single<AuthLocalStorage> { AuthLocalStorageImpl(get()) }
    single<UserRepository>{ UserRepositoryImpl(get(), get()) }
    single<ShoppingListRepository> { ShoppingListRepositoryImpl(get(), get()) }

    // Preferences Repository
    single<PreferencesRepository> {
        PreferencesRepositoryImpl(
            settings = get(),
            json = Json { ignoreUnknownKeys = true }
        )
    }

    //Use Cases
    single { CheckUserExistsUseCase(get(), get()) }
    single { CheckAuthStateUseCase(get()) }
    single { RegisterUserUseCase(get(), get()) }
    single { LoginUseCase(get(), get(), get()) }
    single { GetShoppingListsUseCase(get(), get()) }
    single { CreateShoppingListUseCase(get()) }
    single { GetShoppingListDetailsUseCase(get()) }
    single { AddItemToListUseCase(get()) }
    single { DeleteShoppingListUseCase(get()) }
    single { LogoutUseCase(get(), get()) }
}

val viewModelModule = module {
    factoryOf(::EmailCheckViewModel)
    factoryOf(::RegisterViewModel)
    factoryOf(::LoginViewModel)
    factoryOf(::ShoppingListsViewModel)
    factoryOf(::AuthViewModel)
    factoryOf(::ProfileViewModel)
    factoryOf(::CreateListViewModel)
    factory { ShoppingListDetailsViewModel(get(), get(), get()) }
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
