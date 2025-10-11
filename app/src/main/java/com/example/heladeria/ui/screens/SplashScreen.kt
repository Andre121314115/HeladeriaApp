package com.example.heladeria.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heladeria.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFF8E1),  // crema claro
                        Color(0xFFFFECB3),  // amarillo pastel
                        Color(0xFFFFE0B2)   // naranja suave
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Imagen de logo
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo Heladería",
                modifier = Modifier
                    .size(140.dp)
                    .padding(bottom = 16.dp)
            )

            // Nombre animado o decorativo
            Text(
                text = "Heladería Delicia 🍧",
                color = Color(0xFF6D4C41),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Indicador de carga
            CircularProgressIndicator(
                color = Color(0xFFFFB74D),
                strokeWidth = 4.dp,
                modifier = Modifier.size(38.dp)
            )
        }
    }

    // Tiempo de espera (sin tocar)
    LaunchedEffect(Unit) {
        delay(1400)
        onTimeout()
    }
}
