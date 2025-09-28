package com.example.rommy_100

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rommy_100.ui.theme.AppTextStyles
import com.example.rommy_100.ui.theme.color_5
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

data class MedicalEvent(
    val id: String,
    val title: String,
    val date: LocalDate,
    val color: Color // Opcional, para distinguir tipos de eventos
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarWithEventsScreen(
    currentMonth: YearMonth,
    eventsByDate: Map<LocalDate, List<MedicalEvent>>,
    onMonthChanged: (YearMonth) -> Unit, // Callback para cuando el mes visible cambia
    onDateSelected: (LocalDate?) -> Unit,
    selectedDateForDetails: LocalDate?
) {
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val daysOfWeek = remember { daysOfWeek() }
    var eventToShowInDialog by remember { mutableStateOf<MedicalEvent?>(null) }

    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )

    // Observar cambios en el mes visible para llamar al callback
    LaunchedEffect(calendarState.firstVisibleMonth) {
        if (calendarState.firstVisibleMonth.yearMonth != currentMonth) {
            onMonthChanged(calendarState.firstVisibleMonth.yearMonth)
        }
    }

    Column(modifier = Modifier.background(color_5)) {
        Text(
            text = "${calendarState.firstVisibleMonth.yearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${calendarState.firstVisibleMonth.yearMonth.year}",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            style = AppTextStyles.titleLargeBlack
        )

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)) {
            for (dayOfWeek in daysOfWeek) {
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                    style = AppTextStyles.bodySmallBlack,
                )
            }
        }

        HorizontalCalendar(
            state = calendarState,
            dayContent = { day ->
                val dayEvents = eventsByDate[day.date].orEmpty()
                DayCellWithEvents(
                    day = day,
                    events = dayEvents,
                    isSelected = selectedDateForDetails == day.date,
                    isToday = day.date == LocalDate.now()
                ) { clickedDay ->
                    val newSelectedDate = if (selectedDateForDetails == clickedDay.date) null else clickedDay.date
                    onDateSelected(newSelectedDate)
                }
            },
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        selectedDateForDetails?.let { date ->
            val eventsForSelectedDate = eventsByDate[date].orEmpty()
            if (eventsForSelectedDate.isNotEmpty()) {
                EventDetailsList(
                    date,
                    eventsForSelectedDate,
                    onEventClick = { event ->
                    eventToShowInDialog = event // Actualiza el evento para mostrar en el diálogo
                })
            } else {
                Text("No hay eventos para $date", style = AppTextStyles.bodyLargeBlack, modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally))
            }
        }

        // Llama al Composable del diálogo si hay un evento para mostrar
        eventToShowInDialog?.let { event ->
            EventDetailDialog(
                event = event,
                onDismissRequest = { eventToShowInDialog = null }, // Cierra el diálogo
                onEditClick = {
                    // TODO: Lógica para editar el evento
                    println("Editar evento: ${event.title}")
                    eventToShowInDialog = null // Cierra el diálogo después de la acción
                },
                onDeleteClick = {
                    // TODO: Lógica para eliminar el evento
                    println("Eliminar evento: ${event.title}")
                    eventToShowInDialog = null // Cierra el diálogo después de la acción
                }
            )
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayCellWithEvents(
    day: CalendarDay,
    events: List<MedicalEvent>,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: (CalendarDay) -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f) // Celdas cuadradas
            .padding(1.dp)
            .clip(RoundedCornerShape(4.dp)) // Bordes ligeramente redondeados para la celda
            .background(
                when {
                    isSelected -> MaterialTheme.colorScheme.primaryContainer
                    isToday -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.6f)
                    day.position == DayPosition.MonthDate -> MaterialTheme.colorScheme.surfaceVariant.copy(
                        alpha = 0.3f
                    )

                    else -> Color.Transparent // Días fuera del mes
                }
            )
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) }
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(2.dp)
        ) {
            Text(
                text = day.date.dayOfMonth.toString(),
                style = AppTextStyles.bodySmallBlack,
                color = when {
                    isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
                    day.position == DayPosition.MonthDate -> MaterialTheme.colorScheme.onSurfaceVariant
                    else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                },
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
            )
            // Mostrar indicadores de eventos (puntos)
            if (events.isNotEmpty() && day.position == DayPosition.MonthDate) {
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        2.dp,
                        Alignment.CenterHorizontally
                    ),
                    modifier = Modifier.height(6.dp) // Altura fija para la fila de puntos
                ) {
                    events.take(3).forEach { event -> // Mostrar hasta 3 puntos
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(event.color)
                        )
                    }
                    if (events.size > 3) { // Si hay más de 3 eventos, un indicador adicional
                        Text(
                            "...",
                            fontSize = 6.sp,
                            lineHeight = 6.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp)) // Para mantener la altura si no hay eventos
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventDetailsList(date: LocalDate, events: List<MedicalEvent>, onEventClick: (MedicalEvent) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            "Eventos para ${date.dayOfMonth} de ${
                date.month.getDisplayName(
                    TextStyle.FULL,
                    Locale.getDefault()
                )
            }:",
            // style = MaterialTheme.typography.titleMedium,
            style = AppTextStyles.bodyLargeBlack,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        events.forEach { event ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onEventClick(event) },
                colors = CardDefaults.cardColors(containerColor = event.color.copy(alpha = 0.2f))
            ) {
                Text(
                    event.title,
                    modifier = Modifier.padding(12.dp),
                    style = AppTextStyles.bodySmallBlack
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun CalendarWithEventsScreenPreview() {
    // 1. Datos de ejemplo
    val sampleEvents = listOf(
        MedicalEvent("prev1", "Cita Pasada", LocalDate.now().minusDays(2), Color.Gray),
        MedicalEvent("today1", "Revisión Hoy", LocalDate.now(), Color.Blue),
        MedicalEvent("today2", "Vacuna Hoy", LocalDate.now(), Color.Cyan),
        MedicalEvent("future1", "Cita Futura", LocalDate.now().plusDays(3), Color.Green),
        MedicalEvent("future2", "Taller Próximo", LocalDate.now().plusDays(3), Color.Magenta),
        MedicalEvent("future3", "Charla", LocalDate.now().plusDays(7), Color.Red),
        MedicalEvent("future4", "Otra Cita", LocalDate.now().plusDays(7), Color.Yellow),
        MedicalEvent("future5", "Congreso", LocalDate.now().plusDays(7), Color.Black),
        MedicalEvent("nextmonth", "Plan Mes Siguiente", LocalDate.now().plusMonths(1).withDayOfMonth(5), Color.DarkGray)
    )

    // Estado para la preview
    var currentPreviewMonth by remember { mutableStateOf(YearMonth.now()) }
    var eventsForPreview by remember { mutableStateOf(emptyMap<LocalDate, List<MedicalEvent>>()) }
    var selectedDateInPreview by remember { mutableStateOf<LocalDate?>(null) }

    // Simular carga de eventos para el mes actual en la preview
    LaunchedEffect(currentPreviewMonth) {
        eventsForPreview = sampleEvents
            .filter { it.date.year == currentPreviewMonth.year && it.date.month == currentPreviewMonth.month }
            .groupBy { it.date }
    }

    CalendarWithEventsScreen(
        currentMonth = currentPreviewMonth,
        eventsByDate = eventsForPreview,
        onMonthChanged = { newMonth ->
            currentPreviewMonth = newMonth
            // La lógica de LaunchedEffect anterior recargará los eventos para el nuevo mes
        },
        onDateSelected = { date ->
            selectedDateInPreview = date
        },
        selectedDateForDetails = selectedDateInPreview
    )
}