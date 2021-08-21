package com.example.imagesbrowser.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.imagesbrowser.di.ImagesBrowserApplication
import com.example.imagesbrowser.models.DownloadingImagesStatus
import com.example.imagesbrowser.models.ImageSize
import com.example.imagesbrowser.models.remote.ImagesListResponse
import com.example.imagesbrowser.repository.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.random.Random

private const val IMAGES_IN_LIST = 20
private const val PAGES_LIMIT = 50
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val application: ImagesBrowserApplication,
    private val repository: RepositoryImpl
) : AndroidViewModel(application) {

    private var currentPageNumber =  Random.nextInt(1,51)

    val isInternetConnection: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    val imagesListResponseBody: MutableLiveData<ImagesListResponse> by lazy {
        MutableLiveData<ImagesListResponse>()
    }

    val imagesBitmapsList: MutableLiveData<List<Bitmap>> by lazy {
        MutableLiveData<List<Bitmap>>()
    }

    val downloadingImagesStatus: MutableLiveData<DownloadingImagesStatus> by lazy {
        MutableLiveData<DownloadingImagesStatus>()
    }

    fun fetchData() {
        viewModelScope.launch {
            downloadingImagesStatus.value = DownloadingImagesStatus.STARTED
            val response = try {
                repository.getImageList(getPageNumber(), IMAGES_IN_LIST)
            } catch (e: Exception) {
                downloadingImagesStatus.value = DownloadingImagesStatus.ERROR
                return@launch
            }
            if (response.isSuccessful && response.body() != null ) {
                imagesListResponseBody.value = response.body()
                downloadImagesBitmapList()
            } else {
                downloadingImagesStatus.value = DownloadingImagesStatus.ERROR
            }
        }
    }

    private fun getPageNumber():Int {
        if (currentPageNumber <= PAGES_LIMIT) {
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
                 val newImageSize = getNewImageSize(item.width,item.height)
                 val bitmap = Glide.with(application.applicationContext)
                     .asBitmap()
                     .load(item.download_url)
                     .apply(RequestOptions.overrideOf(newImageSize.width,newImageSize.height))
                     .submit().get()
                 list.add(bitmap)
             }
         }
         job.join()
         imagesBitmapsList.value = list
         downloadingImagesStatus.value = DownloadingImagesStatus.ENDED
    }

    private fun getNewImageSize(
        width: Int,
        height: Int
    ): ImageSize {
        if (width > height) {
            return ImageSize(720,576)
        }
        return ImageSize(567,720)
    }

}