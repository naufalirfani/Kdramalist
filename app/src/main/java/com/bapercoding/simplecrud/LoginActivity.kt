package com.bapercoding.simplecrud

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase

    private var userId: String = ""

    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var loginBtn: Button
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val actionbar = supportActionBar
        actionbar!!.title = ""

        val tvsignUp: TextView = findViewById(R.id.tv_signUp)
        val tvforgot: TextView = findViewById(R.id.tv_forgot)
        val tvwelcome: TextView = findViewById(R.id.tv_welcome)
        val tvdoyou: TextView = findViewById(R.id.tv_doyou)
        tvsignUp.text = resources.getString(R.string.sign_up)
        tvforgot.text = resources.getString(R.string.forgot_password)
        tvwelcome.text = resources.getString(R.string.welcome_to_kdramalist)
        tvdoyou.text = resources.getString(R.string.don_t_have_an_account)


        emailEt = findViewById(R.id.et_email_login)
        passwordEt = findViewById(R.id.et_pass_login)
        loginBtn = findViewById(R.id.btn_login)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        dbReference = firebaseDatabase.getReference("users")
        userId = dbReference.push().key.toString()

        tv_signUp.setOnClickListener {
            val home = Intent(this@LoginActivity, SingUpActivity::class.java)
            startActivity(home)
            finish()
        }

        if(emailEt.text.toString().contains("@")){
            email = emailEt.text.toString()
        }
        else{
            addUserChangeListener(emailEt.text.toString())
        }

        btn_login.setOnClickListener {
            password = passwordEt.text.toString()

            if(TextUtils.isEmpty(password)) {
                Toast.makeText(this@LoginActivity, "Please fill all the fields", Toast.LENGTH_LONG).show()
            }
            else if(TextUtils.isEmpty(email)){
                Toast.makeText(this, "Invalid login, please try again", Toast.LENGTH_LONG).show()
            }
            else{
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener { task ->
                    if(task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else {
                        Toast.makeText(this, "Invalid login, please try again", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }

    private fun addUserChangeListener(username: String) {
        // User data change listener
        dbReference.child(username).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(UserInfo::class.java)

                // Check for null
                if (user == null) {
                    return
                }
                else{
                    email = user.email.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }
}
