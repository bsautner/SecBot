package com.secbot.pi.api

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class DefaultApiClient {

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient()).baseUrl("http://192.168.1.48:8080").build()


    private val apiClient = retrofit.create(ApiClient::class.java)





    suspend fun test() {

        println(apiClient.getVision())


    }

    suspend fun uploadPhoto(file: File) {



        try {
            val fbody: RequestBody = RequestBody.create(
                MediaType.parse("image/*"),
                file
            )
            val name = apiClient.postObstructionPhoto(fbody)

            println("uploading photo ${file.exists()} http://localhost:8080/vision/${name.replace(".jpg", "")}")
        } catch (ex : Exception) {
            ex.printStackTrace()
        }

    }




}