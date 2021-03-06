package com.example.imagesbrowser.views.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.imagesbrowser.viewmodels.MainActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConnectivityManagerUtils(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    viewModel: MainActivityViewModel
) {
    private val connectivityManager = context.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                viewModel.isInternetConnection.value = true
            }
        }
        override fun onLost(network: Network) {
            lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                viewModel.isInternetConnection.value = false
            }
        }
    }

    fun registerNetworkCallback() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    fun unRegisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}