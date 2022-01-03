package com.generalmobi.ss4bikers.service

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import androidx.fragment.app.Fragment
import com.generalmobi.ss4bikers.utils.LogUtils
import com.generalmobi.ss4bikers.utils.showAlert
import org.greenrobot.eventbus.EventBus
import rx.Observable
import rx.Observer
import rx.Subscription

/*
 *  Created by gmobi on 14/12/18.
 *  Copyright (c) 2018 General Mobile Technology India Pvt Ltd. All rights reserved.
 */

@Suppress("DEPRECATION")
class ApiObserver<T>(private val context: Context, private val listener: ServiceCallback<T>?) : Observer<T> {

    private val LOG_TAG = "ApiObserver"
    private lateinit var subscription: Subscription
    private var pDialog: ProgressDialog? = null
    var isShowProgress: Boolean = true
    var progressMessage: String = "Refreshing status..."

    fun execute(observable: Observable<T>, myObserver: ApiObserver<T>) {
        try {
            if (Connectivity.isConnected(context)) {
                if (isShowProgress) {
                    showProgressDialog()
                }
                subscription = observable.subscribe(myObserver)
            } else {
                EventBus.getDefault().post("Check your internet connection and try again")
            }
        } catch (e: Exception) {
            LogUtils.wtf(LOG_TAG, "exception during execute", e)
            responseAlertDialog(
                ApiConstants.SERVER_ERROR_MESSAGE
            )
        }

    }

    override fun onCompleted() {
        LogUtils.i(LOG_TAG, "Request Processed Successfully")
    }

    override fun onError(e: Throwable?) {
        try {
            if (isShowProgress) {
                dismissProgressDialog()
            }

            subscription.unsubscribe()

            responseAlertDialog(
                ApiConstants.SERVER_ERROR_MESSAGE
            )

        } catch (e: Throwable) {
            e.printStackTrace()
            responseAlertDialog(
                ApiConstants.SERVER_ERROR_MESSAGE
            )
        }

    }

    override fun onNext(response: T) {
        if (isShowProgress) {
            dismissProgressDialog()
        }
        subscription.unsubscribe()

        if (listener == null) {
            return
        }

        if (listener is Fragment && !(listener as Fragment).isAdded) {
            return
        }

        if (listener is Activity && (listener as Activity).isDestroyed) {
            return
        }

        listener.onResponse(response)

    }


    private fun showProgressDialog() {
        if (pDialog == null) {
            pDialog = ProgressDialog.show(context, "", progressMessage)
        }
        try {
            pDialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun dismissProgressDialog() {
        try {
            pDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun responseAlertDialog(msg: String) {
        EventBus.getDefault().post(msg)
    }

}