package com.example.swipestore.data.sources

import com.example.swipestore.data.entities.Product
import com.example.swipestore.data.services.ProductDao

class LocalProductDataSource(
    private val dao: ProductDao
) {
    suspend fun fetchProducts(): List<Product> {
        return dao.getAllProducts()
    }

    suspend fun updateProducts(products: List<Product>){
        dao.insertAll(products)
    }

}