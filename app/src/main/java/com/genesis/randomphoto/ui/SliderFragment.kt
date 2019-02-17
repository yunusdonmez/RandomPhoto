package com.genesis.randomphoto.ui


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.genesis.randomphoto.R
import com.genesis.randomphoto.framework.slide.ItemConfig
import com.genesis.randomphoto.framework.slide.ItemTouchHelperCallback
import com.genesis.randomphoto.framework.slide.OnSlideListener
import com.genesis.randomphoto.framework.slide.SlideLayoutManager


class SliderFragment : Fragment() {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mSlideLayoutManager: SlideLayoutManager
    private lateinit var mItemTouchHelper: ItemTouchHelper
    private lateinit var mItemTouchHelperCallback: ItemTouchHelperCallback<Int>
    private var mAdapter: MyAdapter? = null
    private val mList = ArrayList<Int>()

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
        Log.e("SliderFragment", "initView")
        mRecyclerView = rootView.findViewById(R.id.recycler_view)
        mAdapter = MyAdapter(rootView.context, mList)
        mRecyclerView.adapter = mAdapter
        addData()
        mItemTouchHelperCallback = ItemTouchHelperCallback<Int>(mRecyclerView.adapter!!, mList)
        mItemTouchHelper = ItemTouchHelper(mItemTouchHelperCallback)
        mSlideLayoutManager = SlideLayoutManager(mRecyclerView, mItemTouchHelper)
        mItemTouchHelper.attachToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = mSlideLayoutManager

    }

    private fun initListener() {
        mItemTouchHelperCallback.setOnSlideListener(object : OnSlideListener<Int> {
            override fun onSlided(viewHolder: RecyclerView.ViewHolder, t: Int, direction: Int) {
                val position = viewHolder.adapterPosition
                Log.e("SliderFragment", "onSlided")
            }

            override fun onSliding(viewHolder: RecyclerView.ViewHolder, ratio: Float, direction: Int) {
                if (direction == ItemConfig.SLIDING_LEFT) {
                } else if (direction == ItemConfig.SLIDING_RIGHT) {
                }

            }

            override fun onClear() {
                addData()
            }
        })
    }

    fun addData() {
        val bgs = intArrayOf(
            R.drawable.img_slide_1,
            R.drawable.img_slide_2,
            R.drawable.img_slide_3,
            R.drawable.img_slide_4,
            R.drawable.img_slide_5,
            R.drawable.img_slide_6
        )
        for (i in 0..5) {
            mList.add(bgs[i])
        }
    }

    class MyAdapter(private var context: Context, private var mList: ArrayList<Int>) :
        RecyclerView.Adapter<MyAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, int: Int): MyAdapter.ViewHolder {
            val view: View = LayoutInflater.from(context).inflate(R.layout.item_slide, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = mList.size


        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.imageBackground.setImageResource(mList[position])
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var imageBackground: ImageView

            init {
                super.itemView
                imageBackground = itemView.findViewById(R.id.img_bg)
            }
        }

    }
}
