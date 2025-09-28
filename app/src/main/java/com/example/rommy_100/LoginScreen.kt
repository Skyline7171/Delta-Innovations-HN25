package com.example.rommy_100

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rommy_100.ui.theme.AppTextStyles
import com.example.rommy_100.ui.theme.Rommy100Theme
import com.example.rommy_100.ui.theme.color_2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateBack: () -> Unit,
    onLoginClick: (String, String) -> Unit, // email, password
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = ScreenBackgroundColor,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Iniciar Sesión",
                        style = AppTextStyles.titleLarge.copy(color = TextPrimaryColor)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = TextPrimaryColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = color_2
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 32.dp, vertical = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically) // Centra verticalmente si hay espacio
        ) {

            Spacer(modifier = Modifier.height(16.dp)) // Espacio superior

            Image(
                painter = painterResource(id = R.drawable.rommy), // Reutiliza el logo
                contentDescription = "Logo Roomy",
                // modifier = Modifier.size(120.dp) // Ajusta el tamaño si lo ves necesario
            )

            Text(
                text = "¡Qué bueno verte de nuevo!",
                style = AppTextStyles.titleLarge.copy(color = TextPrimaryColor, fontSize = 20.sp),
                textAlign = TextAlign.Center
            )

            // Campo Correo Electrónico
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; emailError = null },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Correo Electrónico", style = AppTextStyles.bodyLarge.copy(color = TextSecondaryColor)) },
                leadingIcon = { // Icono al inicio del campo
                    Icon(
                        Icons.Filled.Email,
                        contentDescription = "Icono de correo electrónico",
                        tint = TextSecondaryColor
                    )
                },
                singleLine = true,
                textStyle = AppTextStyles.bodyLarge.copy(color = TextPrimaryColor),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = ButtonBackgroundColor,
                    unfocusedIndicatorColor = TextSecondaryColor,
                    cursorColor = ButtonBackgroundColor,
                    errorIndicatorColor = MaterialTheme.colorScheme.error,
                    focusedLabelColor = ButtonBackgroundColor,
                    unfocusedLabelColor = TextSecondaryColor,
                    errorLabelColor = MaterialTheme.colorScheme.error
                ),
                isError = emailError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            emailError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }

            // Campo Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; passwordError = null },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Contraseña", style = AppTextStyles.bodyLarge.copy(color = TextSecondaryColor)) },
                leadingIcon = { // Icono al inicio del campo
                    Icon(
                        Icons.Filled.Lock,
                        contentDescription = "Icono de contraseña",
                        tint = TextSecondaryColor
                    )
                },
                singleLine = true,
                textStyle = AppTextStyles.bodyLarge.copy(color = TextPrimaryColor),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description, tint = TextSecondaryColor)
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = ButtonBackgroundColor,
                    unfocusedIndicatorColor = TextSecondaryColor,
                    cursorColor = ButtonBackgroundColor,
                    errorIndicatorColor = MaterialTheme.colorScheme.error,
                    focusedLabelColor = ButtonBackgroundColor,
                    unfocusedLabelColor = TextSecondaryColor,
                    errorLabelColor = MaterialTheme.colorScheme.error
                ),
                isError = passwordError != null
            )
            passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }

            // Enlace ¿Olvidaste tu contraseña?
            TextButton(
                onClick = onForgotPasswordClick,
                modifier = Modifier.align(Alignment.End) // Alinea a la derecha
            ) {
                Text(
                    "¿Olvidaste tu contraseña?",
                    style = AppTextStyles.bodySmallBlack.copy(color = TextSecondaryColor, fontSize = 14.sp),
                )
            }

            Spacer(modifier = Modifier.height(8.dp)) // Espacio antes del botón

            // Botón Iniciar Sesión
            Button(
                onClick = {
                    var isValid = true
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        emailError = "Correo electrónico inválido"; isValid = false
                    }
                    if (password.isEmpty()) {
                        passwordError = "La contraseña no puede estar vacía"; isValid = false
                    }
                    if (isValid) {
                        onLoginClick(email, password)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonBackgroundColor)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Login,
                    contentDescription = "Iniciar Sesión",
                    tint = TextPrimaryColor
                )
                Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                Text(
                    "Iniciar Sesión",
                    style = AppTextStyles.labelSmall.copy(fontSize = 16.sp, color = TextPrimaryColor)
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // Empuja el siguiente TextButton hacia abajo

            // Enlace ¿No tienes cuenta?
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp) // Un poco de padding inferior
            ) {
                Text(
                    "¿No tienes una cuenta?",
                    style = AppTextStyles.bodyLarge.copy(color = TextSecondaryColor)
                )
                TextButton(onClick = onRegisterClick) {
                    Text(
                        "Regístrate",
                        style = AppTextStyles.bodyLarge.copy(color = ButtonBackgroundColor), // Destaca el enlace
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onNavigateBack = {},
        onLoginClick = { _, _ -> },
        onRegisterClick = {},
        onForgotPasswordClick = {}
    )
}
