package com.kreggscode.morsecode.data.remote

import com.kreggscode.morsecode.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(Constants.API_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(Constants.API_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(Constants.API_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.POLLINATIONS_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val pollinationsApi: PollinationsApiService by lazy {
        retrofit.create(PollinationsApiService::class.java)
    }
}
