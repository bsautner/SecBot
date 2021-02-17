package com.secbot.api

import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface ApiClient {

    @GET("/vision")
    suspend fun getVision() : String


    @Multipart
    @POST("/vision")
    suspend fun postObstructionPhoto(
        @Part("file\"; filename=\"pp.jpg\" ") file: RequestBody,

    ) : String
}