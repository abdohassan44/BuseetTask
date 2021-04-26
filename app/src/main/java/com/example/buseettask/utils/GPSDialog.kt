package com.example.buseettask.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.Settings
import android.view.ViewGroup
import android.widget.Button
import com.example.buseettask.R


@SuppressLint("StaticFieldLeak")
object GPSDialog {
    private var dialog: Dialog? = null

    private var enableButton: Button? = null

    private fun init(activity: Activity) {
        dialog = Dialog(activity)
        dialog?.setContentView(R.layout.dialog_location_disabled)
        dialog?.setCancelable(false)
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        enableButton = dialog?.findViewById(R.id.bt_enable)
        enableButton?.setOnClickListener {
            dialog!!.dismiss()
            val viewIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            activity.startActivity(viewIntent)

        }
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
        try {
            if (!dialog?.isShowing!!)
                dialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    } // dismiss
}