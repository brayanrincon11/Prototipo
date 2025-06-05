package com.example.prototipo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.prototipo.ui.theme.PrototipoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrototipoTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(
                                onLoginSuccess = {
                                    navController.navigate("profile") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToRegister = {
                                    navController.navigate("register")
                                }
                            )
                        }
                        composable("register") {
                            RegisterScreen(
                                onRegisterSuccess = {
                                    navController.navigate("login") {
                                        popUpTo("register") { inclusive = true }
                                    }
                                },
                                onNavigateToLogin = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable("profile") {
                            ProfileScreen(
                                navController = navController,
                                onLogout = {
                                    navController.navigate("login") {
                                        popUpTo("profile") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("map") {
                            MapScreen(
                                onNavigateToRoute = { destination ->
                                    navController.navigate("route/$destination")
                                },
                                onHomeClick = { navController.navigate("map") },
                                onFavoritesClick = { navController.navigate("profile") }
                            )
                        }
                        composable(
                            route = "route/{destination}",
                            arguments = listOf(navArgument("destination") { type = androidx.navigation.NavType.StringType })
                        ) { backStackEntry ->
                            val destination = backStackEntry.arguments?.getString("destination") ?: ""
                            RouteScreen(
                                destination = destination,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("drivers") {
                            DriversScreen(
                                navController = navController,
                                onBackClick = { navController.popBackStack() },
                                onHomeClick = { navController.navigate("map") },
                                onFavoritesClick = { navController.navigate("profile") }
                            )
                        }
                        composable("chat") {
                            ChatScreen(
                                onBackClick = { navController.popBackStack() },
                                onHomeClick = { navController.navigate("map") },
                                onFavoritesClick = { navController.navigate("profile") }
                            )
                        }
                    }
                }
            }
        }
    }
}