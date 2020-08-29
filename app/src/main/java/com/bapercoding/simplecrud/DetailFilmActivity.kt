package com.bapercoding.simplecrud

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.design.widget.TabLayout.OnTabSelectedListener
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_detail_film.*
import kotlinx.android.synthetic.main.activity_main.*

class DetailFilmActivity : AppCompatActivity() {

    private var list: ArrayList<Film> = arrayListOf()
    var letak: Int = 0
    lateinit var judul:String
    lateinit var rating:String
    lateinit var episode:String
    lateinit var sinopsis:String
    private lateinit var layout: RelativeLayout
    private lateinit var layout4: RecyclerView
    private lateinit var tabLayout1: TabLayout
    val iterator = arrayOf('a','b','c','d','e','f','g','h','i','j','k')

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_film)

        layout = findViewById(R.id.relativeLayout)
        layout4 = findViewById(R.id.mRecyclerView)
        layout4.setOnTouchListener(object : OnSwipeTouchListener(this@DetailFilmActivity) {
            override fun onSwipeRight() {
                super.onSwipeRight()
                onBackPressed()
            }
        })

        judul = intent.getStringExtra("judul")
        rating = intent.getStringExtra("rating")
        episode = intent.getStringExtra("episode")
        sinopsis = intent.getStringExtra("sinopsis")
        letak = intent.getIntExtra("position",0)

        val tvDataReceived: TextView = findViewById(R.id.tv_data_received)
        tvDataReceived.text = judul

        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = ""

        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        list.addAll(Data.listData)


//        val bottomBorder: LayerDrawable? = getBorders(Color.WHITE, Color.LTGRAY,0,0,0,15)
//        tvDetail.setBackground(bottomBorder)

        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = DetailFilmAdapter(applicationContext,list,judul, rating, episode, sinopsis, letak)
        adapter.notifyDataSetChanged()
        mRecyclerView.adapter = adapter

        mFloatingActionButton2.visibility = View.GONE

        tabLayout1 = findViewById<View>(R.id.tabLayout) as TabLayout
        tabLayout1.addTab(tabLayout1.newTab().setText("Details"))
        tabLayout1.addTab(tabLayout1.newTab().setText("Cast"))
        tabLayout1.addTab(tabLayout1.newTab().setText("Episodes"))
        tabLayout1.addTab(tabLayout1.newTab().setText("Photos"))

        tabClick()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        val intentMain = Intent(this@DetailFilmActivity, MainActivity::class.java)
        startActivity(intentMain)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

//    protected fun getBorders(
//            bgColor: Int, borderColor: Int,
//            left: Int, top: Int, right: Int, bottom: Int
//    ): LayerDrawable? {
//        // Initialize new color drawables
//        val borderColorDrawable = ColorDrawable(borderColor)
//        val backgroundColorDrawable = ColorDrawable(bgColor)
//
//        // Initialize a new array of drawable objects
//        val drawables = arrayOf<Drawable>(
//                borderColorDrawable,
//                backgroundColorDrawable
//        )
//
//        // Initialize a new layer drawable instance from drawables array
//        val layerDrawable = LayerDrawable(drawables)
//
//        // Set padding for background color layer
//        layerDrawable.setLayerInset(
//                1,  // Index of the drawable to adjust [background color layer]
//                left,  // Number of pixels to add to the left bound [left border]
//                top,  // Number of pixels to add to the top bound [top border]
//                right,  // Number of pixels to add to the right bound [right border]
//                bottom // Number of pixels to add to the bottom bound [bottom border]
//        )
//
//        // Finally, return the one or more sided bordered background drawable
//        return layerDrawable
//    }

    fun tabClick(){
        tabLayout1.setTabTextColors(Color.parseColor("#b3e5fc"), Color.parseColor("#03A9F4"))
        tabLayout1.setSelectedTabIndicatorHeight(7)
        tabLayout1.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tabLayout1.selectedTabPosition == 0) {
                    val adapter = DetailFilmAdapter(applicationContext,list,judul, rating, episode, sinopsis, letak)
                    adapter.notifyDataSetChanged()
                    mRecyclerView.adapter = adapter

                    mFloatingActionButton2.visibility = View.GONE

                } else if (tabLayout1.selectedTabPosition == 1) {
                    val listPhoto = ArrayList<Photo>()
                    listPhoto.add(Photo(getResources().getIdentifier("comingsoon", "drawable", getPackageName())))

                    val adapter = PhotoFilmAdapter(applicationContext,listPhoto)
                    adapter.notifyDataSetChanged()
                    mRecyclerView.adapter = adapter

                    mFloatingActionButton2.visibility = View.GONE

                } else if (tabLayout1.selectedTabPosition == 2) {
                    val listPhoto = ArrayList<Photo>()
                    listPhoto.add(Photo(getResources().getIdentifier("comingsoon", "drawable", getPackageName())))

                    val adapter = PhotoFilmAdapter(applicationContext,listPhoto)
                    adapter.notifyDataSetChanged()
                    mRecyclerView.adapter = adapter

                    mFloatingActionButton2.visibility = View.GONE

                } else if (tabLayout1.selectedTabPosition == 3) {
                    val listPhoto = ArrayList<Photo>()
                    for(position in 0 until (iterator.size-1)){
                        val nama = "${iterator[letak]}${iterator[position]}"
                        listPhoto.add(Photo(getResources().getIdentifier(nama, "drawable", getPackageName())))
                        if(nama == "if"){
                            listPhoto.add(Photo(getResources().getIdentifier("$nama${iterator[position]}", "drawable", getPackageName())))
                        }
                    }

                    val adapter = PhotoFilmAdapter(applicationContext,listPhoto)
                    adapter.notifyDataSetChanged()
                    mRecyclerView.adapter = adapter

                    mFloatingActionButton2.visibility = View.VISIBLE

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}
