package com.example.buseettask.utils

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.buseettask.R

class CustomToast {
    companion object {
        fun showErrorMessage(activity: Activity, message: String) {
            val toastView = activity.layoutInflater.inflate(R.layout.custom_toast, null)
            val text = toastView.findViewById<View>(R.id.customToastText) as TextView
            text.text = message

            // Initiate the Toast instance.
            val toast = Toast(activity)
            // Set custom view in toast.
            toast.view = toastView
            toast.duration = Toast.LENGTH_SHORT
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }

    }
}