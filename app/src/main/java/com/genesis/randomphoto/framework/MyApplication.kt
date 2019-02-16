package com.genesis.randomphoto.framework

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        sContext = this
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var sContext: Context
    }
}