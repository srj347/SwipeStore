package com.example.swipestore

import android.app.Application
import com.example.swipestore.ui.di.productModule
import com.example.swipestore.ui.di.roomDbModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger(Level.ERROR)
            androidContext(this@BaseApplication)
            modules(listOf(productModule, roomDbModule))
        }
    }
}