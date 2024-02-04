package com.example.swipestore.ui.screens

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swipestore.R
import com.example.swipestore.data.entities.Product
import com.example.swipestore.databinding.FragmentProductListBinding
import com.example.swipestore.ui.utils.NetworkChangeReceiver
import com.example.swipestore.ui.utils.ProgressLoaderDialog
import com.example.swipestore.ui.utils.UiComponentUtils
import com.example.swipestore.ui.adapters.ProductAdapter
import com.example.swipestore.ui.uistates.UiState
import com.example.swipestore.ui.viewmodels.ProductViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class ProductListFragment : Fragment() {

    private lateinit var networkChangeReceiver: NetworkChangeReceiver
    private val navController by lazy { findNavController() }
    private val binding by lazy { FragmentProductListBinding.inflate(layoutInflater) }
    private val viewmodel: ProductViewModel by sharedViewModel()
    private var products: ArrayList<Product>? = null
    private lateinit var productAdapter: ProductAdapter
    private var isNetworkAvailable: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNetworkChangeListener()
        setToolbar()
        setViewListeners()
        observeData()
        fetchProductApiCall()
    }

    private fun setNetworkChangeListener() {
        // Registering broadcast receiver for network change
        networkChangeReceiver = NetworkChangeReceiver { isConnected ->
            isNetworkAvailable = isConnected
            if(isNetworkAvailable){
                binding.noInternetIv.visibility = View.GONE
                fetchProductApiCall()
            }
        }
//        Todo: update the deprecated attribute
        requireActivity().registerReceiver(
            networkChangeReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    //    Todo: make it abstract and add in the BaseFragment, to make it dynamic and reusable
    private fun setToolbar() {
        val toolbar = binding.customToolbar
        toolbar.toolbarIcSearch.visibility = View.VISIBLE
        toolbar.toolbarTitle.text = resources.getText(R.string.toolbar_title_product_list)

        toolbar.toolbarIcSearch.setOnClickListener{
//            Todo: Instead of replace operation on the destination, add destination to preserve the state of current screen
            val direction = ProductListFragmentDirections.actionProductListToSearch()
            navController.navigate(direction)
        }
    }

    private fun setViewListeners() {
        binding.fabCreateProduct.setOnClickListener {
            val direction = ProductListFragmentDirections.actionProductListToCreateProduct()
            navController.navigate(direction)
        }
    }

    private fun fetchProductApiCall() {
        viewmodel.fetchProducts()
    }

    private fun observeData() {
        viewmodel.uiState.observe(viewLifecycleOwner){ result ->
            when (result) {
                is UiState.Success<*> -> {
                    ProgressLoaderDialog.hideLoader()
                    products = result.data as? ArrayList<Product>
                    if(!products.isNullOrEmpty()){
                        updateUI(products!!)
                    } else {
                        setNetworkNotAvailable()
                    }
                }
                is UiState.Error -> {
                    ProgressLoaderDialog.hideLoader()
                    setNetworkNotAvailable()
                }
                else -> {
                    ProgressLoaderDialog.showLoader(requireContext(), "Fetching Products...")
                }
            }
        }
    }

    private fun setNetworkNotAvailable(){
        // Internet connection not available
        if(!isNetworkAvailable){
            binding.noInternetIv.visibility = View.VISIBLE
        } else {
            UiComponentUtils.showSnackBar(binding.root, "Please try again, failed to fetch the product..")
        }
    }

    private fun updateUI(data: ArrayList<Product>) {
        productAdapter = ProductAdapter(requireActivity(), products!!){ data ->
            val direction = ProductListFragmentDirections.actionProductListToProduct(data)
            navController.navigate(direction)
        }
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = productAdapter
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().unregisterReceiver(networkChangeReceiver)
    }
}