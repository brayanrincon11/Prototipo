package com.example.prototipo.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(navController: NavController, onLogout: () -> Unit) {
    val user = FirebaseAuth.getInstance().currentUser

    val displayName = user?.displayName ?: "Nombre no disponible"
    val email = user?.email ?: "Correo no disponible"
    val photoUrl = user?.photoUrl?.toString()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C2526))
            .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // Título "Mi Perfil"
            Text(
                text = "Mi Perfil",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Fila para la foto de perfil y los datos del usuario
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (photoUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(photoUrl),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .padding(end = 16.dp)
                    )
                }

                Column {
                    Text(
                        text = displayName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "BMW",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }

            // Sección "Favoritos" con ícono
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_preferences),
                    contentDescription = "Favoritos",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Favoritos",
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                )
                Text(
                    text = "Add Home",
                    fontSize = 16.sp,
                    color = Color(0xFF1E88E5),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Sección "Más información"
            Text(
                text = "Más información",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E88E5),
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            // Número de teléfono
            Text(
                text = "Número de teléfono",
                fontSize = 16.sp,
                color = Color.White
            )
            Text(
                text = "3202035472",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Correo electrónico
            Text(
                text = "Correo electrónico",
                fontSize = 16.sp,
                color = Color.White
            )
            Text(
                text = email,
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Botón para navegar a DriversScreen
            Button(
                onClick = { navController.navigate("drivers") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Buscar conductores")
            }

            // Nuevo botón para navegar a MapScreen
            Button(
                onClick = { navController.navigate("map") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("Solicitar Servicio")
            }
        }

        // Botón "Cerrar sesión" al final
        TextButton(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                onLogout()
            },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text(
                text = "Cerrar sesión",
                fontSize = 16.sp,
                color = Color(0xFF1E88E5)
            )
        }
    }
}