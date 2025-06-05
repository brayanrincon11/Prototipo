package com.example.prototipo.ui

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp

@Composable
fun RouteScreen(
    destination: String,
    onBack: () -> Unit
) {
    // Declarar mapView en el ámbito del Composable
    var mapView by remember { mutableStateOf<MapView?>(null) }
    var isMapInitialized by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Barra superior
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_revert),
                    contentDescription = "Volver",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "_",
                color = Color.White,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "_",
                color = Color.White,
                fontSize = 16.sp
            )
        }

        // Mapa con cuadro de origen y llegada superpuesto
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Mapa
            AndroidView(
                factory = { context ->
                    Log.d("RouteScreen", "Creating MapView")
                    MapView(context).apply {
                        onCreate(Bundle())
                        mapView = this // Asignar a la variable del ámbito superior
                        Log.d("RouteScreen", "MapView created: $this")
                        this
                    }
                },
                modifier = Modifier.fillMaxSize(),
                update = { map ->
                    Log.d("RouteScreen", "Updating MapView: $map")
                    if (!isMapInitialized) {
                        map.getMapAsync { googleMap ->
                            Log.d("RouteScreen", "Map initialized successfully")
                            googleMap.uiSettings.isZoomControlsEnabled = true

                            // Coordenadas de ejemplo para U. Católica y Centro Comercial Andino
                            val uCatolica = LatLng(4.63400, -74.06698)
                            val patioBonito = LatLng(4.66709, -74.05327)

                            // Agregar marcadores
                            googleMap.addMarker(MarkerOptions().position(uCatolica).title("U. Católica de Colombia"))
                            googleMap.addMarker(MarkerOptions().position(patioBonito).title("Centro Comercial Andino"))

                            // Simular una ruta con una Polyline (línea recta por ahora)
                            googleMap.addPolyline(
                                PolylineOptions()
                                    .add(uCatolica, patioBonito)
                                    .color(android.graphics.Color.BLUE)
                                    .width(5f)
                            )

                            // Centrar el mapa para mostrar ambos puntos
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(uCatolica, 12f))
                            isMapInitialized = true
                        }
                    }
                }
            )

            // Mostrar un mensaje de depuración si el mapa no se inicializa
            if (!isMapInitialized) {
                Text(
                    text = "Cargando mapa...",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Cuadro de origen y llegada superpuesto
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                // Punto de origen
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_mylocation),
                        contentDescription = "Punto de origen",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Green
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "U. Católica",
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }

                // Línea punteada
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .width(2.dp)
                        .height(16.dp)
                        .background(Color.Gray, shape = DottedShape(step = 4.dp))
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Punto de llegada
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_mylocation),
                        contentDescription = "Punto de llegada",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Red
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Centro Comercial Andino",
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }
        }

        // DisposableEffect para manejar el ciclo de vida del MapView
        DisposableEffect(Unit) {
            Log.d("RouteScreen", "DisposableEffect triggered, mapView: $mapView")
            mapView?.onResume()
            onDispose {
                Log.d("RouteScreen", "DisposableEffect onDispose triggered")
                mapView?.onPause()
                mapView?.onStop()
                mapView?.onDestroy()
            }
        }

        // Detalles del conductor
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp)
        ) {
            Text(
                text = "Este conductor está disponible",
                color = Color.Red,
                fontSize = 16.sp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_myplaces),
                    contentDescription = "Foto conductor",
                    modifier = Modifier.size(40.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Bryan Rincón",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "BMW",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Row {
                        Text(
                            text = "4.5 ★",
                            color = Color.Yellow,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "PRECIO $7,500",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // Botones de acción
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { onBack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Cancelar")
            }
            Button(
                onClick = { /* Acción Confirmar */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text("Confirmar")
            }
        }
    }
}

// Utilidad para crear una línea punteada
class DottedShape(
    private val step: Dp
) : androidx.compose.ui.graphics.Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: androidx.compose.ui.unit.LayoutDirection,
        density: androidx.compose.ui.unit.Density
    ): androidx.compose.ui.graphics.Outline {
        val stepPx = with(density) { step.toPx() }
        val path = androidx.compose.ui.graphics.Path().apply {
            val length = size.height
            var currentY = 0f
            while (currentY < length) {
                moveTo(0f, currentY)
                lineTo(0f, currentY + stepPx / 2)
                currentY += stepPx
            }
        }
        return androidx.compose.ui.graphics.Outline.Generic(path)
    }
}