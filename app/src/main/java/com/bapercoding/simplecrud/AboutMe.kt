package com.bapercoding.simplecrud

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.student_list.view.*

class AboutMe : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    var username: String? = null

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

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        firebaseDatabase = FirebaseDatabase.getInstance()
        dbReference = firebaseDatabase.getReference("users2")
        if (user != null) {
            val id = user.uid
            val email2 = user.email
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    val user2 = dataSnapshot.getValue(UserInfo::class.java)
                    if (user2 != null){
                        myName.text = user2.username
                        email.text = user2.email
                    }
                    // ...
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            }
            dbReference.child(id).addValueEventListener(postListener)

        }
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
