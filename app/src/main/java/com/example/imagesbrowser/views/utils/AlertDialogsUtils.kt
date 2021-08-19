package com.example.imagesbrowser.views.utils

import android.app.AlertDialog
import android.content.Context
import com.example.imagesbrowser.R

class AlertDialogsUtils(
    context: Context
) {
    private  val noInternetAlertDialog = AlertDialog.Builder(context)
        .setMessage(
            context.getString(R.string.no_internet_connection_message)
        )
        .setPositiveButton("OK") { _, _ ->}
        .create()

    private val downloadingDataErrorAlertDialog = AlertDialog.Builder(context)
        .setMessage(
            context.getString(R.string.downloading_data_error_message)
        )
        .setPositiveButton("OK") { _, _ -> }
        .create()

    fun showNoInternetAlertDialog() {
        noInternetAlertDialog.show()
    }

    fun dismissNoInternetAlertDialog() {
        noInternetAlertDialog.dismiss()
    }

    fun showDownloadingDataErrorAlertDialog() {
        downloadingDataErrorAlertDialog.show()
    }
}