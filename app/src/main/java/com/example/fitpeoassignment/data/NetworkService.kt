package com.example.fitpeoassignment.data


import retrofit2.Call
import retrofit2.http.GET

interface NetworkService {
    @GET("photos")
    fun getImagesData(): Call<String>
}