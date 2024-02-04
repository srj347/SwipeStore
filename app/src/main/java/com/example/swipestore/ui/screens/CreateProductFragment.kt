package com.example.swipestore.ui.screens

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.navigation.fragment.findNavController
import com.example.swipestore.R
import com.example.swipestore.data.entities.Product
import com.example.swipestore.data.entities.ProductRequest
import com.example.swipestore.databinding.FragmentCreateProductBinding
import com.example.swipestore.ui.utils.ProgressLoaderDialog
import com.example.swipestore.ui.utils.UiComponentUtils
import com.example.swipestore.ui.uistates.UiState
import com.example.swipestore.ui.viewmodels.ProductViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream


class CreateProductFragment : BottomSheetDialogFragment() {

    private val binding by lazy { FragmentCreateProductBinding.inflate(layoutInflater) }
    private var productToUpload: Product? = null
    private var fileToUpload: File? = null
    private val viewmodel by viewModel<ProductViewModel>()
    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        val sheetBehavior = BottomSheetBehavior.from(binding.bottomSheetCreateProduct)
        sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        setUpProductTypeDropdown()
        setUpViewListeners()
    }

    private fun setUpViewListeners() {
        binding.ivAdd.setOnClickListener{
            pickImageFromGallery()
        }
        binding.btnAddProduct.setOnClickListener {
            UiComponentUtils.hideSoftKeyboard(it, requireActivity())
            saveProduct()
        }

        viewmodel.uiState.observe(viewLifecycleOwner){ result ->
            when (result) {
                is UiState.Success<*> -> {
                    ProgressLoaderDialog.hideLoader()
                    val product = result.data as? Product
                    if(product != null){
                        UiComponentUtils.showSnackBar(binding.root, "${product.name} added successfully")
                        val direction = CreateProductFragmentDirections.actionCreateProductFragmentToProductListFragment()
                        navController.navigate(direction)
                    } else {
                        ProgressLoaderDialog.hideLoader()
                        UiComponentUtils.showSnackBar(binding.root, "Please try again, failed to upload the product..")
                        dismiss()
                    }
                }
                is UiState.Error -> {
                    UiComponentUtils.showSnackBar(binding.root, "Please try again, failed to upload the product..")
                    dismiss()
                }
                else -> {
                    ProgressLoaderDialog.showLoader(requireContext(), "Uploading...")
                }
            }
        }
    }

    private fun setToolbar() {
        val toolbar = binding.customToolbar
        toolbar.toolbarIcBack.visibility = View.VISIBLE
        toolbar.toolbarTitle.text = resources.getText(R.string.toolbar_title_product_create)

        toolbar.toolbarIcBack.setOnClickListener{
            val (productName, productType, _, productPrice, tax) = getProductFormData()

            if(isFormInProgress(productName, productType, productPrice, tax)){
                UiComponentUtils.showCancelDialog(requireActivity()){
                    dismiss()
                }
            } else {
                dismiss()
            }
        }
    }

    private fun setUpProductTypeDropdown(){
        val productTypeSpinner: Spinner = binding.ddProductType
        val productTypes = resources.getStringArray(R.array.spinner_items)

        val productTypeAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, productTypes)
        productTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        productTypeSpinner.adapter = productTypeAdapter
    }

    private fun saveProduct() {
        val product = getProductFormData()
        val (productName, productType, _, productPrice, tax) = product

        if(!isMandatoryAttributeFilled(productName, productType, productPrice, tax)){
            UiComponentUtils.showToast(requireActivity(), "Please fill all mandatory fields")
        }else{
            postProductData(product, fileToUpload)
        }
    }

    private fun isMandatoryAttributeFilled(productName: String, productType: String, productPrice: String, tax: String): Boolean {
        if(productName.isNullOrEmpty() || productType == "Select Product Type" || productPrice.isNullOrEmpty() || tax.isNullOrEmpty()){
            return false
        }
        return true
    }

    private fun isFormInProgress(productName: String, productType: String, productPrice: String, tax: String): Boolean {
        if(productName.isNullOrEmpty() && (productType == "Select Product Type") && productPrice.isNullOrEmpty() && tax.isNullOrEmpty()){
            return false
        }
        return true
    }


    private fun postProductData(product: Product, file: File?) {
        val request = getRequestParams(product, fileToUpload)
        viewmodel.createProduct(request)
    }

    private fun pickImageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_PICK_IMAGE -> {
                    try {
                        val selectedImageUri = data?.data
                        selectedImageUri?.let {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                requireActivity().contentResolver,
                                it
                            )
                            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 240, 240, true)
                            binding.ivAdd.setImageBitmap(resizedBitmap)
                        }

                        fileToUpload = selectedImageUri?.let { uri ->
                            val file = File(requireContext().filesDir, "image.jpg")
                            val inputStream = requireContext().contentResolver.openInputStream(uri)
                            val outputStream = FileOutputStream(file)
                            inputStream?.copyTo(outputStream)
                            file
                        }

                        UiComponentUtils.showToast(requireActivity(), "Image selected")
                    } catch (e: Exception) {
                        UiComponentUtils.showToast(requireActivity(), "Failed to select the image")
                    }
                }
            }
        }
    }

    private fun getRequestParams(product: Product, fileToUpload: File?): ProductRequest {
        val nameRequestBody = product.name.toRequestBody("text/plain".toMediaTypeOrNull())
        val typeRequestBody = product.type.toRequestBody("text/plain".toMediaTypeOrNull())
        val priceRequestBody = product.price.toRequestBody("text/plain".toMediaTypeOrNull())
        val taxRequestBody = product.tax.toRequestBody("text/plain".toMediaTypeOrNull())

        val filePart: MultipartBody.Part? = fileToUpload?.let {
            val fileRequestBody: RequestBody = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("files[]", it.name ?: "", fileRequestBody)
        }

        return ProductRequest(
            mutableMapOf(
                "product_name" to nameRequestBody,
                "product_type" to typeRequestBody,
                "price" to priceRequestBody,
                "tax" to taxRequestBody
            ),
            filePart
        )
    }

    private fun getProductFormData(): Product {
        val productName = binding.etProductName.text.toString()
        val productType = binding.ddProductType.selectedItem.toString()
        val productPrice = binding.etProductPrice.text.toString()
        val tax = binding.etTax.text.toString()

        return Product(productName, productType, "", productPrice, tax)
    }


    companion object {
        private const val REQUEST_PICK_IMAGE = 1

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateProductFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}