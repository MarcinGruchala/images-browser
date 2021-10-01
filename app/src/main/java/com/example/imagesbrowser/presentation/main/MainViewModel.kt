package com.example.imagesbrowser.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imagesbrowser.networking.model.ImagesListResponseItem

class MainViewModel: ViewModel() {

    val isInternetConnection: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    val imageDetails: MutableLiveData<ImagesListResponseItem> by lazy {
        MutableLiveData<ImagesListResponseItem>()
    }


}
