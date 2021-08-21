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
import com.example.imagesbrowser.databinding.ActivityImagesListBinding
import com.example.imagesbrowser.models.DownloadingImagesStatus
import com.example.imagesbrowser.viewmodels.MainActivityViewModel
import com.example.imagesbrowser.views.utils.AlertDialogsUtils
import com.example.imagesbrowser.views.utils.ConnectivityManagerUtils
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class ImagesListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImagesListBinding
    private lateinit var connectivityManagerUtils: ConnectivityManagerUtils
    private lateinit var alertDialogsUtils: AlertDialogsUtils
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagesListBinding.inflate(layoutInflater)
        connectivityManagerUtils = ConnectivityManagerUtils(
            this,
            this,
            viewModel
        )
        alertDialogsUtils = AlertDialogsUtils(this)
        connectivityManagerUtils.registerNetworkCallback()
        setContentView(binding.root)
        setImagesList()
        setObservers()
        setClickListeners()
    }

    override fun onStop() {
        super.onStop()
        connectivityManagerUtils.unRegisterNetworkCallback()
    }

    private fun setImagesList() {
        binding.rvImagesList.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun setObservers() {
        val isInternetConnectionObserver = Observer<Boolean> { status ->
            if (!status) {
                Log.d(TAG,"Lost internet connection")
                alertDialogsUtils.showNoInternetAlertDialog()
            } else {
                Log.d(TAG,"Has internet connection")
                if (viewModel.imagesListResponseBody.value == null) {
                    alertDialogsUtils.dismissNoInternetAlertDialog()
                    viewModel.fetchData()
                }
            }
        }
        viewModel.isInternetConnection.observe(this, isInternetConnectionObserver)

        val imageBitmapListObserver = Observer<List<Bitmap>> {
            if (viewModel.imagesBitmapsList.value != null && viewModel.imagesBitmapsList.value!!.isNotEmpty()) {
                Log.d(TAG, "Image list: ${viewModel.imagesBitmapsList.value}")
                updateImagesList()
            }
        }
        viewModel.imagesBitmapsList.observe(this, imageBitmapListObserver)

        val loadingDialog = Dialog(this).apply {
            setContentView(R.layout.dialog_loading)
            setCancelable(false)
        }
        val downloadingImagesStatusObserver = Observer<DownloadingImagesStatus> { status ->
            when(status) {
                DownloadingImagesStatus.STARTED -> loadingDialog.show()
                DownloadingImagesStatus.ENDED -> loadingDialog.dismiss()
                DownloadingImagesStatus.ERROR ->
                    alertDialogsUtils.showDownloadingDataErrorAlertDialog()
                else -> {}
            }
        }
        viewModel.downloadingImagesStatus.observe(this,downloadingImagesStatusObserver)
    }

    private fun updateImagesList() {
        binding.rvImagesList.adapter = ImagesListAdapter(
            viewModel.imagesListResponseBody.value!!,
            viewModel.imagesBitmapsList.value!!,
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
            if (viewModel.isInternetConnection.value != null &&
                    viewModel.isInternetConnection.value == true) {
                viewModel.imagesListResponseBody.value!!.clear()
                viewModel.imagesBitmapsList.value = listOf()
                viewModel.fetchData()
            } else {
                alertDialogsUtils.showNoInternetAlertDialog()
            }
        }
    }
}