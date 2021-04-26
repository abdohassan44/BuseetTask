package com.example.buseettask.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.example.buseettask.R
import com.example.buseettask.databinding.CustomdialogBinding

object CustomDialog {

    const val errorID = R.drawable.ic_error


    fun Context.showDialog(
        message: String,
        iconID: Int?,
        colorID: Int? = null,
        onDismiss: () -> Unit = {}
    ) {
        val binding: CustomdialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.customdialog,
            null,
            false
        )

        binding.titleTv.text = message

        binding.iconIV.setBackgroundResource(iconID!!)

        binding.headerTitle.setBackgroundResource(colorID ?: R.color.purple_700)


        binding.executePendingBindings()

        val dialogServerResponse = Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val width =
                (this@showDialog.resources.displayMetrics.widthPixels * 0.9).toInt()
            window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
            show()
        }
        binding.closeIV.setOnClickListener {
            dialogServerResponse.dismiss()
        }
        dialogServerResponse.setOnDismissListener { onDismiss() }
        dialogServerResponse.setOnCancelListener { onDismiss() }


    }
}