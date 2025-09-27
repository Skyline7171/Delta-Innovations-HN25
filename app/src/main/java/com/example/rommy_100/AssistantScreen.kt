package com.example.rommy_100

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowUpward
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rommy_100.ui.theme.AppTextStyles
import com.example.rommy_100.ui.theme.color_3
import com.example.rommy_100.ui.theme.color_5
import com.example.rommy_100.ui.theme.color_6
import com.example.rommy_100.ui.theme.variation_color_7
import com.example.rommy_100.ui.theme.variation_color_8

data class ChatMessage(
    val id: String,
    val sender: Sender,
    val content: MessageContentType,
    val timestamp: String,
    val avatarRes: Int? = null, // R.drawable.test2 o R.drawable.ic_user_placeholder
    val avatarIcon: ImageVector? = null // Puedes usar un ícono si prefieres
)

enum class Sender { USER, AI }
sealed class MessageContentType {
    data class Text(val text: String) : MessageContentType()
    data class Code(val code: String, val lang: String = "kotlin") : MessageContentType()
    data class Image(val painterRes: Int) :
        MessageContentType() // Usar Int para PainterResource en preview

    data class Actions(val actions: List<String>) : MessageContentType()
}

@Composable
fun AiChatInputBar(
    textValue: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    onSend: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
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
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), // Fondo sutil
                    unfocusedContainerColor = color_5,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = Color.Transparent, // Sin línea indicadora para un look más limpio
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(24.dp),
                maxLines = 5,
                minLines = 1,
                trailingIcon = { // <--- USA trailingIcon
                    IconButton(onClick = {}) { // Hazlo clickeable si es necesario
                        Icon(
                            imageVector = Icons.Filled.ArrowUpward,
                            contentDescription = "Enviar",
                            tint = color_3
                        )
                    }
                }
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
                    .clip(CircleShape)
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
                .drawBehind { // Dibujar la "cola" de la burbuja
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
                message.avatarIcon?.let {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsisstantScreen(onNavigateBack: () -> Unit) {
    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                "1",
                Sender.AI,
                MessageContentType.Text("¡Hola! Soy tu asistente de IA. ¿En qué puedo ayudarte hoy?"),
                "10:00 AM",
                R.drawable.test2
            ),
            ChatMessage(
                "2",
                Sender.USER,
                MessageContentType.Text("Hoy tuve una cita con mi nutricionista y necesito tu ayuda para seguir sus indicaciones sobre mis horarios de comida."),
                "10:01 AM",
                null,
                Icons.Filled.AccountCircle
            ),
        )
    }
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    val listState = rememberLazyListState()

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
                            painter = painterResource(id = R.drawable.test),
                            contentDescription = "Avatar IA",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Rommy",
                            style = AppTextStyles.titleLarge,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* Acción de Menú/Volver */ }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver/Menú",
                            tint = color_6
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Acción Más Opciones */ }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Más Opciones", tint = color_6)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = variation_color_8, // Tu color deseado para el fondo
                )
            )
        },
        bottomBar = {
            AiChatInputBar(
                textValue = inputText,
                onTextChange = { inputText = it },
                onSend = {
                    if (inputText.text.isNotBlank()) {
                        messages.add(
                            ChatMessage(
                                (messages.size + 1).toString(),
                                Sender.USER,
                                MessageContentType.Text(inputText.text),
                                "10:10 AM"
                            )
                        )
                        inputText = TextFieldValue("")
                        // Aquí la IA respondería
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
            // item { AiTypingIndicatorItem(avatarRes = R.drawable.test2) } // Descomentar para ver el indicador
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AssistantScreenPreview() {
    AppNavigation()
}