package com.example.heladeria.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.heladeria.data.repository.CartEntry
import com.example.heladeria.ui.viewmodel.CartViewModel
import com.example.heladeria.ui.viewmodel.OrderViewModel
import com.example.heladeria.ui.navigation.Routes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavHostController,
    orderVm: OrderViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
    vm: CartViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val items by vm.itemsState.collectAsState()
    val total by vm.totalState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Carrito de Helados 🍧") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(padding)
        ) {
            if (items.isEmpty()) {
                // 🩵 Vista cuando el carrito está vacío
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Tu carrito de helados está vacío 🍦",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Agrega tus sabores favoritos desde el catálogo.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { navController.navigateUp() },
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text("Ver catálogo")
                        }
                    }
                }
                return@Column
            }

            // Lista de helados seleccionados
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { entry ->
                    CartRow(
                        entry = entry,
                        onIncrease = { vm.setQuantity(entry.product.id, entry.quantity + 1) },
                        onDecrease = { vm.setQuantity(entry.product.id, entry.quantity - 1) },
                        onRemove = { vm.remove(entry.product.id) },
                        onSetQuantity = { newQty -> vm.setQuantity(entry.product.id, newQty) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Total y acciones finales
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Total a pagar:", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            "S/ ${"%.2f".format(total)}",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            orderVm.placeOrder { success, errorMsg ->
                                if (success) {
                                    navController.navigate(Routes.ORDER) { launchSingleTop = true }
                                } else {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            errorMsg ?: "Error al registrar pedido"
                                        )
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        Icon(
                            Icons.Default.AddShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Text("Finalizar pedido 🍨")
                    }
                }
            }
        }
    }
}

@Composable
fun CartRow(
    entry: CartEntry,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit,
    onSetQuantity: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val ctx = LocalContext.current
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 12.dp)
            ) {
                if (entry.product.imageRes != null) {
                    Image(
                        painter = painterResource(id = entry.product.imageRes),
                        contentDescription = entry.product.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    AsyncImage(
                        model = ImageRequest.Builder(ctx)
                            .data(entry.product.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = entry.product.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = com.example.heladeria.R.drawable.placeholder_image),
                        error = painterResource(id = com.example.heladeria.R.drawable.placeholder_image)
                    )
                }
            }

            Column(Modifier.weight(1f)) {
                Text(
                    entry.product.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("S/ ${"%.2f".format(entry.product.price)}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "Subtotal: S/ ${"%.2f".format(entry.product.price * entry.quantity)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Controles
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = onIncrease) {
                    Icon(Icons.Default.AddShoppingCart, contentDescription = "Agregar")
                }

                var qtyText by remember { mutableStateOf(entry.quantity.toString()) }

                LaunchedEffect(entry.quantity) {
                    qtyText = entry.quantity.toString()
                }

                OutlinedTextField(
                    value = qtyText,
                    onValueChange = { v ->
                        val filtered = v.filter { it.isDigit() }
                        val safe = if (filtered.isBlank()) "0" else filtered
                        qtyText = safe
                        val intV = safe.toIntOrNull()
                        if (intV != null) {
                            val limited = intV.coerceIn(0, entry.product.stock)
                            if (limited != entry.quantity) {
                                onSetQuantity(limited)
                            }
                        }
                    },
                    modifier = Modifier.width(64.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                IconButton(onClick = onDecrease) {
                    Icon(Icons.Default.Remove, contentDescription = "Reducir")
                }

                TextButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}
