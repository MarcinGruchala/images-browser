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

    var currentPageNumber =  0

    val imagesList: MutableLiveData<ImagesListResponse> by lazy {
        MutableLiveData<ImagesListResponse>()
    }

    init {
        updateImagesList()
    }

    fun updateImagesList() {
        viewModelScope.launch {
            imagesList.value = repository.getImageList(getPageNumber(),20).body()
        }
    }

    private fun getPageNumber():Int {
        if (currentPageNumber < 51) {
            currentPageNumber += 1
            return currentPageNumber
        }
        currentPageNumber = 0
        return currentPageNumber
    }
}