package com.example.heladeria.ui.viewmodel

import androidx.compose.animation.core.copy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heladeria.data.model.Product
import com.example.heladeria.data.repository.ProductRepository
import com.example.heladeria.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val products: List<Product> = emptyList(),
    val cart: Map<Int, Int> = emptyMap(),
    val selectedProduct: Product? = null
    ) {
    val cartCount: Int get() = cart.values.sum()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: ProductRepository,
    private val cartRepo: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            val prods = repo.getProducts()
            _uiState.value = _uiState.value.copy(isLoading = false, products = prods)
        }
    }
    fun addToCart(product: Product) {
        cartRepo.addProduct(product)
    }

    fun onProductSelected(product: Product) {
        _uiState.update { it.copy(selectedProduct = product) }
    }

    fun onDismissProductDetails() {
        _uiState.update { it.copy(selectedProduct = null) }
    }
}
