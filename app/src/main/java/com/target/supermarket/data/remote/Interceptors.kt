package com.target.supermarket.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

fun client():OkHttpClient{
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder().addInterceptor(interceptor).build()
}