package org.efradev.todolist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.efradev.todolist.viewmodel.RegisterState
import org.efradev.todolist.viewmodel.RegisterViewModel
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun RegisterForm(email: String, onBack: () -> Unit, onRegisterSuccess: () -> Unit) {
    val viewModel: RegisterViewModel = koinViewModel<RegisterViewModel>()
    var username by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    var firstName by remember { mutableStateOf(TextFieldValue()) }
    var lastName by remember { mutableStateOf(TextFieldValue()) }
    var phoneNumber by remember { mutableStateOf(TextFieldValue()) }

    // Estados para los errores
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var firstNameError by remember { mutableStateOf<String?>(null) }
    var lastNameError by remember { mutableStateOf<String?>(null) }
    var phoneNumberError by remember { mutableStateOf<String?>(null) }

    // Funciones de validación
    fun validateUsername(value: String) {
        usernameError = when {
            value.isBlank() -> "El nombre de usuario es requerido"
            value.length < 3 -> "El nombre de usuario debe tener al menos 3 caracteres"
            else -> null
        }
    }

    fun validatePassword(value: String) {
        passwordError = when {
            value.isBlank() -> "La contraseña es requerida"
            value.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            !value.any { it.isDigit() } -> "La contraseña debe contener al menos un número"
            !value.any { it.isUpperCase() } -> "La contraseña debe contener al menos una mayúscula"
            else -> null
        }
    }

    fun validateName(value: String, field: String): String? = when {
        value.isBlank() -> "$field es requerido"
        !value.all { it.isLetter() || it.isWhitespace() } -> "$field solo debe contener letras"
        else -> null
    }

    fun validatePhone(value: String) {
        phoneNumberError = when {
            value.isBlank() -> "El número de teléfono es requerido"
            !value.all { it.isDigit() } -> "El número de teléfono solo debe contener números"
            value.length < 10 -> "El número de teléfono debe tener al menos 10 dígitos"
            else -> null
        }
    }

    fun validateForm(): Boolean {
        validateUsername(username.text)
        validatePassword(password.text)
        firstNameError = validateName(firstName.text, "El nombre")
        lastNameError = validateName(lastName.text, "El apellido")
        validatePhone(phoneNumber.text)

        return usernameError == null && passwordError == null &&
                firstNameError == null && lastNameError == null &&
                phoneNumberError == null
    }

    // Observar estado del registro
    LaunchedEffect(viewModel.state) {
        when (val state = viewModel.state) {
            is RegisterState.Success -> {
                onRegisterSuccess()
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Registro",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = username.text,
                    onValueChange = {
                        username = TextFieldValue(it)
                        validateUsername(it)
                    },
                    label = { Text("Nombre de usuario") },
                    singleLine = true,
                    isError = usernameError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (usernameError != null) {
                    Text(
                        text = usernameError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = password.text,
                    onValueChange = {
                        password = TextFieldValue(it)
                        validatePassword(it)
                    },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    isError = passwordError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (passwordError != null) {
                    Text(
                        text = passwordError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = firstName.text,
                    onValueChange = {
                        firstName = TextFieldValue(it)
                        firstNameError = validateName(it, "El nombre")
                    },
                    label = { Text("Nombre") },
                    singleLine = true,
                    isError = firstNameError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (firstNameError != null) {
                    Text(
                        text = firstNameError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = lastName.text,
                    onValueChange = {
                        lastName = TextFieldValue(it)
                        lastNameError = validateName(it, "El apellido")
                    },
                    label = { Text("Apellido") },
                    singleLine = true,
                    isError = lastNameError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (lastNameError != null) {
                    Text(
                        text = lastNameError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = phoneNumber.text,
                    onValueChange = {
                        phoneNumber = TextFieldValue(it)
                        validatePhone(it)
                    },
                    label = { Text("Número de teléfono") },
                    singleLine = true,
                    isError = phoneNumberError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (phoneNumberError != null) {
                    Text(
                        text = phoneNumberError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            OutlinedTextField(
                value = email,
                onValueChange = {},
                label = { Text("Email") },
                singleLine = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            if (viewModel.state is RegisterState.Error) {
                Text(
                    text = (viewModel.state as RegisterState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onBack,
                    modifier = Modifier.weight(1f),
                    enabled = viewModel.state !is RegisterState.Loading
                ) {
                    Text("Volver")
                }

                Button(
                    onClick = {
                        if (validateForm()) {
                            viewModel.register(
                                username = username.text,
                                password = password.text,
                                email = email,
                                firstName = firstName.text,
                                lastName = lastName.text,
                                phoneNumber = phoneNumber.text
                            )
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = viewModel.state !is RegisterState.Loading
                ) {
                    if (viewModel.state is RegisterState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Registrar")
                    }
                }
            }
        }
    }
}