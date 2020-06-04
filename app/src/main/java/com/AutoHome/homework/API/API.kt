package com.AutoHome.homework.API

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface API
{
    @GET("weather?")
    fun getCurrentWeather(
        @Query("lat") lat: String,
        @Query("lon") lng: String,
        @Query("APPID") APPID: String)
            : Call<JsonObject>
}