package com.genesis.randomphoto.ui


import android.content.Context
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.genesis.randomphoto.R
import com.genesis.randomphoto.dto.PhotoDTO
import com.genesis.randomphoto.framework.slide.ItemConfig
import com.genesis.randomphoto.framework.slide.ItemTouchHelperCallback
import com.genesis.randomphoto.framework.slide.OnSlideListener
import com.genesis.randomphoto.framework.slide.SlideLayoutManager
import com.genesis.randomphoto.network.SendRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SliderFragment : Fragment(), Callback<ArrayList<PhotoDTO>> {
    override fun onFailure(call: Call<ArrayList<PhotoDTO>>, t: Throwable) {
        Toast.makeText(context, "Resimleri Görebilmek İçin İnternet Bağlantısı Gerekir.", Toast.LENGTH_LONG).show()
        mPhotoList.add(PhotoDTO(9999))
        initView()
        initListener()
    }

    override fun onResponse(call: Call<ArrayList<PhotoDTO>>, response: Response<ArrayList<PhotoDTO>>) {
        mPhotoList = response.body()!!
        mPhotoList.shuffle()
        initView()
        initListener()
    }


    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mSlideLayoutManager: SlideLayoutManager
    private lateinit var mItemTouchHelper: ItemTouchHelper
    private lateinit var mItemTouchHelperCallback: ItemTouchHelperCallback<Int>
    //private lateinit var mViewModel: SliderFragmentViewModel
    private lateinit var rootView: View
    private var mAdapter: MyAdapter? = null
    private var mPhotoList: ArrayList<PhotoDTO> = ArrayList()

    private var FAB_Status = false
    private lateinit var show_fab_1: Animation
    private lateinit var hide_fab_1: Animation
    private lateinit var show_fab_2: Animation
    private lateinit var hide_fab_2: Animation
    private lateinit var show_fab_3: Animation
    private lateinit var hide_fab_3: Animation
    private lateinit var rotate_main: Animation
    private lateinit var revert_main: Animation
    private lateinit var fabMain: FloatingActionButton
    private lateinit var fabEdit: FloatingActionButton
    private lateinit var fabSave: FloatingActionButton
    private lateinit var fabShare: FloatingActionButton
    private lateinit var rootLayout: CoordinatorLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_slider, container, false)

        //Floating Action Buttons
        rootLayout = rootView.findViewById(R.id.coordinatorLayout)
        fabMain = rootView.findViewById(R.id.fab)
        fabEdit = rootView.findViewById(R.id.fab_edit)
        fabSave = rootView.findViewById(R.id.fab_save)
        fabShare = rootView.findViewById(R.id.fab_share)

        //Animations
        show_fab_1 = AnimationUtils.loadAnimation(context, R.anim.fab1_show)
        show_fab_2 = AnimationUtils.loadAnimation(context, R.anim.fab2_show)
        show_fab_3 = AnimationUtils.loadAnimation(context, R.anim.fab3_show)
        hide_fab_1 = AnimationUtils.loadAnimation(context, R.anim.fab1_hide)
        hide_fab_2 = AnimationUtils.loadAnimation(context, R.anim.fab2_hide)
        hide_fab_3 = AnimationUtils.loadAnimation(context, R.anim.fab3_hide)
        rotate_main = AnimationUtils.loadAnimation(context, R.anim.fab_main_rotate)
        revert_main = AnimationUtils.loadAnimation(context, R.anim.fab_main_revert)

        /* rotate_main  = RotateAnimation(0.0f,45.0f,10.0f,10.0f)
         rotate_main.duration=800
         rotate_main.repeatCount=0
         rotate_main.repeatMode=Animation.REVERSE*/
        //  rotate_main.setInterpolator(context,android.R.interpolator.accelerate_decelerate)
        rotate_main.fillAfter = true

        fabMain.setOnClickListener {
            FAB_Status = if (!FAB_Status) {
                expandFAB()
                true
            } else {
                hideFAB()
                false
            }
        }

        fabShare.setOnClickListener {
            Toast.makeText(context, "Share!!!", Toast.LENGTH_SHORT).show()
        }
        fabEdit.setOnClickListener {
            Toast.makeText(context, "Edit!!!", Toast.LENGTH_SHORT).show()
        }
        fabSave.setOnClickListener {
            Toast.makeText(context, "Save!!!", Toast.LENGTH_SHORT).show()
        }
        //initView(rootView)
        //initListener()
        addData()
        return rootView
    }

    private fun expandFAB() {

        fabMain.startAnimation(rotate_main)
        //Floating Action Button 1
        val layoutParams: FrameLayout.LayoutParams = fabEdit.layoutParams as FrameLayout.LayoutParams
        layoutParams.rightMargin += (fabEdit.width * 1.7).toInt()
        layoutParams.bottomMargin += (fabEdit.height * 0.25).toInt()
        fabEdit.layoutParams = layoutParams
        fabEdit.startAnimation(show_fab_1)
        fabEdit.isClickable = true

        //Floating Action Button 2
        val layoutParams2: FrameLayout.LayoutParams = fabSave.layoutParams as FrameLayout.LayoutParams
        layoutParams2.rightMargin += (fabSave.width * 1.5).toInt()
        layoutParams2.bottomMargin += (fabSave.height * 1.5).toInt()
        fabSave.layoutParams = layoutParams2
        fabSave.startAnimation(show_fab_2)
        fabSave.isClickable = true

        //Floating Action Button 3
        val layoutParams3: FrameLayout.LayoutParams = fabShare.layoutParams as FrameLayout.LayoutParams
        layoutParams3.rightMargin += (fabShare.width * 0.25).toInt()
        layoutParams3.bottomMargin += (fabShare.height * 1.7).toInt()
        fabShare.layoutParams = layoutParams3
        fabShare.startAnimation(show_fab_3)
        fabShare.isClickable = true
    }

    private fun hideFAB() {

        fabMain.startAnimation(revert_main)

        //Floating Action Button 1
        val layoutParams: FrameLayout.LayoutParams = fabEdit.layoutParams as FrameLayout.LayoutParams
        layoutParams.rightMargin -= (fabEdit.width * 1.7).toInt()
        layoutParams.bottomMargin -= (fabEdit.height * 0.25).toInt()
        fabEdit.layoutParams = layoutParams
        fabEdit.startAnimation(hide_fab_1)
        fabEdit.isClickable = false

        //Floating Action Button 2
        val layoutParams2: FrameLayout.LayoutParams = fabSave.layoutParams as FrameLayout.LayoutParams
        layoutParams2.rightMargin -= (fabSave.width * 1.5).toInt()
        layoutParams2.bottomMargin -= (fabSave.height * 1.5).toInt()
        fabSave.layoutParams = layoutParams2
        fabSave.startAnimation(hide_fab_2)
        fabSave.isClickable = false

        //Floating Action Button 3
        val layoutParams3: FrameLayout.LayoutParams = fabShare.layoutParams as FrameLayout.LayoutParams
        layoutParams3.rightMargin -= (fabShare.width * 0.25).toInt()
        layoutParams3.bottomMargin -= (fabShare.height * 1.7).toInt()
        fabShare.layoutParams = layoutParams3
        fabShare.startAnimation(hide_fab_3)
        fabShare.isClickable = false
    }

    private fun initView() {
        Log.e("SliderFragment", "initView")
        mRecyclerView = rootView.findViewById(R.id.recycler_view)
        mAdapter = MyAdapter(rootView.context, mPhotoList)
        mRecyclerView.adapter = mAdapter
        //addData()
        mItemTouchHelperCallback = ItemTouchHelperCallback<Int>(mRecyclerView.adapter!!, mPhotoList)
        mItemTouchHelper = ItemTouchHelper(mItemTouchHelperCallback)
        mSlideLayoutManager = SlideLayoutManager(mRecyclerView, mItemTouchHelper)
        mItemTouchHelper.attachToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = mSlideLayoutManager

    }

    private fun initListener() {
        mItemTouchHelperCallback.setOnSlideListener(object : OnSlideListener<PhotoDTO> {
            /* override fun onSlided(viewHolder: RecyclerView.ViewHolder, t: Int, direction: Int) {
                 Log.e("SliderFragment", "onSlided")
             }*/

            override fun onSlided(viewHolder: RecyclerView.ViewHolder, t: PhotoDTO, direction: Int) {
                //val position = viewHolder.adapterPosition
                if (FAB_Status) {
                    hideFAB()
                    FAB_Status = false
                }
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
        SendRequest.getPhotos().enqueue(this@SliderFragment)
        /*   val bgs = intArrayOf(
               R.drawable.img_slide_1,
               R.drawable.img_slide_2,
               R.drawable.img_slide_3,
               R.drawable.img_slide_4,
               R.drawable.img_slide_5,
               R.drawable.img_slide_6
           )
           for (i in 0..5) {
               mList.add(bgs[i])
           }*/
    }

    class MyAdapter(private var context: Context, private var mList: List<PhotoDTO>/*private var mList: List<Int>*/) :
        RecyclerView.Adapter<MyAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, int: Int): MyAdapter.ViewHolder {
            val view: View = LayoutInflater.from(context).inflate(R.layout.item_slide, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = mList.size


        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (mList[position].id != 9999) {
                val URL = "https://picsum.photos/400/600?image=${mList[position].id}"
                // val options=RequestOptions.placeholderOf(R.drawable.background).error(R.drawable.background)
                //Glide.with(holder.itemView).load(URL).apply(options).into(holder.imageBackground)
                Glide.with(holder.itemView).load(URL).into(holder.imageBackground)
            } else {
                // Glide.with(holder.itemView).asGif().load("https://gifs.benlk.com/rubiks-loading.gif").into(holder.imageBackground)
                Glide.with(holder.itemView).load(R.drawable.loading).into(holder.imageBackground)
            }

            //    holder.imageBackground.setImageResource(mList[position])
            /*val options=RequestOptions.placeholderOf(R.drawable.background).error(R.drawable.background)

            Glide.with(holder.itemView).load(mList[position]).apply(options).into(holder.imageBackground)*/

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
