package com.example.buseettask.ui.main

import com.example.buseettask.network.ApiResponse
import com.example.buseettask.network.ApiService
import com.example.buseettask.utils.Constant
import com.example.buseettask.utils.RemoteException
import com.example.buseettask.utils.Status
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class RestaurantRepository @Inject constructor(private val apiClient: ApiService) {


    private val TAG = "RestaurantRepository"
    suspend fun getRestaurants(
        limit: Int, latitiude: Double, longtiude: Double,
        onResult: suspend (Status<ApiResponse>) -> Unit
    ) = withContext(Dispatchers.IO) {
        onResult(Status.Loading)

        try {
            val response = apiClient.getRestaurants(
                limit = limit,
                ll = "$latitiude,$longtiude",
                categoryId = Constant.CATEGORY_ID,
                clientId = Constant.CLIENT_ID,
                clientSecret = Constant.CLIENT_SECRET,
                v = Constant.V
            ).await()

            when {
                response != null -> {

                    onResult(Status.Success(response))
                }


                else ->
                    onResult(Status.Error(RemoteException("error")))
            }

        } catch (t: Throwable) {
            if (t !is CancellationException)
                onResult(Status.Error(t))
        }
    }

}