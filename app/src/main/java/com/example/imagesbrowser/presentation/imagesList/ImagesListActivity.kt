package com.example.imagesbrowser.presentation.imagesList

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imagesbrowser.*
import com.example.imagesbrowser.databinding.ActivityImagesListBinding
import com.example.imagesbrowser.presentation.common.DownloadingImagesStatus
import com.example.imagesbrowser.presentation.imageDetails.ImageDetailsActivity
import com.example.imagesbrowser.presentation.common.AlertDialogsUtils
import com.example.imagesbrowser.presentation.common.ConnectivityManagerUtils
import com.example.imagesbrowser.presentation.main.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImagesListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImagesListBinding
    private val viewModel: MainActivityViewModel by viewModels()

    private lateinit var connectivityManagerUtils: ConnectivityManagerUtils
    private lateinit var alertDialogsUtils: AlertDialogsUtils

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
                alertDialogsUtils.showNoInternetAlertDialog()
            } else {
                if (viewModel.imagesListResponseBody.value == null) {
                    alertDialogsUtils.dismissNoInternetAlertDialog()
                    viewModel.fetchData()
                }
            }
        }
        viewModel.isInternetConnection.observe(this, isInternetConnectionObserver)

        val imageBitmapListObserver = Observer<List<Bitmap>> {
            if (viewModel.imagesBitmapsList.value != null &&
                viewModel.imagesBitmapsList.value!!.isNotEmpty()) {
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
                viewModel.imageDetails.value = item
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
