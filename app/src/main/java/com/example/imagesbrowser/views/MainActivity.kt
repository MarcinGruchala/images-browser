package com.example.imagesbrowser.views

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imagesbrowser.R
import com.example.imagesbrowser.adapters.ImagesListAdapter
import com.example.imagesbrowser.databinding.ActivityMainBinding
import com.example.imagesbrowser.models.DownloadingImagesStatus
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
        val loadingDialog = Dialog(this).apply {
            setContentView(R.layout.dialog_loading)
            setCancelable(false)
        }

        val imageBitmapListObserver = Observer<List<Bitmap>> {
            if (viewModel.imagesBitmapList.value != null && viewModel.imagesBitmapList.value!!.isNotEmpty()) {
                Log.d(TAG, "Image list: ${viewModel.imagesBitmapList.value}")
                updateImagesList()
            }
        }
        viewModel.imagesBitmapList.observe(this, imageBitmapListObserver)

        val downloadingImagesStatusObserver = Observer<DownloadingImagesStatus> { status ->
            when(status) {
                DownloadingImagesStatus.STARTED -> loadingDialog.show()
                DownloadingImagesStatus.ENDED -> loadingDialog.dismiss()
                else -> {}
            }
        }
        viewModel.downloadingImagesStatus.observe(this,downloadingImagesStatusObserver)
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
            viewModel.imagesListResponseBody.value!!.clear()
            viewModel.imagesBitmapList.value = listOf()
            viewModel.fetchData()
        }
    }

}