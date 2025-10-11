package com.example.heladeria.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.heladeria.ui.viewmodel.OrderViewModel
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.heladeria.data.db.OrderWithItems
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    navController: androidx.navigation.NavHostController,
    orderVm: OrderViewModel = hiltViewModel()
) {
    val orders by orderVm.ordersFlow.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        "🍦 Historial de pedidos",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF6D4C41)
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFFFFF3E0)
                )
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(padding)
        ) {
            if (orders.isEmpty()) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color(0xFFFFF8E1)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No hay pedidos de helado aún 🍨",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                }
                return@Column
            }

            // Cambiamos la disposición de los elementos
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp)
            ) {
                items(orders) { orderWithItems: OrderWithItems ->
                    OrderListRow(orderWithItems = orderWithItems, onClick = {})
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun OrderListRow(orderWithItems: OrderWithItems, onClick: () -> Unit) {
    val order = orderWithItems.order
    val sdf = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    // Nuevo diseño tipo “card de pedido”
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE0B2)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "🍧 Pedido #${order.id}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF5D4037)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "S/ ${"%.2f".format(order.total)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFD84315)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Fecha: ${sdf.format(Date(order.timestamp))}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF6D4C41)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column {
                orderWithItems.items.take(3).forEach { item ->
                    Text(
                        "- ${item.productName} x${item.quantity}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF4E342E)
                    )
                }
                if (orderWithItems.items.size > 3) {
                    Text(
                        "+ ${orderWithItems.items.size - 3} más",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF8D6E63)
                    )
                }
            }
        }
    }
}
