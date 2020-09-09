package com.bapercoding.simplecrud

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

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

        val username = emailEt.text.toString()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val user = dataSnapshot.getValue(UserInfo::class.java)
                if (user != null){
                    email = user.email
                }
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        dbReference.child(username).addValueEventListener(postListener)

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu2, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.keluar) {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}
