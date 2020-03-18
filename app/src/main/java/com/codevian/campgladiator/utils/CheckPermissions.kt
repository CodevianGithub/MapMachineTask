package com.codevian.campgladiator.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.codevian.campgladiator.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

object CheckPermissions {

    var permissionAllowed = false

    // check and grant runtime permissions
    fun askPermissions(context: Context, permission: String): Boolean {
        Dexter.withActivity(context as Activity).withPermissions(permission)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        permissionAllowed = true
                    } else {
                        Toast.makeText(context,"Please allow permissions",Toast.LENGTH_SHORT).show()
                        permissionAllowed = false
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                        permissionAllowed = false
                        showSettingsAlert(context)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
        return permissionAllowed
    }

    fun showSettingsAlert(context: Context) {
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Alert")
        builder.setMessage("Permissions Required")
        builder.setPositiveButton(
            "Settings",
            object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.dismiss()
                    showSettingsPage(context)
                }
            })
        val dialog = builder.create()
        dialog.show()
    }

    private fun showSettingsPage(context: Context) {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:" + context.packageName)
        )

        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

}