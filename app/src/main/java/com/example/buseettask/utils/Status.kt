package com.example.buseettask.utils

import android.app.Activity
import android.content.Context
import com.example.buseettask.R
import retrofit2.HttpException
import java.io.IOException


sealed class Status<out T> {

    object Loading : Status<Nothing>()

    data class Success<T>(val data: T) : Status<T>()

    data class Error(val throwable: Throwable) : Status<Nothing>() {
        fun getErrorMessage(): Int {
            return when (throwable) {
                is IOException -> {
                    R.string.no_internet_connection
                }

                is HttpException -> {
                    R.string.app_name
                }
                else -> {
                    R.string.app_name
                }
            }
        }

        fun getMessage(context: Context): String {

            return when (throwable) {
                is RemoteException -> throwable.errorMessage ?: context.getString(R.string.error)
                is IOException -> context.getString(R.string.no_internet_connection)
                else -> context.getString(R.string.error)
            }
        }

        fun getMessage(activity: Activity): ErrorData {

            return when (throwable) {
                is RemoteException -> ErrorData(
                    message = throwable.errorMessage ?: activity.getString(R.string.error),
                    iconRes = R.drawable.ic_nowifi,
                )
                is IOException -> ErrorData(
                    message = activity.getString(R.string.no_internet_connection),
                    iconRes = R.drawable.ic_nowifi
                )

                else -> ErrorData(
                    message = activity.getString(R.string.error),
                    iconRes = R.drawable.ic_nowifi
                )
            }

        }
    }

    data class ErrorData(
        val message: String,
        val iconRes: Int,
        val colorId: Int = R.color.white
    )
}
