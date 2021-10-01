package com.example.imagesbrowser.presentation.imageDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imagesbrowser.networking.model.ImagesListResponseItem

class ImageDetailsViewModel: ViewModel() {

    val imageDetails: MutableLiveData<ImagesListResponseItem> by lazy {
        MutableLiveData<ImagesListResponseItem>()
    }

}
