package com.genesis.randomphoto.network

import com.genesis.randomphoto.dto.PhotoDTO
import retrofit2.Call
import retrofit2.http.GET

interface PhotoService {
    @GET("/list")
    fun getPhotosList(): Call<ArrayList<PhotoDTO>>
}