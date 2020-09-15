package com.bapercoding.simplecrud

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val actionbar = supportActionBar
        actionbar!!.title = ""
        actionbar.hide()
        //set back button

        Handler().postDelayed({ //setelah loading maka akan langsung berpindah ke home activity
            val home = Intent(this@SplashScreen, LoginActivity::class.java)
            startActivity(home)
            finish()
        }, 3000)
    }
}
