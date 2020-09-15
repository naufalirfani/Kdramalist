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
    private lateinit var repasswordEt: EditText

    private lateinit var signUpBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)

        val actionbar = supportActionBar
        actionbar!!.title = ""
        actionbar.hide()

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        dbReference = firebaseDatabase.getReference("users")

        usernameEt = findViewById(R.id.et_username_sign)
        emailEt = findViewById(R.id.et_email_sign)
        passwordEt = findViewById(R.id.et_pass_sign)
        repasswordEt = findViewById(R.id.et_reenterpass_sign)
        signUpBtn = findViewById(R.id.btn_signUp)
        val tvcreate: TextView = findViewById(R.id.tv_create)
        val tvHave: TextView = findViewById(R.id.tv_have)
        val tvLogin: TextView = findViewById(R.id.tv_Login)
        tvHave.text = resources.getString(R.string.have_an_account)
        tvLogin.text = resources.getString(R.string.login)
        tvcreate.setText(getString(R.string.create_an_account))

        tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
            finish()
        }

        signUpBtn.setOnClickListener{
            var username: String = usernameEt.text.toString()
            var email: String = emailEt.text.toString()
            var password: String = passwordEt.text.toString()
            var repassword: String = repasswordEt.text.toString()

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(username) || TextUtils.isEmpty(repassword)) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show()
            }
            else if(password != repassword){
                Toast.makeText(this, "Incorrect password", Toast.LENGTH_LONG).show()
            }
            else{
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
                            Toast.makeText(this, "Successfully registered", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
                            finish()
                        }else {
                            Toast.makeText(this, "Username or rmail already used", Toast.LENGTH_LONG).show()
                        }
                    })
                }
                else{
                    Toast.makeText(this, "Username or rmail already used", Toast.LENGTH_LONG).show()
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
