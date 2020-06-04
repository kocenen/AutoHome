package com.AutoHome.homework.Client

import com.AutoHome.homework.API.API
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Client {

    companion object {
        private val retrofitClient: Client = Client()

        fun getInstance(): Client {
            return retrofitClient
        }
    }

    fun buildRetrofit(): API {
        val retrofit: Retrofit? = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: API = retrofit!!.create(API :: class.java)
        return service
    }

}