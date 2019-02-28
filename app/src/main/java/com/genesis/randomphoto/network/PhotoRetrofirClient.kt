package com.genesis.randomphoto.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PhotoRetrofirClient {
    companion object {
        fun getClient(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://picsum.photos")
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
    }
}