package com.example.swipestore.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swipestore.data.entities.Product
import com.example.swipestore.databinding.FragmentSearchBinding
import com.example.swipestore.ui.adapters.SearchAdapter
import com.example.swipestore.ui.viewmodels.ProductViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class SearchFragment : Fragment() {

    private val binding by lazy { FragmentSearchBinding.inflate(layoutInflater) }
    private lateinit var products: ArrayList<Product>
    private lateinit var searchAdapter: SearchAdapter
    private val viewModel: ProductViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        products = viewModel.products.value ?: ArrayList()
        setUpSearchAdapter()
        setSearchQueryChangeListener()
    }

    private fun setUpSearchAdapter() {
        searchAdapter = SearchAdapter(requireActivity(),products){data ->
            viewModel.products.value = products
            val direction = SearchFragmentDirections.actionSearchToProduct(data)
            findNavController().navigate(direction)
        }
        binding.recyclerPr.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun setSearchQueryChangeListener(){
        binding.etSearch.setOnClickListener{
            binding.etSearch.isIconified = false
        }
        binding.etSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchAdapter.filter.filter(newText)
                return true
            }
        })
    }
}