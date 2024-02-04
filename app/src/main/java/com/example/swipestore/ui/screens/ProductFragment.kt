package com.example.swipestore.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.swipestore.R
import com.example.swipestore.data.entities.Product
import com.example.swipestore.databinding.FragmentProductBinding
import com.example.swipestore.ui.utils.UiComponentUtils
import com.example.swipestore.ui.viewmodels.ProductViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class ProductFragment : Fragment() {

    private val binding by lazy { FragmentProductBinding.inflate(layoutInflater) }
    private val viewmodel by sharedViewModel<ProductViewModel>()
    private val args: ProductFragmentArgs by navArgs()
    private var selectedPrduct: Product? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        selectedPrduct = args.keyProduct
        setProductData(selectedPrduct)
    }

    private fun setProductData(product: Product?) {
        val productView = binding.itemviewProduct
        productView.tvProductName.text = product?.name
        productView.tvProductType.text = product?.type
        productView.tvProductPrice.text = product?.price
        productView.tvProductTax.text = product?.tax
        UiComponentUtils.loadImageFromUrl(requireContext(), product?.image ?: "", productView.ivProductImage, R.drawable.ic_default_product)
    }


    private fun setToolbar() {
        val toolbar = binding.customToolbar
        toolbar.toolbarIcBack.visibility = View.VISIBLE
        toolbar.toolbarTitle.text = resources.getText(R.string.toolbar_title_product_view)

        toolbar.toolbarIcBack.setOnClickListener{
            activity?.supportFragmentManager?.popBackStack()
        }
    }
}