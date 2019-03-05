package com.genesis.randomphoto.dto

import android.util.Log

class FabSingletonItem {
    companion object {
        var selected = 0

        init {
            Log.e("Singleton", "$selected")
        }
    }
}