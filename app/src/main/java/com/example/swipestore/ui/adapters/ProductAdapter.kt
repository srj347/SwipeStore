package com.example.swipestore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.swipestore.R
import com.example.swipestore.data.entities.Product
import com.example.swipestore.databinding.ItemviewProductBinding
import com.example.swipestore.ui.utils.UiComponentUtils

class ProductAdapter(
    private val context: Context,
    private val products: ArrayList<Product>,
    private val onItemClick: (data: Product) -> Unit
): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemviewProductBinding.inflate(LayoutInflater.from(context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
        holder.itemView.setOnClickListener{
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClick(products[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class ProductViewHolder(private val binding: ItemviewProductBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product){
            binding.apply {
                tvProductName.text = product.name
                tvProductType.text = product.type
                tvProductPrice.text = String.format("%.2f", product.price.toDoubleOrNull())
                tvProductTax.text = "tax: ${String.format("%.2f", product.tax.toDoubleOrNull())}"
                UiComponentUtils.loadImageFromUrl(context, product.image, ivProductImage, R.drawable.ic_default_product)
            }
        }
    }
}
