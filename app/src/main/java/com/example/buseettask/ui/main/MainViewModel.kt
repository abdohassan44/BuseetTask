package com.example.buseettask.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buseettask.network.ApiResponse
import com.example.buseettask.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: RestaurantRepository
) : ViewModel() {
    private val _status = MutableLiveData<Status<ApiResponse>>()
    val status: LiveData<Status<ApiResponse>> get() = _status


     fun getRestaurantsList(limit: Int, latitiude: Double, longtiude: Double) {
        viewModelScope.launch {
            repository.getRestaurants(
                limit = limit,
                latitiude=latitiude,
                longtiude=longtiude,
                onResult = {
                    _status.postValue(it)
                }
            )
        }
    }


}