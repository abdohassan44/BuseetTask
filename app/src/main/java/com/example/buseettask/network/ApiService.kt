package com.example.buseettask.network

import com.example.buseettask.utils.Constant
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(Constant.Get_RESTAURANTS)
    fun getRestaurants(
        @Query("categoryId") categoryId: String,
        @Query("ll") ll: String,
        @Query("limit") limit: Int,
        @Query("v") v: String,
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
    ): Deferred<ApiResponse>
}