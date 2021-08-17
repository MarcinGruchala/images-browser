package com.example.imagesbrowser.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagesbrowser.models.ImagesListResponse
import com.example.imagesbrowser.repository.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: RepositoryImpl
) : ViewModel() {

    val imagesList: MutableLiveData<ImagesListResponse> by lazy {
        MutableLiveData<ImagesListResponse>()
    }

    init {
        updateImagesList()
    }


    private fun updateImagesList() {

        viewModelScope.launch {
            imagesList.value = repository.getImageList(1,20).body()
        }

    }
}