package com.example.swipestore.ui.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NetworkChangeReceiver(val onNetworkChange: (status: Boolean) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val status: Int = NetworkUtil.getConnectivityStatusString(context)

        if (intent.action == "android.net.conn.CONNECTIVITY_CHANGE") {
            if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                onNetworkChange(false)

            } else {
                onNetworkChange(true)
            }
        }
    }
}