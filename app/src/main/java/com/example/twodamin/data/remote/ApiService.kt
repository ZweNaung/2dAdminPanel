package com.example.twodamin.data.remote

import android.util.Log
import com.example.twodamin.data.remote.api.DailyResultApiService
import com.example.twodamin.data.remote.api.DataEntryApiService
import com.example.twodamin.data.remote.api.OmenApiService
import com.example.twodamin.util.Constants
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object ApiService {

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply{
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build().also {
                Log.d("RetrofitClient", "OkHttpClient initialized.")
            }
    }

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
    }
    val contentType ="application/json".toMediaType()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    val dailyResultApiService : DailyResultApiService = retrofit.create(DailyResultApiService::class.java)

    val dataEntryApiService: DataEntryApiService =retrofit.create(DataEntryApiService::class.java)

    val omenApiService: OmenApiService = retrofit.create(OmenApiService::class.java)

}