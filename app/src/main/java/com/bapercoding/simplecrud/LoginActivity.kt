package com.bapercoding.simplecrud

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*


@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var dbReference2: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase

    private var userId: String = ""

    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var loginBtn: Button
    private  var email: String? = null
    private lateinit var password: String
    var sp: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val actionbar = supportActionBar
        actionbar!!.title = ""
        actionbar.hide()

        val tvsignUp: TextView = findViewById(R.id.tv_signUp)
        val tvforgot: TextView = findViewById(R.id.tv_forgot)
        val tvwelcome: TextView = findViewById(R.id.tv_welcome)
        val tvdoyou: TextView = findViewById(R.id.tv_doyou)
        tvsignUp.text = resources.getString(R.string.sign_up)
        tvforgot.text = resources.getString(R.string.forgot_password)
        tvwelcome.text = resources.getString(R.string.welcome_to_kdramalist)
        tvdoyou.text = resources.getString(R.string.don_t_have_an_account)

        val textView = login_google.getChildAt(0) as TextView
        textView.text = "Google"

        emailEt = findViewById(R.id.et_email_login)
        passwordEt = findViewById(R.id.et_pass_login)
        loginBtn = findViewById(R.id.btn_login)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        firebaseDatabase = FirebaseDatabase.getInstance()
        dbReference = firebaseDatabase.getReference("users")
        dbReference2 = firebaseDatabase.getReference("users2")
        userId = dbReference.push().key.toString()

        tv_signUp.setOnClickListener {
            val home = Intent(this@LoginActivity, SingUpActivity::class.java)
            startActivity(home)
            overridePendingTransition(R.anim.enter, R.anim.exit)
            finish()
        }

        btn_login.setOnClickListener {
            getEmail()
            password = passwordEt.text.toString()
            var iterator = 0

            val progressDialog = ProgressDialog(this)
            progressDialog.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog.setIndeterminate(true)
            progressDialog.setCancelable(true)
            progressDialog.show()
            progressDialog.setContentView(R.layout.progressdialog)


            if(TextUtils.isEmpty(password)) {
                progressDialog.dismiss()
                Toast.makeText(this@LoginActivity, "Please fill all the fields", Toast.LENGTH_LONG).show()
            }
            else if(TextUtils.isEmpty(email)){
                progressDialog.dismiss()
                getEmail()
            }
            else{
                email?.let { it1 ->
                    auth.signInWithEmailAndPassword(it1, password).addOnCompleteListener(this, OnCompleteListener { task ->
                        if(task.isSuccessful) {
                            progressDialog.dismiss()
                            val username = emailEt.text.toString()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("username",username)
                            startActivity(intent)
                            overridePendingTransition(R.anim.enter, R.anim.exit)
                            finish()
                        }else {
                            progressDialog.dismiss()
                            Toast.makeText(this, "Invalid login, please try again", Toast.LENGTH_LONG).show()
                        }
                    })
                }
            }
        }

        sp = getSharedPreferences("login",MODE_PRIVATE)

        if(sp!!.getBoolean("login",true)){
            if (user != null) {
                // User is signed in (getCurrentUser() will be null if not signed in)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.enter, R.anim.exit)
                finish()
            }
        }

        val radio: RadioButton = findViewById(R.id.radio_remember)
        var isChecked = false
        radio.isChecked = true
        if(radio.isChecked == true){
            sp?.edit()?.putBoolean("login",true)?.apply()
        }
        radio.setOnClickListener{
            radio.isChecked = isChecked
            if(isChecked == true){
                isChecked = !isChecked
                sp?.edit()?.putBoolean("login",true)?.apply()
            }
            else{
                isChecked = !isChecked
                sp?.edit()?.putBoolean("login",false)?.apply()
            }

        }
    }

    fun getEmail(){
        if(emailEt.text.toString().contains("@")){
            email = emailEt.text.toString()
        }
        if(!(emailEt.text.toString().contains("@"))){
            val username = emailEt.text.toString()
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    val user2 = dataSnapshot.getValue(UserInfo::class.java)
                    if (user2 != null){
                        email = user2.email
                    }
                    // ...
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            }
            dbReference.child(username).addValueEventListener(postListener)
        }
    }
}
