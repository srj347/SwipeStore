package com.example.swipestore.data.services

import com.example.swipestore.data.entities.Product
import com.example.swipestore.data.entities.ProductRequest
import com.example.swipestore.data.entities.ProductResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface ProductService {
    @GET("api/public/get")
    suspend fun getProducts(): List<Product>

    @Multipart
    @POST("api/public/add")
    suspend fun createProduct(
        @PartMap partMap: MutableMap<String,@JvmSuppressWildcards RequestBody>,
        @Part file: MultipartBody.Part?
    ): ProductResponse
}