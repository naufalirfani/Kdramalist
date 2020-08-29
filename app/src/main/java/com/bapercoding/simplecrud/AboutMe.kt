package com.bapercoding.simplecrud

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class AboutMe : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me)

        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "About Me"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        val iv: ImageView = findViewById(R.id.img_my_photo)
        iv.setImageResource(getResources().getIdentifier("fotoku", "drawable", getPackageName()))

        val myName: TextView = findViewById(R.id.tv_name)
        val email: TextView = findViewById(R.id.tv_email)

        myName.text = getString(R.string.data_pribadi)
        email.text = getString(R.string.email)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intentMain = Intent(this@AboutMe, MainActivity::class.java)
        startActivity(intentMain)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
