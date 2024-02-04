package com.example.swipestore.ui.uistates

import com.example.swipestore.data.entities.Product

data class ProductListUiState(
    val products: ArrayList<Product>,
    var uiState: UiState
)
