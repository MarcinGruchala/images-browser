package com.example.imagesbrowser.viewmodels

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.example.imagesbrowser.models.ImagesListResponse
import com.example.imagesbrowser.repository.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(
    application: Application,
    private val repository: RepositoryImpl
) : AndroidViewModel(application) {
    private val applicationContext = application.applicationContext

    var currentPageNumber =  0

    val imagesListResponseBody: MutableLiveData<ImagesListResponse> by lazy {
        MutableLiveData<ImagesListResponse>()
    }

    val imagesBitmapList: MutableLiveData<List<Bitmap>> by lazy {
        MutableLiveData<List<Bitmap>>()
    }

    init {
        fetchData()
    }
    fun fetchData() {
        viewModelScope.launch {
            Log.d("MainActivity", "Images download start")
            val response = repository.getImageList(getPageNumber(), 20)
            if (response.isSuccessful && response.body() != null ){
                imagesListResponseBody.value = response.body()
                downloadImagesBitmapList()
            }
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

     private suspend fun downloadImagesBitmapList() {
         val list = mutableListOf<Bitmap>()
         val job = viewModelScope.launch(Dispatchers.IO) {
             for (item in imagesListResponseBody.value!!) {
                 val bitmap = Glide.with(applicationContext)
                     .asBitmap()
                     .load(item.download_url)
                     .apply(RequestOptions.overrideOf(item.width/4,item.height/4))
                     .submit().get()
                 list.add(bitmap)
             }
         }
         job.join()
         imagesBitmapList.value = list
         Log.d("MainActivity", "Images download ended")
    }

}