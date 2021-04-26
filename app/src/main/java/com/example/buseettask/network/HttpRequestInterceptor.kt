package com.example.buseettask.network

import okhttp3.Interceptor
import okhttp3.Response

class HttpRequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()

        val originalUrl = originalRequest.url

        val url = originalUrl.newBuilder().build()

        val requestBuilder = originalRequest.newBuilder().url(url)
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
        val request = requestBuilder.build()

        val response = chain.proceed(request)

        response.code//status code
        return response

    }
}