package com.genesis.randomphoto.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.widget.Toast
import com.genesis.randomphoto.dto.PhotoDTO
import com.genesis.randomphoto.network.SendRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SliderFragmentViewModel(application: Application) : AndroidViewModel(application) {
    internal var photoList: MutableLiveData<ArrayList<PhotoDTO>> = MutableLiveData()

    init {
        SendRequest.getPhotos().enqueue(object : Callback<ArrayList<PhotoDTO>> {
            override fun onFailure(call: Call<ArrayList<PhotoDTO>>, t: Throwable) {
                Toast.makeText(
                    application.baseContext,
                    "Resimleri Görebilmek İçin İnternet Bağlantısı Gerekir.",
                    Toast.LENGTH_LONG
                ).show()
                val tmpList = ArrayList<PhotoDTO>()
                tmpList.add(PhotoDTO(9999))
                photoList.value = tmpList
            }

            override fun onResponse(call: Call<ArrayList<PhotoDTO>>, response: Response<ArrayList<PhotoDTO>>) {
                photoList.value = response.body()
            }

        })
    }

}