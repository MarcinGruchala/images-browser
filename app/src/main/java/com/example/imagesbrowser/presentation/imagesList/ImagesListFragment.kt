package com.example.imagesbrowser.presentation.imagesList

import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imagesbrowser.R
import com.example.imagesbrowser.databinding.FragmentImagesListBinding
import com.example.imagesbrowser.presentation.base.BaseFragment
import com.example.imagesbrowser.presentation.common.AlertDialogsUtils
import com.example.imagesbrowser.presentation.common.DownloadingImagesStatus
import com.example.imagesbrowser.presentation.main.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImagesListFragment : BaseFragment() {
    private lateinit var binding: FragmentImagesListBinding

    private lateinit var alertDialogsUtils: AlertDialogsUtils


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImagesListBinding.inflate(inflater, container, false)
        alertDialogsUtils = AlertDialogsUtils(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setImagesList()
        setObservers()
        setClickListeners()
    }

    private fun setImagesList() {
        binding.rvImagesList.layoutManager = LinearLayoutManager(
            requireContext(),
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
        viewModel.isInternetConnection.observe(requireActivity(), isInternetConnectionObserver)

        val imageBitmapListObserver = Observer<List<Bitmap>> {
            if (viewModel.imagesBitmapsList.value != null &&
                viewModel.imagesBitmapsList.value!!.isNotEmpty()) {
                updateImagesList()
            }
        }
        viewModel.imagesBitmapsList.observe(requireActivity(), imageBitmapListObserver)

        val loadingDialog = Dialog(requireContext()).apply {
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
        viewModel.downloadingImagesStatus.observe(requireActivity(),downloadingImagesStatusObserver)
    }

    private fun updateImagesList() {
        binding.rvImagesList.adapter = ImagesListAdapter(
            viewModel.imagesListResponseBody.value!!,
            viewModel.imagesBitmapsList.value!!,
            itemClickListener = { item ->
                viewModel.imageDetails.value = item
                view?.let {
                    Navigation.findNavController(it).navigate(R.id.action_imagesListFragment_to_imageDetailsFragment)
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
