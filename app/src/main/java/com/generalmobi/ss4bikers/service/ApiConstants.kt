package com.generalmobi.ss4bikers.service


/*
 *  Created by gmobi on 14/12/18.
 *  Copyright (c) 2018 General Mobile Technology India Pvt Ltd. All rights reserved.
 */

object ApiConstants {

    /**
     * Api URLs (auth and aeps URLs)
     */
    val BASE_URL = "http://bigip.generalmobi.mobi/"
    val DIRECTION_API_URL = "https://maps.googleapis.com/maps/api/"

    /**
     * Response codes and messages
     */
    val SERVER_ERROR = 500
    val SERVER_ERROR_MESSAGE = "Unable to fetch data from server. Please try after sometime."

    /**
     * Header keys and values
     */
    val HEADER_KEY_1 = "Content-Type"
    val HEADER_VALUE_1 = "application/json"
}
