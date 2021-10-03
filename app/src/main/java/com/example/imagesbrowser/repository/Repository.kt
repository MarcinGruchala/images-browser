package com.example.imagesbrowser.repository

import com.example.imagesbrowser.networking.model.ImagesListResponse
import io.reactivex.rxjava3.core.Observable

interface Repository {

    suspend fun getImageList(
        page: Int,
        limit: Int
    ): Observable<ImagesListResponse>
}
