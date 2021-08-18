package com.example.imagesbrowser.views

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imagesbrowser.adapters.ImagesListAdapter
import com.example.imagesbrowser.databinding.ActivityMainBinding
import com.example.imagesbrowser.viewmodels.MainActivityViewModel
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
        setClickListeners()
    }

    private fun setImagesList() {
        binding.rvImagesList.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun setObservers() {
        val imageBitmapListObserver = Observer<List<Bitmap>> {
            if (viewModel.imagesBitmapList.value != null) {
                Log.d(TAG, "Image list: ${viewModel.imagesBitmapList.value}")
                updateImagesList()
            }
        }
        viewModel.imagesBitmapList.observe(this, imageBitmapListObserver)
    }

    private fun updateImagesList() {
        binding.rvImagesList.adapter = ImagesListAdapter(
            viewModel.imagesListResponseBody.value!!,
            viewModel.imagesBitmapList.value!!,
            itemClickListener = { item ->
                Intent(this, ImageDetailsActivity::class.java).also {
                    it.putExtra("ITEM_DETAILS", item)
                    startActivity(it)
                }
            }
        )
    }

    private fun setClickListeners() {
        binding.btnRefreshList.setOnClickListener {
            viewModel.fetchData()
        }
    }

}