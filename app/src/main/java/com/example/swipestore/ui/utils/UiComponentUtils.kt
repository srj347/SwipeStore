package com.example.swipestore.ui.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.swipestore.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

object UiComponentUtils {
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun showCancelDialog(context: Context, onSheetDiscard: () -> Unit) {
        MaterialAlertDialogBuilder(
            context,
            R.style.ThemeOverlay_App_MaterialAlertDialog1
        )
            .setTitle("${context.getString(R.string.discard_changes)}?")
            .setMessage(context.getString(R.string.discard_detail_message))
            .setNegativeButton(context.getString(R.string.cancel_btn)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Discard") { dialog, _ ->
                dialog.dismiss()
                onSheetDiscard()
            }
            .show()
    }

    fun showSnackBar(view: View, message: String){
        Snackbar.make(
            view,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    fun hideSoftKeyboard(view : View, activity: Activity) {
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun loadImageFromUrl(
        context: Context,
        imageUrl: String,
        imageContainer: ImageView,
        defaultImageId: Int
    ){
        if(imageUrl.isNullOrEmpty()){
            imageContainer.setImageResource(defaultImageId)
        } else {
            Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .centerCrop()
                .into(imageContainer)
        }
    }
}