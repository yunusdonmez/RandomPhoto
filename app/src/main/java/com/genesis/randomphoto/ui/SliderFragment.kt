package com.genesis.randomphoto.ui


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.genesis.randomphoto.R
import com.genesis.randomphoto.dto.FabSingletonItem
import com.genesis.randomphoto.dto.PhotoDTO
import com.genesis.randomphoto.framework.AppConfig
import com.genesis.randomphoto.framework.slide.ItemConfig
import com.genesis.randomphoto.framework.slide.ItemTouchHelperCallback
import com.genesis.randomphoto.framework.slide.OnSlideListener
import com.genesis.randomphoto.framework.slide.SlideLayoutManager
import com.genesis.randomphoto.viewmodel.SliderFragmentViewModel
import kotlinx.android.synthetic.main.fragment_slider.*
import java.io.File
import java.io.FileOutputStream


class SliderFragment : Fragment() {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mSlideLayoutManager: SlideLayoutManager
    private lateinit var mItemTouchHelper: ItemTouchHelper
    private lateinit var mItemTouchHelperCallback: ItemTouchHelperCallback<Int>
    private lateinit var sliderFragmentViewModel: SliderFragmentViewModel
    private lateinit var rootView: View
    private var mAdapter: MyAdapter? = null

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
            Toast.makeText(context, "Edit!!! ${FabSingletonItem.selected}", Toast.LENGTH_SHORT).show()

        }
        fabSave.setOnClickListener {
            Toast.makeText(context, "Save!!!", Toast.LENGTH_SHORT).show()
            // val URL = "https://picsum.photos/400/600?image=${FabSingletonItem.selected}"
            Glide.with(this)
                .asBitmap()
                .load(AppConfig.URL + FabSingletonItem.selected.toString())
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        saveImage(resource)
                    }

                })
        }
        addData()
        return rootView
    }

    private fun saveImage(image: Bitmap): String {
        var saveImagePath = ""
        val imageFileName = "JPEG_" + "${FabSingletonItem.selected}" + ".jpg"
        val storageDir: File =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString())
        var success = true
        if (!storageDir.exists())
            success = storageDir.mkdirs()
        if (success) {
            val imageFile = File(storageDir, imageFileName)
            saveImagePath = imageFile.absolutePath
            try {
                val fOut = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            // Add the image to the system gallery
            galleryAddPic(saveImagePath)
            Toast.makeText(context, "IMAGE SAVED", Toast.LENGTH_LONG).show()
        }
        return saveImagePath
    }

    private fun galleryAddPic(imagePath: String) {
        val mediaScanIntent: Intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(imagePath)
        val contentUri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        context?.sendBroadcast(mediaScanIntent)
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
        //heartGroup.visibility=View.INVISIBLE
        heartGroup.startAnimation(AnimationUtils.loadAnimation(context, R.anim.heart_hide))

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

        heartGroup.startAnimation(AnimationUtils.loadAnimation(context, R.anim.heart_show))
    }

    private fun initListener() {
        mItemTouchHelperCallback.setOnSlideListener(object : OnSlideListener<PhotoDTO> {

            override fun onSlided(viewHolder: RecyclerView.ViewHolder, t: PhotoDTO, direction: Int) {
                //val position = viewHolder.adapterPosition
                if (FAB_Status) {
                    hideFAB()
                    FAB_Status = false
                }
                val scaleAnimation = ScaleAnimation(
                    1.0f,
                    1.5f,
                    1.0f,
                    1.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.7f,
                    Animation.RELATIVE_TO_SELF,
                    0.7f
                )
                scaleAnimation.duration = 500
                val bounceInterpolator = BounceInterpolator()
                scaleAnimation.interpolator = bounceInterpolator
                if (direction == ItemConfig.SLIDED_LEFT) {
                    imgHeart.startAnimation(scaleAnimation)
                } else if (direction == ItemConfig.SLIDED_RIGHT) {
                    imgBrokenHeart.startAnimation(scaleAnimation)
                }
                Log.e("SliderFragment", "onSlided:$direction")
            }

            override fun onSliding(viewHolder: RecyclerView.ViewHolder, ratio: Float, direction: Int) {

            }

            override fun onClear() {
                addData()
            }
        })
    }

    fun addData() {
        sliderFragmentViewModel = ViewModelProviders.of(this@SliderFragment).get(SliderFragmentViewModel::class.java)
        sliderFragmentViewModel.photoList.observe(this, Observer {
            Log.e("SliderFragment", "initView")
            it?.shuffle()
            mRecyclerView = rootView.findViewById(R.id.recycler_view)
            mAdapter = MyAdapter(rootView.context, it!!)
            mRecyclerView.adapter = mAdapter
            mItemTouchHelperCallback = ItemTouchHelperCallback<Int>(mRecyclerView.adapter!!, it)
            mItemTouchHelper = ItemTouchHelper(mItemTouchHelperCallback)
            mSlideLayoutManager = SlideLayoutManager(mRecyclerView, mItemTouchHelper)
            mItemTouchHelper.attachToRecyclerView(mRecyclerView)
            mRecyclerView.layoutManager = mSlideLayoutManager
            initListener()
        })
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
                Glide.with(holder.itemView).load(AppConfig.URL + mList[position].id.toString())
                    .into(holder.imageBackground)
                FabSingletonItem.selected = mList[position].id
            } else {
                Glide.with(holder.itemView).load(R.drawable.loading).into(holder.imageBackground)
            }
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
