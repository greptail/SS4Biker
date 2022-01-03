package com.generalmobi.ss4bikers.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.widget.Toast
import android.preference.PreferenceManager


/*
 *  Created by gmobi on 24/9/19.
 *  Copyright (c) 2019 General Mobile Technology India Pvt Ltd. All rights reserved.
 */

/**
 * alert dialog box
 *
 * @param context  current application context
 * @param message  alert message
 */
const val PREF_DEVICE_IMEI = "DEVICE_IMEI"
const val PREF_UPDATE_FREQ = "UPDATE_FREQ"

fun showAlert(
    context: Context, message: String,
    okListener: DialogInterface.OnClickListener?,
    cancelListener: DialogInterface.OnClickListener?,
    showCancel: Boolean = true
) {
    val builder = AlertDialog.Builder(context)
    builder.setMessage(message)
    builder.setCancelable(false)
    if (showCancel) {
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
            cancelListener?.onClick(dialog, which)
        }
    }
    builder.setPositiveButton("OK") { dialog, which ->
        dialog.dismiss()
        okListener?.onClick(dialog, which)
    }
    builder.show()
}

fun showToast(context: Context, msg: String?) {
    if (!TextUtils.isEmpty(msg)) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}

fun storePreference(context: Context, key: String, value: String) {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    val editor = preferences.edit()
    editor.putString(key, value)
    editor.apply()
}

fun fetchPreference(context: Context, key: String): String? {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    return preferences.getString(key, null)
}