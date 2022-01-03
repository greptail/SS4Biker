package com.generalmobi.ss4bikers.service


interface ServiceCallback<T> {
    fun onError(e: Throwable?){
    }

    fun onResponse(response: T)

}