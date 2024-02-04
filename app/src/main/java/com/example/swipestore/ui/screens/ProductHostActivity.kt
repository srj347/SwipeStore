package com.example.swipestore.ui.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.swipestore.R
import com.example.swipestore.ui.viewmodels.ProductViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductHostActivity : AppCompatActivity() {

    private val viewModel by viewModel<ProductViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_host)

    }
}