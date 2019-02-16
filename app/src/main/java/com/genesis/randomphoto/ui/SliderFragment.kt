package com.genesis.randomphoto.ui


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.genesis.randomphoto.R
import com.genesis.randomphoto.framework.MyApplication


class SliderFragment : Fragment() {

    private val TAG = "SliderFragment"
    private lateinit var mRecyclerView: RecyclerView
    val mList: ArrayList<Int> = ArrayList()
    private lateinit var myAdapter: MyAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_slider, container, false)
        initView(rootView)
        initListener()
        return rootView
    }

    private fun initView(rootView: View) {
        mRecyclerView = rootView.findViewById(R.id.recycler_view)
        myAdapter = MyAdapter(rootView.context, mList)
        mRecyclerView.adapter = myAdapter
        addData()
        mItemTouchHelperCallback
    }

    class MyAdapter(private var context: Context, private var mList: ArrayList<Int>) :
        RecyclerView.Adapter<MyAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, int: Int): MyAdapter.ViewHolder {
            val view: View = LayoutInflater.from(MyApplication.sContext).inflate(R.layout.item_slide, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onBindViewHolder(p0: MyAdapter.ViewHolder, p1: Int) {
            mLis
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            lateinit var imageBackground: ImageView
            fun ViewHolder(itemView: View) {
                super.itemView
                imageBackground = itemView.findViewById(R.id.img_bg)
            }
        }

    }
}
