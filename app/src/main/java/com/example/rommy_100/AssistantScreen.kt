package com.example.rommy_100

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rommy_100.ui.theme.AppTextStyles
import com.example.rommy_100.ui.theme.color_3
import com.example.rommy_100.ui.theme.color_5
import com.example.rommy_100.ui.theme.color_6
import com.example.rommy_100.ui.theme.variation_color_10
import com.example.rommy_100.ui.theme.variation_color_7
import com.example.rommy_100.ui.theme.variation_color_8
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ChatMessage(
    val id: String,
    val sender: Sender,
    val content: MessageContentType,
    val timestamp: String,
    val avatarRes: Int? = null,
    val avatarIcon: ImageVector? = null
)

enum class Sender { USER, AI }
sealed class MessageContentTypeTest {
    data class Text(val text: String) : MessageContentType()
    data class Code(val code: String, val lang: String = "kotlin") : MessageContentType()
    data class Image(val painterRes: Int) :
        MessageContentType()

    data class Actions(val actions: List<String>) : MessageContentType()
}
sealed class MessageContentType {
    data class Text(val text: String) : MessageContentType()
    data class Code(val code: String, val lang: String = "kotlin") : MessageContentType()
    data class Image(val painterRes: Int) :
        MessageContentType()

    data class Actions(val actions: List<String>) : MessageContentType()
}

val AiCodeBackgroundColor = Color(0xFF2B2B2B)

// Función para obtener la hora actual formateada
fun formatTimestamp(date: Date, locale: Locale): String { // Aquí sí se espera java.util.Locale
    val sdf = SimpleDateFormat("hh:mm a", locale)
    return sdf.format(date)
}

