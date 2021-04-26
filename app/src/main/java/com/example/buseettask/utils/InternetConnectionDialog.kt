package com.example.buseettask.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.ViewGroup
import com.example.buseettask.R

@SuppressLint("StaticFieldLeak")
object InternetConnectionDialog {
    private var dialog: Dialog? = null

    private fun initDialog(activity: Activity) {
        dialog = Dialog(activity)
        dialog?.setContentView(R.layout.dialog_internet_connection)
        dialog?.setCancelable(true)
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        Log.e("network", "dialog")


    } // fun of initDialog

    fun show(activity: Activity) {

        initDialog(activity)
        //show dialog
        try {
            dialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    } // show

    fun isVisible(): Boolean {
        try {
            return dialog!!.isShowing
        } catch (e: Exception) {
            return false
        }
    } // fun of isVisible

    fun dismiss() {
        dialog?.dismiss()
    } // dismiss

}