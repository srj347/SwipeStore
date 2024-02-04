package com.example.swipestore.ui.utils

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.TextView
import com.example.swipestore.R

object ProgressLoaderDialog {
    private var loader: Dialog? = null

    fun showLoader(context: Context, message: String) {
        if(loader?.isShowing == true) return
        loader = Dialog(context)
        loader?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loader?.setContentView(R.layout.layout_loader_dialog)
        loader?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        loader?.setCancelable(false)
        val tvMessage = loader?.findViewById<TextView>(R.id.tv_message)
        tvMessage?.text = message
        loader?.show()
    }

    fun hideLoader() {
        if(loader?.isShowing == false) return
        loader?.dismiss()
    }
}