@Composable
fun CodeBlockItem(code: String, language: String, textColor: Color) {
    Surface(
        color = AiCodeBackgroundColor,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Text(
                text = code.trimIndent(),
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.87f),
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, top = 10.dp, bottom = 4.dp)
                    .horizontalScroll(rememberScrollState()) // Para scroll horizontal si el código es largo
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    language.uppercase(),
                    fontSize = 10.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
                TextButton(onClick = { /* Copiar código */ }) {
                    Icon(
                        Icons.Filled.ContentCopy,
                        contentDescription = "Copiar código",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Copiar", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun AiChatInputBar(
    textValue: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    onSend: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = variation_color_8,
        shadowElevation = 3.dp // Sensación de elevación
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Bottom // Para alinear con textfield multilínea
        ) {
            TextField(
                value = textValue,
                onValueChange = onTextChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                placeholder = {
                    Text(
                        "Pregúntame algo...",
                        style = AppTextStyles.bodyLargeGrey,
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = variation_color_10,
                    unfocusedTextColor = variation_color_10,
                    focusedContainerColor = color_5,
                    unfocusedContainerColor = color_5,
                    // disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    // cursorColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = Color.Transparent, // Sin línea indicadora para un look más limpio
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(24.dp),
                maxLines = 5,
                minLines = 1,
                trailingIcon = {
                    IconButton(onClick = onSend) {
                        Icon(
                            imageVector = Icons.Filled.ArrowUpward,
                            contentDescription = "Enviar",
                            tint = color_3
                        )
                    }
                },
                textStyle = AppTextStyles.bodyLargeGrey,
            )

            val isTextEmpty = textValue.text.isBlank()
        }
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage) {
    val horizontalArrangement =
        if (message.sender == Sender.USER) Arrangement.End else Arrangement.Start
    val bubbleColor =
        if (message.sender == Sender.USER) MaterialTheme.colorScheme.primaryContainer else variation_color_8
    val textColor =
        if (message.sender == Sender.USER) MaterialTheme.colorScheme.onPrimaryContainer else color_5
    val tailSize = 8.dp
    val cornerRadius = 16.dp

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.Bottom // Para alinear avatar con la burbuja
    ) {
        // Avatar de la IA
        if (message.sender == Sender.AI && message.avatarRes != null) {
            Image(
                painter = painterResource(id = message.avatarRes),
                contentDescription = "Avatar IA",
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.Bottom) // Asegura que esté abajo si el mensaje es alto
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        // Burbuja de mensaje
        Box(
            modifier = Modifier
                .weight(
                    1f,
                    fill = false
                ) // Para que no ocupe más de lo necesario (aprox 70-80% max)
                .widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.75f)
                .clip(
                    RoundedCornerShape(
                        topStart = cornerRadius,
                        topEnd = cornerRadius,
                        bottomStart = if (message.sender == Sender.USER && horizontalArrangement == Arrangement.End) cornerRadius else if (message.sender == Sender.AI && horizontalArrangement == Arrangement.Start) 0.dp else cornerRadius,
                        bottomEnd = if (message.sender == Sender.USER && horizontalArrangement == Arrangement.End) 0.dp else if (message.sender == Sender.AI && horizontalArrangement == Arrangement.Start) cornerRadius else cornerRadius,
                    )
                )
                .background(bubbleColor)
                .drawBehind { // Dibuja la "cola" de la burbuja
                    if (message.sender == Sender.USER && horizontalArrangement == Arrangement.End) {
                        val path = Path().apply {
                            moveTo(size.width, size.height - tailSize.toPx() - cornerRadius.toPx())
                            lineTo(size.width, size.height)
                            lineTo(
                                size.width - tailSize.toPx() - cornerRadius.toPx() / 2,
                                size.height - tailSize.toPx()
                            )
                            close()
                        }
                        drawPath(path, color = bubbleColor)
                    } else if (message.sender == Sender.AI && horizontalArrangement == Arrangement.Start) {
                        val path = Path().apply {
                            moveTo(0f, size.height - tailSize.toPx() - cornerRadius.toPx())
                            lineTo(0f, size.height)
                            lineTo(
                                tailSize.toPx() + cornerRadius.toPx() / 2,
                                size.height - tailSize.toPx()
                            )
                            close()
                        }
                        drawPath(path, color = bubbleColor)
                    }
                }
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Column {
                when (val content = message.content) {
                    is MessageContentType.Text -> Text(
                        content.text,
                        color = textColor,
                        style = AppTextStyles.bodyLarge
                    )

                    is MessageContentType.Code -> CodeBlockItem(
                        content.code,
                        content.lang,
                        textColor = MaterialTheme.colorScheme.onSurface
                    )

                    is MessageContentType.Image -> Image(
                        painter = painterResource(id = content.painterRes),
                        contentDescription = "Imagen adjunta",
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )

                    is MessageContentType.Actions -> Column(
                        verticalArrangement = Arrangement.spacedBy(
                            6.dp
                        )
                    ) {
                        content.actions.forEach { actionText ->
                            OutlinedButton(
                                onClick = { /* Acción del botón */ },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                                border = BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                )
                            ) {
                                Text(actionText, fontSize = 14.sp)
                            }
                        }
                    }

                    is MessageContentTypeTest.Actions -> TODO()
                    is MessageContentTypeTest.Code -> TODO()
                    is MessageContentTypeTest.Image -> TODO()
                    is MessageContentTypeTest.Text -> TODO()
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    message.timestamp,
                    color = textColor.copy(alpha = 0.7f),
                    style = AppTextStyles.labelSmall,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }

        // Avatar del Usuario
        if (message.sender == Sender.USER && (message.avatarRes != null || message.avatarIcon != null)) {
            Spacer(modifier = Modifier.width(8.dp))
            if (message.avatarRes != null) {
                Image(
                    painter = painterResource(id = message.avatarRes),
                    contentDescription = "Avatar Usuario",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                )
            }
            else {
                message.avatarIcon.let {
                    if (it != null) {
                        Icon(
                            imageVector = it,
                            contentDescription = "Avatar Usuario",
                            tint = variation_color_8,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AiTypingIndicatorItem(avatarRes: Int) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = avatarRes),
            contentDescription = "Avatar IA",
            modifier = Modifier
                .size(32.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))

        // Animación de puntos
        Row(verticalAlignment = Alignment.CenterVertically) {
            (1..3).forEach { index ->
                val delay = index * 200
                val infiniteTransition = rememberInfiniteTransition(label = "typing_dots")
                val alpha by infiniteTransition.animateFloat(
                    initialValue = 0.3f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = keyframes {
                            durationMillis = 1200
                            0.3f at 0 with LinearEasing
                            1f at (300 + delay) with LinearEasing
                            0.3f at (600 + delay) with LinearEasing
                            0.3f at 1200 with LinearEasing
                        },
                        repeatMode = RepeatMode.Restart
                    ), label = "dot_alpha_$index"
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(variation_color_8.copy(alpha = alpha))
                )
            }
        }
        Text(
            " Rommy está pensando...",
            style = AppTextStyles.labelSmall.copy(fontStyle = FontStyle.Italic)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsisstantScreen(onNavigateBack: () -> Unit) {
    val currentSystemLocale = LocalConfiguration.current.locales[0]
    val timestamp = formatTimestamp(Date(), currentSystemLocale)

    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                "1",
                Sender.AI,
                MessageContentType.Text("¡Hola! Soy tu asistente de IA. ¿En qué puedo ayudarte hoy?"),
                timestamp,
                R.drawable.rommy
            )
        )
    }
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope() // Para lanzar coroutines
    var isAiTyping by remember { mutableStateOf(false) } // Estado para el indicador de escritura

    // Para scroll automático al nuevo mensaje
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.rommy),
                            contentDescription = "Avatar IA",
                            modifier = Modifier
                                .size(42.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Roomy", style = AppTextStyles.titleLarge)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = color_6)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Acción Más Opciones */ }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Más Opciones", tint = color_6)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = variation_color_8)
            )
        },
        bottomBar = {
            AiChatInputBar(
                textValue = inputText,
                onTextChange = { inputText = it },
                onSend = {
                    val userMessageText = inputText.text.trim()
                    if (userMessageText.isNotBlank()) {
                        val timestamp = formatTimestamp(Date(), currentSystemLocale)
                        // Añadir mensaje del usuario
                        messages.add(
                            ChatMessage(
                                (messages.size + 1).toString(),
                                Sender.USER,
                                MessageContentType.Text(userMessageText),
                                timestamp,
                                null,
                                Icons.Filled.AccountCircle
                            )
                        )
                        inputText = TextFieldValue("")
                        isAiTyping = true // Mostrar indicador de escritura

                        // Lanzar coroutine para obtener respuesta de la IA
                        coroutineScope.launch {
                            val result = OpenRouterApiService.getChatCompletion(userMessageText)
                            val timestamp = formatTimestamp(Date(), currentSystemLocale)
                            isAiTyping = false // Ocultar indicador
                            result.fold(
                                onSuccess = { aiReply ->
                                    messages.add(
                                        ChatMessage(
                                            (messages.size + 1).toString(),
                                            Sender.AI,
                                            MessageContentType.Text(aiReply),
                                            timestamp,
                                            R.drawable.rommy
                                        )
                                    )
                                },
                                onFailure = { error ->
                                    val timestamp = formatTimestamp(Date(), currentSystemLocale)
                                    messages.add(
                                        ChatMessage(
                                            (messages.size + 1).toString(),
                                            Sender.AI,
                                            MessageContentType.Text("⚠️ Error: ${error.message ?: "No se pudo conectar con el asistente."}"),
                                            timestamp,
                                            R.drawable.rommy
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            )
        },
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(variation_color_7)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
        ) {
            items(messages, key = { it.id }) { message ->
                ChatMessageItem(message = message)
            }
            // Mostrar indicador de escritura si isAiTyping es true
            if (isAiTyping) {
                item {
                    AiTypingIndicatorItem(avatarRes = R.drawable.rommy) // Usa el avatar de Roomy
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = false)
@Composable
fun AssistantScreenPreview() {
    AsisstantScreen(onNavigateBack = {})
}