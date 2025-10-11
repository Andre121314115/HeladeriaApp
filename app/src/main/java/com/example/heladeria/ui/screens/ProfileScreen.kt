package com.example.heladeria.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.heladeria.ui.viewmodel.AuthViewModel
import com.example.heladeria.ui.navigation.Routes

@Composable
fun ProfileScreen(
    navController: NavHostController,
    authVm: AuthViewModel = hiltViewModel()
) {
    val state by authVm.state.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8E1)) // Fondo crema pastel
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFD84315))
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = "🍦 Mi Perfil",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFF6D4C41)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Tarjeta de información del usuario
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE0B2)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "👤 Nombre:",
                                color = Color(0xFF5D4037),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = state.user?.name ?: "-",
                                color = Color(0xFF4E342E)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "📧 Correo:",
                                color = Color(0xFF5D4037),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = state.user?.email ?: "-",
                                color = Color(0xFF4E342E)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(36.dp))

                    // Botones en diferente orden y color pastel
                    Button(
                        onClick = { navController.navigate(Routes.ORDERS) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFCC80))
                    ) {
                        Text("🧾 Ver historial", color = Color(0xFF4E342E))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            authVm.logout()
                            val popped = navController.popBackStack(route = Routes.HOME, inclusive = false)
                            if (!popped) {
                                navController.navigate(Routes.HOME) {
                                    launchSingleTop = true
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFAB91))
                    ) {
                        Text("🚪 Cerrar sesión", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                    ) {
                        Text("⬅️ Volver")
                    }
                }
            }
        }
    }
}
