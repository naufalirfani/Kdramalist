package com.bapercoding.simplecrud

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
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
    private  var email: String? = null
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

        if (auth.currentUser != null) {
            // User is signed in (getCurrentUser() will be null if not signed in)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        tv_signUp.setOnClickListener {
            val home = Intent(this@LoginActivity, SingUpActivity::class.java)
            startActivity(home)
            overridePendingTransition(R.anim.enter, R.anim.exit)
            finish()
        }

        btn_login.setOnClickListener {
            if(emailEt.text.toString().contains("@")){
                email = emailEt.text.toString()
            }
            if(!(emailEt.text.toString().contains("@"))){
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
            password = passwordEt.text.toString()

            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Login...")
            progressDialog.show()

            if(TextUtils.isEmpty(password)) {
                progressDialog.dismiss()
                Toast.makeText(this@LoginActivity, "Please fill all the fields", Toast.LENGTH_LONG).show()
            }
            else if(TextUtils.isEmpty(email)){
                progressDialog.dismiss()
                Toast.makeText(this, "Invalid login, please try again", Toast.LENGTH_LONG).show()
            }
            else{
                email?.let { it1 ->
                    auth.signInWithEmailAndPassword(it1, password).addOnCompleteListener(this, OnCompleteListener { task ->
                        if(task.isSuccessful) {
                            progressDialog.dismiss()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else {
                            progressDialog.dismiss()
                            Toast.makeText(this, "Invalid login, please try again", Toast.LENGTH_LONG).show()
                        }
                    })
                }
            }
        }
    }
}
