package com.generalmobi.ss4bikers.service

import android.content.Context
import com.generalmobi.ss4bikers.models.Result
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/*
 *  Created by gmobi on 14/12/18.
 *  Copyright (c) 2018 General Mobile Technology India Pvt Ltd. All rights reserved.
 */
object ApiHelper {

    fun callGetData(
        context: Context,
        IMEINumber: String,
        apiObserver: ApiObserver<String>
    ) {
        val header = HashMap<String, String>()
        header[ApiConstants.HEADER_KEY_1] = ApiConstants.HEADER_VALUE_1
        val apiService = ApiClient.getClient(ApiConstants.BASE_URL, header)
        val responseObservable = apiService.callGetData(IMEINumber)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
        apiObserver.execute(responseObservable, apiObserver)
    }

    fun callDirections(
        context: Context,
        origin: String,
        destination: String,
        apiObserver: ApiObserver<Result>
    ) {
        val header = HashMap<String, String>()
        header[ApiConstants.HEADER_KEY_1] = ApiConstants.HEADER_VALUE_1
        val apiService = ApiClient.getClient(ApiConstants.DIRECTION_API_URL, header)
        val responseObservable = apiService.callDirections(
            "driving",
            "less_driving",
            origin,
            destination,
            "AIzaSyBWEpF9w4RVhai4UQsOgk7t2dsK0bn5MiE"
        )
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
        apiObserver.execute(responseObservable, apiObserver)
    }
}
