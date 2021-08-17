package com.example.imagesbrowser.repository

import android.graphics.pdf.PdfDocument
import com.example.imagesbrowser.models.ImagesListResponse
import retrofit2.Response

interface Repository {

    suspend fun getImageList(
        page: Int,
        limit: Int
    ): Response<ImagesListResponse>
}