package com.example.swipestore.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swipestore.data.entities.Product
import com.example.swipestore.data.entities.ProductRequest
import com.example.swipestore.data.repositories.ProductRepository
import com.example.swipestore.data.repositories.Repository
import com.example.swipestore.ui.uistates.UiState
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ProductViewModel(
    val repository: Repository
): ViewModel() {

    private val TAG = ProductViewModel::class.simpleName
    private val _uiState: MutableLiveData<UiState> = MutableLiveData()
    private val _products: MutableLiveData<ArrayList<Product>?> = MutableLiveData()
    val uiState: LiveData<UiState> = _uiState
    val products: MutableLiveData<ArrayList<Product>?> = _products

    fun fetchProducts(){
        viewModelScope.launch {
            if(repository is ProductRepository){
                try{
                    _uiState.value = UiState.Loading
                    val products = repository.fetchProducts()
                    _products.value = products
                    _uiState.value = UiState.Success(products)
                } catch (e: Exception) {
                    _uiState.value = UiState.Error(e.message.toString())
                }
            }
        }
    }

    fun createProduct(request: ProductRequest) {
        viewModelScope.launch {
            if(repository is ProductRepository){
                try{
                    _uiState.value = UiState.Loading
                    val response = repository.createProduct(request)
                    _uiState.value = UiState.Success(response?.product_details)
                } catch (e: Exception) {
                    _uiState.value = UiState.Error(e.message.toString())
                }
            }
        }
    }
}