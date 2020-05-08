package com.photowalk.android.networking

import com.photowalk.android.data.PhotosSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("services/rest/")
    suspend fun fetchImages(
        @Query("method") method: String,
        @Query("format") format: String,
        @Query("tags") tags: String,
        @Query("nojsoncallback") nojsoncallback: Short,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("radius") radius: Int,
        @Query("per_page") per_page: Int,
        @Query("page") page: Int,
        @Query("accuracy") accuracy: Int,
        @Query("api_key") api_key: String
    ): PhotosSearchResponse
}
