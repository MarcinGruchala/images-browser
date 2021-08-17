package com.example.imagesbrowser.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.imagesbrowser.viewmodels.MainActivityViewModel
import com.example.imagesbrowser.databinding.ActivityMainBinding
import com.example.imagesbrowser.models.ImagesListResponse
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageListObserver = Observer<ImagesListResponse> { list ->
            Log.d(TAG, "Image list: ${viewModel.imagesList.value}")
        }
        viewModel.imagesList.observe(this, imageListObserver)

    }


}