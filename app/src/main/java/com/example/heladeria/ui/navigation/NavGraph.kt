package com.example.heladeria.ui.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.heladeria.ui.screens.HomeScreen
import com.example.heladeria.ui.screens.SplashScreen
import com.example.heladeria.ui.screens.LoginScreen
import com.example.heladeria.ui.screens.RegisterScreen
import com.example.heladeria.ui.screens.ProfileScreen
import com.example.heladeria.ui.screens.CartScreen
import com.example.heladeria.ui.screens.OrderScreen
import com.example.heladeria.ui.screens.OrdersScreen
import com.example.heladeria.ui.viewmodel.HomeViewModel
import com.example.heladeria.ui.viewmodel.AuthViewModel
import com.example.heladeria.ui.viewmodel.CartViewModel
import com.example.heladeria.ui.viewmodel.OrderViewModel

object Routes {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val PROFILE = "profile"
    const val CART = "cart"
    const val ORDER = "order"
    const val ORDERS = "orders"
}

@Composable
fun HeladeriaNavGraph() {
    val navController = rememberNavController()

    // ViewModels principales (persisten entre pantallas)
    val authVm: AuthViewModel = hiltViewModel()
    val cartVm: CartViewModel = hiltViewModel()
    val orderVm: OrderViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {

        // 🍨 Pantalla de presentación con animación o logo de heladería
        composable(Routes.SPLASH) {
            SplashScreen(onTimeout = {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            })
        }

        // 🏠 Pantalla principal con catálogo de helados
        composable(Routes.HOME) {
            val homeVm: HomeViewModel = hiltViewModel()
            val uiState by homeVm.uiState.collectAsState()
            val authState by authVm.state.collectAsState()
            val cartItems by cartVm.itemsState.collectAsState()
            val cartCount = cartItems.sumOf { it.quantity }

            HomeScreen(
                uiState = uiState,
                onAddToCart = { product -> homeVm.addToCart(product) },
                navController = navController,
                authState = authState,
                cartCount = cartCount
            )
        }

        // 🔐 Inicio de sesión
        composable(Routes.LOGIN) {
            LoginScreen(
                navController = navController,
                onBack = { navController.navigateUp() },
                authVm = authVm
            )
        }

        // 🧁 Registro de nuevos usuarios
        composable(Routes.REGISTER) {
            RegisterScreen(
                navController = navController,
                onBack = { navController.navigateUp() },
                authVm = authVm
            )
        }

        // Perfil del cliente
        composable(Routes.PROFILE) {
            ProfileScreen(
                navController = navController,
                authVm = authVm
            )
        }

        // Carrito de compras
        composable(Routes.CART) {
            CartScreen(
                navController = navController,
                orderVm = orderVm
            )
        }

        // Confirmación del pedido actual
        composable(Routes.ORDER) {
            OrderScreen(
                navController = navController,
                orderVm = orderVm
            )
        }

        // Historial de pedidos anteriores
        composable(Routes.ORDERS) {
            OrdersScreen(
                navController = navController,
                orderVm = orderVm
            )
        }
    }
}
