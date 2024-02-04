package com.example.swipestore.data.entities

import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

data class ProductRequest(
    val requestMap: MutableMap<String, RequestBody> = mutableMapOf(),
    var file: MultipartBody.Part? = null
)