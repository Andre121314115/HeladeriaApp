package com.example.heladeria.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.heladeria.ui.navigation.Routes
import com.example.heladeria.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavHostController,
    onBack: () -> Unit,
    authVm: AuthViewModel
) {
    val state by authVm.state.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFFBF6), Color(0xFFD7F9F8))
                )
            )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                // Ícono y título
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Heladería Icono",
                    tint = Color(0xFF6AD3D3),
                    modifier = Modifier.size(72.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Bienvenido a Heladería Delicia 🍧",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF3B3B3B),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Campo correo
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6AD3D3),
                        focusedLabelColor = Color(0xFF6AD3D3)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6AD3D3),
                        focusedLabelColor = Color(0xFF6AD3D3)
                    )
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Botón principal
                Button(
                    onClick = {
                        authVm.login(email, password) { success, err ->
                            if (success) {
                                onBack()
                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar(err ?: "Error al iniciar sesión")
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6AD3D3),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Iniciar sesión", style = MaterialTheme.typography.bodyLarge)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botón crear cuenta
                OutlinedButton(
                    onClick = { navController.navigate(Routes.REGISTER) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF6AD3D3)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Crear cuenta", style = MaterialTheme.typography.bodyLarge)
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = { onBack() }) {
                    Text(
                        "Volver",
                        color = Color(0xFFFA9E8C),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF6AD3D3))
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
