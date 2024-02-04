package com.example.swipestore.data.services

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.swipestore.data.entities.Product

@Database(entities = [Product::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}
