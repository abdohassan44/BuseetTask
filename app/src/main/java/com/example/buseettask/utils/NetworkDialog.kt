package com.example.buseettask.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import com.example.buseettask.R


@SuppressLint("StaticFieldLeak")
object NetworkDialog {
    private var dialog: Dialog? = null

    private fun init(activity: Activity) {
        dialog = Dialog(activity)
        dialog?.setContentView(R.layout.dialog_network)
        dialog?.setCancelable(false)
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )


    } // init

    fun show(activity: Activity) {
        if (!isVisible()) {
            init(activity)

            if (!(activity).isFinishing) {
                //show dialog
                try {
                    if (!dialog?.isShowing!!)
                        dialog?.show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
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