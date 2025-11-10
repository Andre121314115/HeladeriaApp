package com.example.heladeria.data.repository

import com.example.heladeria.data.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseProductRepository @Inject constructor() : ProductRepository {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun getProducts(): List<Product> {
        return try {
            val snapshot = db.collection("productos").get().await()
            snapshot.documents.mapNotNull { doc ->
                val name = doc.getString("name")
                val description = doc.getString("description")
                val price = doc.getDouble("price")
                val stock = doc.getLong("stock")?.toInt()
                val imageUrl = doc.getString("imageUrl")

                if (name != null && description != null && price != null && stock != null) {
                    Product(
                        id = doc.id.hashCode(), // genera un id local único
                        name = name,
                        description = description,
                        price = price,
                        imageUrl = imageUrl,
                        stock = stock
                    )
                } else null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
