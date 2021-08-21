package com.example.imagesbrowser.repository

import com.example.imagesbrowser.models.remote.ImagesListResponse
import retrofit2.Response

interface Repository {

    suspend fun getImageList(
        page: Int,
        limit: Int
    ): Response<ImagesListResponse>
}