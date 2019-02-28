package com.genesis.randomphoto.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.genesis.randomphoto.dto.PhotoDTO
import com.genesis.randomphoto.repository.SliderFragmentRepository

class SliderFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepostitory: SliderFragmentRepository = SliderFragmentRepository(application)
    private var photoList: List<PhotoDTO>? = null
    fun getPhotoList() {
        photoList = mRepostitory.getPohotoList()
    }
}