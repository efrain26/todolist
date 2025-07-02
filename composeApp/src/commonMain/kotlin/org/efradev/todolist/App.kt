package org.efradev.todolist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.efradev.todolist.di.initKoin
import org.efradev.todolist.ui.screens.ShoppingListsScreen
import org.efradev.todolist.ui.screens.ShoppingListDetailsScreen
import org.efradev.todolist.ui.screens.EmailForm
import org.efradev.todolist.ui.screens.LoginForm
import org.efradev.todolist.ui.screens.RegisterForm
import org.efradev.todolist.ui.screens.SplashScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.efradev.todolist.viewmodel.EmailCheckViewModel
import org.efradev.todolist.viewmodel.EmailCheckState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    MaterialTheme {
        var screen by remember { mutableStateOf("splash") } // Cambiado a "splash" como estado inicial
        var email by remember { mutableStateOf("") }
        var selectedListId by remember { mutableStateOf("") }
        val viewModel: EmailCheckViewModel = koinViewModel<EmailCheckViewModel>()
        val state = viewModel.state

        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (screen) {
                "splash" -> SplashScreen(
                    onAuthenticated = { screen = "shopping_lists" },
                    onNotAuthenticated = { screen = "email" }
                )

                "email" -> EmailForm(
                    onSubmit = { enteredEmail ->
                        email = enteredEmail
                        viewModel.checkEmail(email)
                    },
                    isLoading = state is EmailCheckState.Loading,
                    errorMessage = (state as? EmailCheckState.Error)?.message
                        ?: (state as? EmailCheckState.Error)?.message
                )

                "register" -> RegisterForm(
                    email = email,
                    onBack = { screen = "email" },
                    onRegisterSuccess = { screen = "login" }
                )

                "login" -> LoginForm(
                    email = email,
                    onBack = { screen = "email" },
                    onLoginSuccess = { screen = "shopping_lists" }
                )

                "shopping_lists" -> ShoppingListsScreen(
                    onLogout = { screen = "email" },
                    onListClick = { listId ->
                        selectedListId = listId
                        screen = "list_details"
                    }
                )

                "list_details" -> ShoppingListDetailsScreen(
                    listId = selectedListId,
                    onBackClick = { screen = "shopping_lists" },
                    onListDeleted = { screen = "shopping_lists" }
                )
            }

            // Navegación automática según el estado
            LaunchedEffect(state) {
                when (state) {
                    is EmailCheckState.Success -> {
                        if (state.isRegistered) {
                            screen = "login"
                        } else {
                            screen = "register"
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    initKoin()
    App(
    )
}