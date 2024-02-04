package com.example.swipestore.ui.di

import com.example.swipestore.data.repositories.ProductRepository
import com.example.swipestore.data.repositories.Repository
import com.example.swipestore.data.services.AppDatabase
import com.example.swipestore.data.services.ProductService
import com.example.swipestore.data.services.RetrofitClient
import com.example.swipestore.data.sources.LocalProductDataSource
import com.example.swipestore.data.sources.RemoteProductDataSource
import com.example.swipestore.ui.viewmodels.ProductViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


val productModule = module {
    single{
        RetrofitClient.getRetrofit()
            .create(ProductService::class.java)
    }
    single{
        LocalProductDataSource(get())
    }
    single{
        RemoteProductDataSource(get())
    }
    factory<Repository> {
        ProductRepository(get(), get())
    }
    viewModel {
        ProductViewModel(get())
    }
}