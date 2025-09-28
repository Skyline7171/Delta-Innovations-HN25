package com.example.rommy_100

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rommy_100.ui.theme.AppTextStyles
import com.example.rommy_100.ui.theme.color_1

val MediumBlueBackground = Color(0xFF3F51B5) // Un azul medio para las secciones
val CardBackground = Color(0xFFE8EAF6) // Un color claro para el contenido de las tarjetas

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        style = AppTextStyles.titleLarge,
    )
}

@Composable
fun WeeklyEventsPlaceholder() {
    // Placeholder para la fila de eventos semanales
    // En una implementación real, usarías LazyRow con elementos de eventos reales
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(3) { // Simula 3 tarjetas de eventos
            EventCardPlaceholder(modifier = Modifier
                .width(150.dp)
                .height(80.dp))
        }
    }
}

@Composable
fun TodayEventPlaceholder() {
    // Placeholder para la tarjeta del evento de hoy
    EventCardPlaceholder(modifier = Modifier
        .fillMaxWidth()
        .height(120.dp))
}

@Composable
fun EventCardPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MediumBlueBackground) // Fondo azul medio para la tarjeta
            .padding(
                start = 6.dp,
                end = 6.dp,
                top = 6.dp,
                bottom = 6.dp
            ) // Padding para el borde más claro
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(6.dp)) // Redondeo interior
                .background(CardBackground) // Color claro para el contenido
        ) {
            // Aquí iría el contenido real de la tarjeta del evento
            // Por ahora, lo dejamos vacío para el diseño
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
// Comentado porque no me convence el padding que otorga Scaffold
fun EventCalendarScreen(/*innerPadding: PaddingValues*/) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            // .padding(innerPadding) // Aplica el padding del Scaffold
            .background(color_1) // Asegura que el fondo sea el correcto
            .verticalScroll(scrollState)
            .padding(16.dp)
            // .padding(start = 16.dp, end = 16.dp)
    ) {
        // Sección "Mes actual"
        SectionTitle("Mes actual")
        Spacer(modifier = Modifier.height(8.dp))
        // <-------------------------->
        CalendarWithEventsScreenPreview()
        // <-------------------------->
        Spacer(modifier = Modifier.height(24.dp))

        // Sección "Eventos de esta semana"
        SectionTitle("Eventos de esta semana:")
        Spacer(modifier = Modifier.height(8.dp))
        WeeklyEventsPlaceholder() // Placeholder para los eventos de la semana
        Spacer(modifier = Modifier.height(12.dp))

        // Sección "Hoy"
        SectionTitle("Hoy")
        Spacer(modifier = Modifier.height(8.dp))
        TodayEventPlaceholder() // Placeholder para el evento de hoy
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun EventCalendarScreenPreview() {
    EventCalendarScreen(/*innerPadding = PaddingValues(0.dp)*/)
}