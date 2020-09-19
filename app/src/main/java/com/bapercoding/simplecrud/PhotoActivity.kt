package com.bapercoding.simplecrud

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_photo.*

class PhotoActivity : AppCompatActivity() {

    private var list:ArrayList<String> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        val actionBar = supportActionBar
        actionBar!!.hide()
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)

        list = intent.getStringArrayListExtra("list")
        rvPhotoBesar.setHasFixedSize(true)
        rvPhotoBesar.layoutManager = LinearLayoutManager(applicationContext)
        val adapter = PhotoBesarAdapter(applicationContext, list)
        adapter?.notifyDataSetChanged()
        rvPhotoBesar.adapter = adapter
    }
}
