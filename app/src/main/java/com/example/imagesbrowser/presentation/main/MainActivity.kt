package com.example.imagesbrowser.presentation.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.imagesbrowser.databinding.ActivityMainBinding
import com.example.imagesbrowser.presentation.common.ConnectivityManagerUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    private lateinit var connectivityManagerUtils: ConnectivityManagerUtils


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        connectivityManagerUtils = ConnectivityManagerUtils(this, this, viewModel)
        connectivityManagerUtils.registerNetworkCallback()
        setContentView(binding.root)
    }

    override fun onStop() {
        super.onStop()
        connectivityManagerUtils.unRegisterNetworkCallback()
    }
}
