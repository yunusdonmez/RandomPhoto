package com.genesis.randomphoto.repository

import android.app.Application
import com.genesis.randomphoto.dto.PhotoDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SliderFragmentRepository internal constructor(application: Application) : Callback<List<PhotoDTO>> {
    override fun onFailure(call: Call<List<PhotoDTO>>, t: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResponse(call: Call<List<PhotoDTO>>, response: Response<List<PhotoDTO>>) {
        photoList = response.body()
    }

    private var photoList: List<PhotoDTO>? = null

    fun getPohotoList(): List<PhotoDTO>? {
        //  SendRequest.getPhotos().enqueue(this)
        return photoList
    }
}