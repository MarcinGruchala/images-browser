package com.example.imagesbrowser.repository

import com.example.imagesbrowser.networking.model.ImagesListResponse
import com.example.imagesbrowser.networking.service.PicsumService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response

class RepositoryImpl(
    private val webService: PicsumService
): Repository {

    override suspend fun getImageList(
        page: Int,
        limit: Int
    ): Observable<ImagesListResponse> =
        webService.getImageLis(page, limit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


}
