package com.example.prototipo.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MapScreen(
    onNavigateToRoute: (String) -> Unit,
    onHomeClick: () -> Unit,
    onFavoritesClick: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val scope = rememberCoroutineScope()

    var locationText by remember { mutableStateOf("Obteniendo ubicación...") }
    var permissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var marker by remember { mutableStateOf<Marker?>(null) }
    var destination by remember { mutableStateOf("Ingresar Dirección") }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted
        if (isGranted) {
            obtenerUbicacion(fusedLocationClient, scope) { lat, lon ->
                currentLocation = LatLng(lat, lon)
                locationText = "Lat: $lat, Lon: $lon"
            }
        } else {
            locationText = "Permiso de ubicación denegado"
        }
    }

    LaunchedEffect(Unit) {
        if (!permissionGranted) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            obtenerUbicacion(fusedLocationClient, scope) { lat, lon ->
                currentLocation = LatLng(lat, lon)
                locationText = "Lat: $lat, Lon: $lon"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
    ) {
        // Encabezado
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                contentDescription = "Logo",
                modifier = Modifier.size(40.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Carpooling UCC",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Blue
            )
        }

        // Campo de texto "¿A dónde vamos?" con lupa
        OutlinedTextField(
            value = destination,
            onValueChange = { destination = it },
            label = { Text("¿A dónde vamos?") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(8.dp),
            trailingIcon = {
                IconButton(
                    onClick = { onNavigateToRoute(destination) }
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_search),
                        contentDescription = "Buscar dirección",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        )

        // Mapa
        var mapView by remember { mutableStateOf<MapView?>(null) }
        AndroidView(
            factory = { context ->
                MapView(context).apply {
                    onCreate(null)
                    mapView = this
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .aspectRatio(1.5f),
            update = { map ->
                mapView = map
                map.getMapAsync { googleMap ->
                    googleMap.uiSettings.isZoomControlsEnabled = true
                    val sampleLocation = LatLng(4.65655, -74.08744)
                    googleMap.addMarker(MarkerOptions().position(sampleLocation).title("Conductor"))
                    currentLocation?.let { latLng ->
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                        marker?.remove()
                        marker = googleMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title("Ubicación actual")
                        )
                    }
                }
            }
        )

        DisposableEffect(Unit) {
            val map = mapView
            if (map != null) {
                map.onResume()
            }
            onDispose {
                map?.onPause()
                map?.onStop()
                map?.onDestroy()
            }
        }

        // Sección de Conductores Disponibles
        Text(
            text = "Conductores Disponibles:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        LazyColumn {
            items(listOf("Alice Smith", "Alejandro García", "Pedro Durán")) { driver ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = false,
                        onClick = { /* No implementado */ },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = driver,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        painter = painterResource(id = android.R.drawable.btn_star_big_on),
                        contentDescription = "Estrellas",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Yellow
                    )
                    Icon(
                        painter = painterResource(id = android.R.drawable.btn_star_big_on),
                        contentDescription = "Estrellas",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Yellow
                    )
                    Icon(
                        painter = painterResource(id = android.R.drawable.btn_star_big_on),
                        contentDescription = "Estrellas",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Yellow
                    )
                    Icon(
                        painter = painterResource(id = android.R.drawable.btn_star_big_on),
                        contentDescription = "Estrellas",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Yellow
                    )
                }
            }
        }

        // Botones inferiores
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { /* Acción Buscar */ },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_search),
                    contentDescription = "Buscar",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Buscar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { /* Acción Favoritos */ },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.star_on),
                    contentDescription = "Favoritos",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Favoritos...")
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { /* Acción Perfil */ }
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_myplaces),
                    contentDescription = "Perfil",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

fun obtenerUbicacion(
    fusedLocationClient: FusedLocationProviderClient,
    scope: CoroutineScope,
    onResultado: (Double, Double) -> Unit
) {
    scope.launch(Dispatchers.IO) {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                scope.launch(Dispatchers.Main) {
                    if (location != null) {
                        onResultado(location.latitude, location.longitude)
                    } else {
                        val locationRequest = LocationRequest.create().apply {
                            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                            interval = 10000
                            fastestInterval = 5000
                        }

                        val locationCallback = object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult) {
                                locationResult.lastLocation?.let { newLocation ->
                                    scope.launch(Dispatchers.Main) {
                                        onResultado(newLocation.latitude, newLocation.longitude)
                                    }
                                }
                            }
                        }

                        fusedLocationClient.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.getMainLooper()
                        ).addOnSuccessListener {
                            println("Solicitud de actualizaciones iniciada correctamente")
                        }.addOnFailureListener { e ->
                            scope.launch(Dispatchers.Main) {
                                println("Error al solicitar actualizaciones: ${e.localizedMessage}")
                            }
                        }
                    }
                }
            }.addOnFailureListener { e ->
                scope.launch(Dispatchers.Main) {
                    println("Error al obtener ubicación: ${e.localizedMessage}")
                }
            }
        } catch (e: SecurityException) {
            scope.launch(Dispatchers.Main) {
                println("Permiso no concedido: ${e.localizedMessage}")
            }
        } catch (e: Exception) {
            scope.launch(Dispatchers.Main) {
                println("Error al obtener ubicación: ${e.localizedMessage}")
            }
        }
    }
}