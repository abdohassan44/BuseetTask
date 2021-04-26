package com.example.buseettask.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.PermissionChecker
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

object PermissionsUtil {
    private val PERMISSIONS = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    fun isPermissionGranted(context: Context): Boolean {
        for (perms in PERMISSIONS) {
            val res = PermissionChecker.checkCallingOrSelfPermission(context, perms)
            if (res != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }

        return true

    }

    fun Context.requestPermission(
        onPermissionGranted: () -> Unit,
        onPermissionDenied: () -> Unit,
        permissionList: List<String>? = PERMISSIONS
    ) {
        Dexter.withContext(this)
            .withPermissions(
                permissionList
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted())
                            onPermissionGranted()
                        else
                            onPermissionDenied()

                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            })
            .withErrorListener {

            }
            .check()
    }

}