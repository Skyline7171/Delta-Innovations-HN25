package com.example.rommy_100

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rommy_100.ui.theme.AppTextStyles
import com.example.rommy_100.ui.theme.color_1
import com.example.rommy_100.ui.theme.color_2
import com.example.rommy_100.ui.theme.color_5
import com.example.rommy_100.ui.theme.variation_color_11

val ButtonBackgroundColor = color_2 // Azul medio/claro para botones (0xFF3A86FF)
val TextPrimaryColor = color_5 // Blanco/casi blanco (0xFFFFFFFF o similar)
val TextSecondaryColor = variation_color_11 // Un gris azulado claro para texto secundario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Roomy: Asistente de Salud Virtual",
                        style = AppTextStyles.titleLarge.copy(color = TextPrimaryColor),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = color_2,
                ))
        },
        containerColor = color_1
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 32.dp, vertical = 16.dp), // Espaciado general
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround // Distribuye el espacio verticalmente
        ) {
            Image(
                painter = painterResource(id = R.drawable.rommy),
                contentDescription = "Asistente virtual",
            )

            // Mensaje de Bienvenida
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "¡Hola! Soy Roomy",
                    style = AppTextStyles.titleLarge.copy(
                        fontSize = 28.sp,
                        color = TextPrimaryColor
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tu asistente de salud virtual",
                    style = AppTextStyles.bodyLarge.copy(color = TextSecondaryColor),
                    textAlign = TextAlign.Center
                )
            }

            // Sección de Acciones (Botones)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp) // Espacio entre elementos de esta columna
            ) {
                Text(
                    text = "¿Primera vez aquí?",
                    style = AppTextStyles.bodyLarge.copy(color = TextPrimaryColor),
                )
                Button(
                    onClick = onRegisterClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp), // Bordes más redondeados
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonBackgroundColor)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PersonAdd,
                        contentDescription = "Registrarse",
                        tint = TextPrimaryColor
                    )
                    Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                    Text(
                        text = "Registrarse",
                        style = AppTextStyles.labelSmall.copy(
                            fontSize = 16.sp,
                            color = TextPrimaryColor
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "¿O nos conocemos?",
                    style = AppTextStyles.bodyLarge.copy(color = TextPrimaryColor),
                )
                Button(
                    onClick = onLoginClick,
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
                        text = "Iniciar Sesión",
                        style = AppTextStyles.labelSmall.copy(
                            fontSize = 16.sp,
                            color = TextPrimaryColor
                        )
                    )
                }
            }

            // Enlace de "Olvidaste tu contraseña"
            TextButton(
                onClick = onForgotPasswordClick,
                modifier = Modifier.padding(top = 8.dp) // Un poco de espacio antes de este
            ) {
                Icon(
                    imageVector = Icons.Filled.LockReset,
                    contentDescription = null, // El texto ya describe la acción
                    tint = TextSecondaryColor, // Mismo color que el texto del enlace
                    modifier = Modifier.size(18.dp) // Un poco más pequeño para el enlace
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "¿Olvidaste tu contraseña o cuenta?",
                    style = AppTextStyles.bodySmallBlack.copy(
                        color = TextSecondaryColor,
                        fontSize = 14.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(
        onRegisterClick = {},
        onLoginClick = {},
        onForgotPasswordClick = {}
    )
}