package com.example.swipestore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.swipestore.data.entities.Product
import com.example.swipestore.databinding.ItemviewProductSearchBinding
import java.io.File


class SearchAdapter(
    private val context: Context,
    private val products: ArrayList<Product>,
    private val onItemClick: (data: Product) -> Unit

): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>(),Filterable {
    var filterList:ArrayList<Product> = products
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemviewProductSearchBinding.inflate(LayoutInflater.from(context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val product = filterList[position]
        holder.bind(product)
        holder.itemView.setOnClickListener{
            onItemClick(product)
        }
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    inner class SearchViewHolder(private val binding: ItemviewProductSearchBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product){
            binding.apply {
                nameProduct.text = product.name
                priceProduct.text = "\u20B9 ${String.format("%.2f", product.price.toDoubleOrNull())}"
                val imgString = product.image
                Glide.with(context).asFile()
                    .load(imgString)
                    .into(object : SimpleTarget<File>() {
                        override fun onResourceReady(
                            resource: File,
                            transition: Transition<in File>?
                        ) {
                            val filePath = resource.absolutePath
                            Glide.with(context).load(filePath).into(productImg)
                        }
                    })
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint.isNullOrBlank()|| constraint.length < 3) {
                    filterResults.values = products
                } else {
                    val filteredItems = products.filter {
                        it.name.contains(constraint, ignoreCase = true)
                    }
                    filterResults.values = filteredItems
                }
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterList = (results?.values as? List<Product> ?: emptyList()) as ArrayList<Product>
                notifyDataSetChanged()
            }
        }
    }
}
