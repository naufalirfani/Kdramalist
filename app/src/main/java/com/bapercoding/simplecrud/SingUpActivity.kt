package com.bapercoding.simplecrud

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SingUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase

    private var userId: String = ""

    private lateinit var usernameEt: EditText
    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText

    private lateinit var signUpBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)

        val actionbar = supportActionBar
        actionbar!!.title = ""

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        dbReference = firebaseDatabase.getReference("users")

        usernameEt = findViewById(R.id.et_username_sign)
        emailEt = findViewById(R.id.et_email_sign)
        passwordEt = findViewById(R.id.et_pass_sign)
        signUpBtn = findViewById(R.id.btn_signUp)
        val tvcreate: TextView = findViewById(R.id.tv_create)
        tvcreate.setText(getString(R.string.create_an_account))

        signUpBtn.setOnClickListener{
            var username: String = usernameEt.text.toString()
            var email: String = emailEt.text.toString()
            var password: String = passwordEt.text.toString()

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(username)) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show()
            } else{
                var user: UserInfo? = null
                dbReference.child(username).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        user = dataSnapshot.getValue(UserInfo::class.java)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                    }
                })
                if(TextUtils.isEmpty(user?.username)){
                    createUser(username, email)
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener{ task ->
                        if(task.isSuccessful){
                            Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else {
                            Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show()
                        }
                    })
                }
                else{
                    Toast.makeText(this, "Username Already Exist", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun createUser(name: String, email: String) {
        var username: String = usernameEt.text.toString()
        val user = UserInfo(name, email)
        dbReference.child(username).setValue(user)
    }
}
