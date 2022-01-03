package com.generalmobi.ss4bikers.service


import com.generalmobi.ss4bikers.models.Result
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/*
 *  Created by gmobi on 14/12/18.
 *  Copyright (c) 2018 General Mobile Technology India Pvt Ltd. All rights reserved.
 */
interface ApiInterface {

    @GET("TpiBs/iot/api/v1/bikeStatus")
    fun callGetData(@Query("IMEINumber") IMEINumber: String): Observable<String>

    @GET("directions/json")
    fun callDirections(
        @Query("mode") mode: String,
        @Query("transit_routing_preference") transit_routing_preference: String,
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String
    ): Observable<Result>
}
