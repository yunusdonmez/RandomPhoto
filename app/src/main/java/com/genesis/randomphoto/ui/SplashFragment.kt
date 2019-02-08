package com.genesis.randomphoto.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.genesis.randomphoto.R
import com.genesis.randomphoto.framework.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*


class SplashFragment : BaseFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.appBarLayout?.visibility=View.GONE
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        object : CountDownTimer(2000,1000){
            override fun onFinish() {
                Log.e("Timer","Süre Bitti")
                view?.let { Navigation.findNavController(it).navigate(R.id.action_splash_to_home) }
            }

            override fun onTick(millisUntilFinished: Long) {
                Log.e("Timer","Süre Başladı")

            }

        }.start()
    }
}
