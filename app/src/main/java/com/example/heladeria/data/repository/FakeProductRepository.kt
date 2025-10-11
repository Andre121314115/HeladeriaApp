package com.example.heladeria.data.repository

import com.example.heladeria.data.model.Product
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton
import com.example.heladeria.R

@Singleton
class FakeProductRepository @Inject constructor() : ProductRepository {
    override suspend fun getProducts(): List<Product> {
        delay(500)
        return listOf(
            Product(1, "Capuchino frío", "Delicioso capuchino helado con crema y cacao.", 6.50, imageRes = R.drawable.capuchino_frio, stock = 10),
            Product(2, "Copa de helado", "Helado surtido con frutas frescas y salsa de chocolate.", 8.00, imageRes = R.drawable.copa_de_helado, stock = 15),
            Product(4, "Granizado", "Bebida helada y refrescante con sabor natural.", 4.00, imageRes = R.drawable.granizado, stock = 5),
            Product(5, "Helado de fresa", "Cremoso helado artesanal de fresa natural.", 4.50, imageRes = R.drawable.helado_de_fresa, stock = 8)
        )
    }
}
