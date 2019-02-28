package com.genesis.randomphoto.network

import com.genesis.randomphoto.dto.PhotoDTO
import retrofit2.Call

object SendRequest {
    fun getPhotos(): Call<ArrayList<PhotoDTO>> =
        PhotoRetrofirClient.getClient().create(PhotoService::class.java).getPhotosList()
}