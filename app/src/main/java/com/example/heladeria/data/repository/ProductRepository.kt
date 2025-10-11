package com.example.heladeria.data.repository

import com.example.heladeria.data.model.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>
}
