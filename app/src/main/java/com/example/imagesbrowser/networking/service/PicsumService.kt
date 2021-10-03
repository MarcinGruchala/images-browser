package com.example.imagesbrowser.networking.service

import com.example.imagesbrowser.networking.model.ImagesListResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PicsumService {

    @GET("v2/list")
    suspend fun getImageLis(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ) : Observable<ImagesListResponse>
}
