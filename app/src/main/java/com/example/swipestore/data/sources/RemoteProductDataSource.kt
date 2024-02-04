package com.example.swipestore.data.sources

import com.example.swipestore.data.entities.Product
import com.example.swipestore.data.entities.ProductRequest
import com.example.swipestore.data.entities.ProductResponse
import com.example.swipestore.data.services.ProductService
import retrofit2.Response

class RemoteProductDataSource(private val api: ProductService) {

    suspend fun getProducts(): List<Product>? {
        return api.getProducts()
    }

    suspend fun createProduct(request: ProductRequest): ProductResponse {
        return api.createProduct(
            request.requestMap,
            request.file
        )
    }
}