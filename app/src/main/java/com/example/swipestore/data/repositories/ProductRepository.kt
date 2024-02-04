package com.example.swipestore.data.repositories

import android.util.Log
import com.example.swipestore.data.entities.Product
import com.example.swipestore.data.entities.ProductRequest
import com.example.swipestore.data.entities.ProductResponse
import com.example.swipestore.data.sources.LocalProductDataSource
import com.example.swipestore.data.sources.RemoteProductDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class ProductRepository(
    val localSource: LocalProductDataSource,
    val remoteSource: RemoteProductDataSource
) : Repository {

    private val TAG = ProductRepository::class.simpleName

    suspend fun fetchProducts(): ArrayList<Product>? = withContext(Dispatchers.IO) {
        try {
            val products = remoteSource.getProducts()
//            Todo: Optimize this conflict resolution strategy
            localSource.updateProducts(products!!)
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
        }
        return@withContext localSource.fetchProducts() as? ArrayList<Product>
    }

    suspend fun createProduct(request: ProductRequest): ProductResponse? = withContext(Dispatchers.IO){
        var response: ProductResponse? = null
        try {
            response = remoteSource.createProduct(request)
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
        }
        return@withContext response
    }
}