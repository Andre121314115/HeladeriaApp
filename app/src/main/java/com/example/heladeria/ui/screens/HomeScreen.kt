package com.example.heladeria.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.heladeria.data.model.Product
import com.example.heladeria.ui.components.ProductCard
import com.example.heladeria.ui.navigation.Routes
import com.example.heladeria.ui.viewmodel.AuthState
import com.example.heladeria.ui.viewmodel.HomeUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onAddToCart: (Product) -> Unit,
    navController: NavHostController,
    authState: AuthState,
    cartCount: Int
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "🍦 Heladería Delicia 🍧",
                            color = Color(0xFF3B3B3B),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp
                        )
                        Text(
                            "¡Disfruta de nuestros sabores artesanales!",
                            fontSize = 13.sp,
                            color = Color(0xFF7C7C7C)
                        )
                    }
                },
                actions = {
                    if (authState.isAuthenticated) {
                        IconButton(onClick = { navController.navigate(Routes.PROFILE) }) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Perfil",
                                tint = Color(0xFF4DD0E1)
                            )
                        }
                    } else {
                        TextButton(onClick = { navController.navigate(Routes.LOGIN) }) {
                            Text(
                                "Iniciar sesión",
                                color = Color(0xFF4DD0E1),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    IconButton(onClick = {
                        if (authState.isAuthenticated) {
                            navController.navigate(Routes.CART)
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Inicia sesión para ver tu carrito 🍨")
                            }
                        }
                    }) {
                        BadgedBox(badge = {
                            if (cartCount > 0) {
                                Badge(containerColor = Color(0xFFFF8A65)) {
                                    Text("$cartCount")
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Carrito",
                                tint = Color(0xFF4DD0E1)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFFFF8F2)
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(Color(0xFFFFF8F2), Color(0xFFE0F7FA))
                    )
                )
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Nuestros sabores destacados 🍨",
                    color = Color(0xFF3B3B3B),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 8.dp)
                )

                if (uiState.isLoading) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF4DD0E1))
                    }
                } else if (uiState.products.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "No hay productos disponibles 😢",
                            textAlign = TextAlign.Center,
                            color = Color(0xFF7C7C7C)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(uiState.products) { product ->
                            ProductCard(
                                product = product,
                                onAddToCart = onAddToCart,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(3.dp, RoundedCornerShape(16.dp))
                                    .background(
                                        color = Color.White,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}
