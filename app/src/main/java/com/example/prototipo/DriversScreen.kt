package com.example.prototipo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable // Importar Modifier.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController

// Datos de ejemplo para la lista de conductores
data class Driver(
    val name: String,
    val status: String,
    val online: Boolean,
    val rating: Float
)

@Composable
fun DriversScreen(
    navController: NavController,
    onBackClick: () -> Unit, // Asegúrate de que onBackClick esté definido aquí
    onHomeClick: () -> Unit,
    onFavoritesClick: () -> Unit
) {
    val drivers = listOf(
        Driver("Alice Smith", "En línea", true, 4.5f),
        Driver("Carlos Torres", "Fuera de línea", false, 4.7f),
        Driver("Alejandro G...", "En línea", true, 4.2f),
        Driver("Santiago Ve...", "Fuera de línea", false, 4.8f),
        Driver("Sebastian A...", "Fuera de línea", false, 4.3f),
        Driver("Pedro Durán", "En línea", true, 4.6f)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 64.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // Encabezado "Carpooling UCC" con ícono
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE0F7FA), shape = RoundedCornerShape(8.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = "UCC Logo",
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = "Carpooling UCC",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Barra de búsqueda
            OutlinedTextField(
                value = "",
                onValueChange = { /* TODO: Implementar búsqueda */ },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                placeholder = { Text("Buscar conductores") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )

            // Lista de conductores
            LazyColumn {
                items(drivers) { driver ->
                    DriverItem(driver = driver, navController = navController)
                }
            }
        }

        // Botón "ATRÁS"
        Button(
            onClick = onBackClick, // Usa onBackClick directamente
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("ATRÁS", textAlign = TextAlign.Center)
        }

        // Botones "Home" y "Favoritos" en la parte inferior
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
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
fun DriverItem(driver: Driver, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { navController.navigate("chat") }, // clickable ahora está resuelto
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ícono de perfil (placeholder)
        Icon(
            painter = painterResource(id = android.R.drawable.ic_dialog_info),
            contentDescription = "Profile",
            modifier = Modifier.size(24.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(
                text = driver.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = driver.status,
                fontSize = 14.sp,
                color = if (driver.online) Color.Green else Color.Red
            )
        }
        Icon(
            painter = if (driver.online) painterResource(id = android.R.drawable.checkbox_on_background)
            else painterResource(id = android.R.drawable.checkbox_off_background),
            contentDescription = if (driver.online) "En línea" else "Fuera de línea",
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = "★★★★☆".take(driver.rating.toInt()),
            fontSize = 16.sp,
            color = Color.Yellow
        )
    }
}