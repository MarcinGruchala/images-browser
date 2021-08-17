package com.example.imagesbrowser.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imagesbrowser.adapters.ImagesListAdapter
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

        setImagesList()
        setObservers()

    }

    private fun setObservers() {
        val imageListObserver = Observer<ImagesListResponse> { list ->
            Log.d(TAG, "Image list: ${viewModel.imagesList.value}")
            updateImagesList()
        }
        viewModel.imagesList.observe(this, imageListObserver)
    }

    private fun setImagesList() {
        binding.rvImagesList.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun updateImagesList() {
        binding.rvImagesList.adapter = ImagesListAdapter(viewModel.imagesList.value!!)
    }


}