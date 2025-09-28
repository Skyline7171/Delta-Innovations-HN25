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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.rommy_100.ui.theme.AppTextStyles

// Colores para el diálogo
val DialogHeaderBlue = Color(0xFF2962FF) // Azul similar al de la imagen
val DialogBackgroundDark = Color(0xFF212121) // Un gris oscuro para el cuerpo
val DialogTextColorLight = Color.White
val DialogTextColorMedium = Color(0xB3FFFFFF) // Blanco con algo de transparencia para subtítulos
val DialogDividerColor = Color(0x4DFFFFFF) // Blanco con más transparencia para divisores

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventDetailDialog(
    event: MedicalEvent,
    onDismissRequest: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {// Estado para el Switch
    var alarmEnabled by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DialogBackgroundDark),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                // Sección Superior Azul
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DialogHeaderBlue)
                        .padding(horizontal = 24.dp, vertical = 32.dp) // Más padding vertical
                ) {
                    Column {
                        Text(
                            text = event.title,
                            color = DialogTextColorLight,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            style = AppTextStyles.titleLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Hoy a las ${
                                event.date.atTime(1, 14).toLocalTime()
                            } - ${event.date.atTime(23, 0).toLocalTime()}", // Ejemplo de hora
                            style = AppTextStyles.bodySmallBlack
                        )
                    }
                }

                // Sección Inferior Oscura con Opciones
                Column(modifier = Modifier.padding(
                    horizontal = 24.dp,
                    vertical = 16.dp
                )) {
                    DialogRowItem(
                        label = "Cuenta",
                        value = "aaronjimenezr2004@gmail.com", // Ejemplo
                        onClick = { /* TODO: Acción al hacer clic en cuenta */ }
                    )
                    DialogDivider()
                    DialogRowItem(
                        label = "Recordatorios",
                        value = "5 minutos antes del evento", // Ejemplo
                        onClick = { /* TODO: Acción al hacer clic en recordatorios */ }
                    )
                    DialogDivider()
                    DialogSwitchItem(
                        label = "Recordatorios con alarma",
                        checked = alarmEnabled,
                        onCheckedChange = { alarmEnabled = it }
                    )
                }

                // Barra Inferior con Iconos
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween // Alineados a la izquierda en la imagen
                ) {
                    IconButton(onClick = onEditClick) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Editar evento",
                            tint = DialogTextColorMedium
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "Eliminar evento",
                            tint = DialogTextColorMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogRowItem(label: String, value: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        // horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = DialogTextColorLight, fontSize = 16.sp)

        Spacer(modifier = Modifier.width(8.dp)) // Espacio mínimo garantizado

        Text(
            text = value,
            color = DialogTextColorMedium,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f),
            textAlign = androidx.compose.ui.text.style.TextAlign.End,
            maxLines = 1,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.width(4.dp)) // Espacio entre el valor y el icono

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = DialogTextColorMedium,
            modifier = Modifier.size(24.dp)
        )
        /*
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = value, color = DialogTextColorMedium, fontSize = 16.sp)
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, // O Icons.Filled.KeyboardArrowRight
                contentDescription = null,
                tint = DialogTextColorMedium,
                modifier = Modifier.size(24.dp)
            )
        }
        */
    }
}

@Composable
private fun DialogSwitchItem(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), // Menos padding vertical para Switch
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = DialogTextColorLight, fontSize = 16.sp)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = DialogHeaderBlue,
                checkedTrackColor = DialogHeaderBlue.copy(alpha = 0.5f),
                uncheckedThumbColor = DialogTextColorMedium,
                uncheckedTrackColor = DialogTextColorMedium.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
private fun DialogDivider() {
    HorizontalDivider(thickness = 0.5.dp, color = DialogDividerColor.copy(alpha = 0.2f))
}

