package com.example.imagesbrowser.repository

import com.example.imagesbrowser.models.remote.ImagesListResponse
import com.example.imagesbrowser.webservice.PicsumService
import retrofit2.Response

class RepositoryImpl(
    private val webService: PicsumService
): Repository {

    override suspend fun getImageList(
        page: Int,
        limit: Int
    ): Response<ImagesListResponse> = webService.getImageLis(page, limit)
}