package com.generalmobi.ss4bikers.service

import com.generalmobi.ss4bikers.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit


object ApiClient {

    fun getClient(baseUrl: String, header: HashMap<String, String>): ApiInterface {

        val httpClient = OkHttpClient.Builder()

        httpClient.connectTimeout(30000, TimeUnit.MILLISECONDS)
        httpClient.readTimeout(30000, TimeUnit.MILLISECONDS)
        if (BuildConfig.DEBUG){
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(interceptor)
        }

        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
            for ((key, value) in header) {
                request.addHeader(key, value)
            }
            request.method(original.method(), original.body())
            chain.proceed(request.build())
        }


        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(httpClient.build())
            .build()

        return retrofit.create(ApiInterface::class.java)
    }
}
