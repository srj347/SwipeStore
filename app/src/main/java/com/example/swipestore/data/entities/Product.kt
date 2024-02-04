package com.example.swipestore.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "products", primaryKeys = ["name", "type", "price", "tax"])
data class Product(
    @SerializedName("product_name")
    val name: String,
    @SerializedName("product_type")
    val type: String,
    val image: String,
    val price: String,
    val tax: String
): Serializable
