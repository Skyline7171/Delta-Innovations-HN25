package com.example.rommy_100

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Sos
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Sos
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.rommy_100.ui.theme.AppTextStyles
import com.example.rommy_100.ui.theme.color_1
import com.example.rommy_100.ui.theme.color_2
import com.example.rommy_100.ui.theme.color_3

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }
}

// Clase de datos para representar cada elemento de la barra de navegación
data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean, // Ejemplo de "badge" o notificación
    val tint: Color,
    val badgeCount: Int? = null,
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class) // Necesario para BadgedBox y algunos parámetros de NavigationBarItem
@Composable
fun MainScreen(navController: NavController) {
    // Lista de elementos para la barra de navegación
    val items = listOf(
        BottomNavigationItem(
            title = "Inicio",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            hasNews = false,
            tint = color_3,
        ),
        BottomNavigationItem(
            title = "Protocolos",
            selectedIcon = Icons.AutoMirrored.Filled.Assignment,
            unselectedIcon = Icons.AutoMirrored.Outlined.Assignment,
            hasNews = false,
            tint = color_3,
            badgeCount = 23, // Mostrar un número en el badge
        ),
        BottomNavigationItem(
            title = "SOS",
            selectedIcon = Icons.Filled.Sos,
            unselectedIcon = Icons.Outlined.Sos,
            hasNews = false,
            tint = color_3
        ),
        BottomNavigationItem(
            title = "Eventos",
            selectedIcon = Icons.Filled.DateRange,
            unselectedIcon = Icons.Outlined.DateRange,
            hasNews = true, // Mostrar un punto de notificación
            tint = color_3
        )
    )

    // Estado para recordar el ítem seleccionado
    var selectedItemIndex by remember { mutableIntStateOf(0) }
    var selectedItemTitle by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = { Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menú",
                    tint = color_3
                ) },
                title = { Text(selectedItemTitle, style = AppTextStyles.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = color_2, // Tu color deseado para el fondo
            ))
        },
        bottomBar = {
            NavigationBar(
                containerColor = color_2,
            ) { // Este es el contenedor de la barra de navegación inferior
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            selectedItemTitle = item.title
                            // Aquí manejarías la navegación o la acción al hacer clic
                            // Por ejemplo: navController.navigate(item.route)
                        },
                        label = { Text(text = item.title, style = AppTextStyles.labelSmall) },
                        icon = {
                            BadgedBox( // Para mostrar notificaciones (punto o número)
                                badge = {
                                    if (item.badgeCount != null) {
                                        Badge { Text(item.badgeCount.toString()) }
                                    } else if (item.hasNews) {
                                        Badge() // Un simple punto
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (selectedItemIndex == index) {
                                        item.selectedIcon
                                    } else {
                                        item.unselectedIcon
                                    },
                                    contentDescription = item.title,
                                    tint = item.tint
                                )
                            }
                        }
                        // Opcional: alwaysShowLabel = false (para ocultar etiquetas de ítems no seleccionados si hay muchos)
                    )
                }
            }
        }
    ) { innerPadding ->
        // Contenido principal de tu pantalla, que cambiaría según el ítem seleccionado
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(color_1)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (selectedItemIndex) {
                0 -> {
                    Image(
                        painter = painterResource(id = R.drawable.rommy),
                        contentDescription = "Asistente virtual",
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                onClick = {
                                    navController.navigate(AppDestinations.ASSISTANT_ROUTE)
                                },
                                role = Role.Button,
                                interactionSource = remember { MutableInteractionSource() },
                            )
                    )
                }
                3 -> {
                    // EventCalendarScreen(innerPadding)
                    EventCalendarScreen()
                }
                else -> {
                    Text(text = "Contenido de: ${items[selectedItemIndex].title}",style = AppTextStyles.bodyLarge)
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppNavigation()
}