package com.example.heladeria.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.isEmpty
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.heladeria.data.model.Product
import com.example.heladeria.ui.components.ProductCard
import com.example.heladeria.ui.components.ProductDetailBottomSheet
import com.example.heladeria.ui.navigation.Routes
import com.example.heladeria.ui.viewmodel.AuthState
import com.example.heladeria.ui.viewmodel.HomeUiState
import kotlinx.coroutines.launch
import kotlin.text.contains

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onAddToCart: (Product) -> Unit,
    navController: NavHostController,
    authState: AuthState,
    cartCount: Int,
    onProductSelected: (Product) -> Unit,
    onDismissProductDetails: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // --- NUEVO: Estado para guardar el texto de búsqueda ---
    var searchQuery by remember { mutableStateOf("") }

    // --- NUEVO: Lista de productos filtrada ---
    val filteredProducts = remember(searchQuery, uiState.products) {
        if (searchQuery.isBlank()) {
            uiState.products
        } else {
            uiState.products.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    uiState.selectedProduct?.let { product ->
        ProductDetailBottomSheet(
            product = product,
            onDismiss = onDismissProductDetails,
            onAddToCart = onAddToCart
        )
    }

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
                Spacer(modifier = Modifier.height(16.dp))

                // --- NUEVO: Campo de texto para el buscador ---
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Buscar por nombre...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Icono de búsqueda"
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4DD0E1),
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color(0xFF4DD0E1)
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

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
                    // --- MODIFICADO: Comprobar la lista filtrada ---
                } else if (filteredProducts.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            if (searchQuery.isBlank()) "No hay productos disponibles 😢"
                            else "No se encontraron productos para \"$searchQuery\" 😢",
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
                        // --- MODIFICADO: Usar la lista filtrada ---
                        items(filteredProducts) { product ->
                            ProductCard(
                                product = product,
                                onProductClick = { onProductSelected(product) },
                                onAddToCart = {
                                    onAddToCart(product)
                                    scope.launch {
                                        snackbarHostState.showSnackbar("¡Helado añadido al carrito!")
                                    }
                                },
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