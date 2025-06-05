package com.example.prototipo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.delay

// Datos ficticios para los mensajes
data class Message(
    val text: String,
    val isSentByUser: Boolean,
    val timestamp: String,
    val emojis: List<String> = emptyList()
)

@Composable
fun ChatScreen(
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onFavoritesClick: () -> Unit
) {
    // Estado para los mensajes y el texto del input
    var messages by rememberSaveable { mutableStateOf(listOf<Message>()) }
    var messageText by rememberSaveable { mutableStateOf("") }
    var sendTrigger by remember { mutableStateOf(0) } // Estado para disparar el envío

    // Lógica para manejar el envío y la respuesta
    LaunchedEffect(sendTrigger) {
        if (sendTrigger > 0 && messages.isNotEmpty()) {
            val lastMessage = messages.last()
            if (lastMessage.isSentByUser) { // Solo responder si el último mensaje es del usuario
                delay(1000) // Retraso de 1 segundo
                val response = when {
                    lastMessage.text.contains("confirmar", ignoreCase = true) -> "Sí, el servicio está confirmado. ¿Algo más?"
                    lastMessage.text.contains("hola", ignoreCase = true) -> "¡Hola! ¿En qué puedo ayudarte?"
                    else -> "Gracias por tu mensaje. Estoy revisando tu solicitud."
                }
                messages = messages + Message(response, false, "01:31 PM -05") // Hora actualizada
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        // Encabezado con nombre del conductor
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_revert),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = "Brayan Rincón",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "BMW",
                fontSize = 16.sp,
                color = Color.Blue
            )
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_info_details),
                contentDescription = "Info",
                modifier = Modifier.size(24.dp)
            )
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_preferences),
                contentDescription = "Favorites",
                modifier = Modifier.size(24.dp)
            )
        }

        // Lista de mensajes
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 16.dp),
            reverseLayout = true
        ) {
            items(messages.reversed()) { message ->
                MessageItem(message = message)
            }
        }

        // Área de entrada de texto
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = { Text("Escribir un mensaje") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            IconButton(onClick = {
                if (messageText.isNotBlank()) {
                    // Agregar el mensaje del usuario
                    messages = messages + Message(messageText, true, "01:30 PM -05") // Hora actualizada
                    messageText = "" // Limpiar el input después de enviar
                    sendTrigger++ // Disparar LaunchedEffect para la respuesta
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Color.Blue
                )
            }
            IconButton(onClick = {
                // TODO: Implementar selección de emojis
            }) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = "Emojis",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Botones "Home" y "Favoritos" en la parte inferior
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = onHomeClick) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_myplaces),
                    contentDescription = "Home",
                    modifier = Modifier.size(24.dp)
                )
                Text("Home", fontSize = 12.sp)
            }
            IconButton(onClick = onFavoritesClick) {
                Icon(
                    painter = painterResource(id = android.R.drawable.star_on),
                    contentDescription = "Favoritos",
                    modifier = Modifier.size(24.dp)
                )
                Text("Favoritos", fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun MessageItem(message: Message) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = if (message.isSentByUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = if (message.isSentByUser) Color(0xFFE3F2FD) else Color(0xFFF5F5F5),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = message.text,
                    fontSize = 16.sp
                )
                if (message.emojis.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Row {
                        message.emojis.forEach { emoji ->
                            Text(text = emoji, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
        Text(
            text = message.timestamp,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}