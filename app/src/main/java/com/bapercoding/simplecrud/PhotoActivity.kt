package com.bapercoding.simplecrud

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_photo.*
import java.lang.reflect.Type


class PhotoActivity : AppCompatActivity() {

    private var list:ArrayList<String> = arrayListOf()
    private var position: Int = 0
    private var judul: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        val actionBar = supportActionBar
        actionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#000000")))
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)

        list = intent.getStringArrayListExtra("list")
        position = intent.getIntExtra("position", 0)
        judul = intent.getStringExtra("judul")

        actionBar.title = judul

        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val gson = Gson()
        if(list.isNotEmpty()){
            val editor: SharedPreferences.Editor = sharedPrefs.edit()
            val json = gson.toJson(list)

            editor.putString("list", json)
            editor.commit()
        }
        val json = sharedPrefs.getString("list", "")
        val type: Type = object : TypeToken<ArrayList<String?>?>() {}.getType()
        val arrayList: ArrayList<String> = gson.fromJson(json, type)

        rvPhotoBesar.setHasFixedSize(true)
        rvPhotoBesar.layoutManager = LinearLayoutManager(applicationContext)
        val adapter = PhotoBesarAdapter(applicationContext, arrayList)
        adapter?.notifyDataSetChanged()
        rvPhotoBesar.adapter = adapter
        rvPhotoBesar.scrollToPosition(position)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
