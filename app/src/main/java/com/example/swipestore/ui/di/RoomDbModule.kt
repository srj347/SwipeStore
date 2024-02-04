package com.example.swipestore.ui.di

import android.content.Context
import androidx.room.Room
import com.example.swipestore.data.services.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val roomDbModule = module {
    single {
        provideDatabase(androidApplication())
    }
    single{
        provideDao(get())
    }
}
fun provideDatabase(context: Context) = run {
    Room.databaseBuilder(context, AppDatabase::class.java, "products")
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()
}
fun provideDao(db: AppDatabase) = db.productDao()