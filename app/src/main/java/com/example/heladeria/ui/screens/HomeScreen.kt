package com.example.heladeria.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Helado",
                            tint = Color(0xFF6AD3D3)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Heladería Delicia",
                            color = Color(0xFF3B3B3B),
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    if (authState.isAuthenticated) {
                        IconButton(onClick = { navController.navigate(Routes.PROFILE) }) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Perfil",
                                tint = Color(0xFF6AD3D3)
                            )
                        }
                    } else {
                        TextButton(onClick = { navController.navigate(Routes.LOGIN) }) {
                            Text(
                                "Iniciar sesión",
                                color = Color(0xFF6AD3D3),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    IconButton(onClick = {
                        if (authState.isAuthenticated) {
                            navController.navigate(Routes.CART)
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Inicia sesión para ver tu carrito 🍦")
                            }
                        }
                    }) {
                        BadgedBox(badge = {
                            if (cartCount > 0) {
                                Badge(
                                    containerColor = Color(0xFFFA9E8C)
                                ) { Text("$cartCount") }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Carrito",
                                tint = Color(0xFF6AD3D3)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFFFFBF6)
                )
            )
        },

        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(Color(0xFFFFFBF6), Color(0xFFE0F7FA))
                    )
                )
        ) {
            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Cargando sabores...", color = Color(0xFF6AD3D3))
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 170.dp),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = uiState.products) { product ->
                        ProductCard(product = product, onAddToCart = onAddToCart)
                    }
                }
            }
        }
    }
}
