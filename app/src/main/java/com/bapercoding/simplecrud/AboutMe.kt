package com.bapercoding.simplecrud

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ContextThemeWrapper
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_about_me.*
import java.io.File
import java.io.IOException


@Suppress("DEPRECATION")
class AboutMe : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var dbReference2: DatabaseReference
    private lateinit var dbReference3: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var id: String
    private var filePath: Uri? = null
    private var storageReference: StorageReference? = null

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

        val myName: TextView = findViewById(R.id.tv_name)
        val email: TextView = findViewById(R.id.tv_email)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        firebaseDatabase = FirebaseDatabase.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        dbReference = firebaseDatabase.getReference("users2")
        dbReference2 = firebaseDatabase.getReference("imageProfil")
        if (user != null) {
            id = user.uid
            val email2 = user.email
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    val user2 = dataSnapshot.getValue(UserInfo::class.java)
                    if (user2 != null){
                        myName.text = user2.username
                        email.text = user2.email
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            }
            dbReference.child(id).addValueEventListener(postListener)

            val postListener2 = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    val user2 = dataSnapshot.getValue(Upload::class.java)
                    if (user2 != null){
                        if(TextUtils.isEmpty(user2.url)){
                            iv.setImageDrawable(resources.getDrawable(R.drawable.ic_account_circle_grey_24dp))
                        }
                        else{
                            Glide.with(applicationContext)
                                    .load(user2.url)
                                    .apply(RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL))
                                    .into(iv)
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            }
            dbReference2.child(id).addValueEventListener(postListener2)

        }

        change_photo.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intentMain = Intent(this@AboutMe, MainActivity::class.java)
        startActivity(intentMain)
        overridePendingTransition(R.anim.enter, R.anim.exit)
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

    val REQUEST_TAKE_PHOTO = 1
    private fun dispatchTakePictureIntent() {
        val options =
                arrayOf<CharSequence>("Camera", "Choose from Gallery", "Delete")
        val items = arrayOf(
                Item("Camera", R.drawable.camera),
                Item("Choose from gallery", R.drawable.gallery),
                Item("Delete", R.drawable.delete))
        val adapter: ListAdapter = object : ArrayAdapter<Item?>(
                this,
                android.R.layout.select_dialog_item,
                android.R.id.text1,
                items) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
                //Use super class to create the View
                val v: View = super.getView(position, convertView, parent!!)
                val tv = v.findViewById(android.R.id.text1) as TextView
                tv.setTextSize(16F)

                //Put the image on the TextView
                val image: Drawable
                val res: Resources = resources
                image = res.getDrawable(items.get(position).icon)
                image.setBounds(0, 0, 100, 100)
                tv.setCompoundDrawables(image, null, null, null)

                //Add margin between image and text (support various screen densities)
                val dp5 = (15 * resources.displayMetrics.density + 0.5f).toInt()
                tv.compoundDrawablePadding = dp5

                return v
            }
        }
        val cw = ContextThemeWrapper(this, R.style.AlertDialogTheme)
        AlertDialog.Builder(cw)
                .setAdapter(adapter) { dialog, item ->
                    if (options[item].equals("Camera")) {
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                            // Ensure that there's a camera activity to handle the intent
                            takePictureIntent.resolveActivity(packageManager)?.also {
                                // Create the File where the photo should go
                                val photoFile: File? = try {
                                    createImageFile()
                                } catch (ex: IOException) {
                                    // Error occurred while creating the File
                                    null
                                }
                                // Continue only if the File was successfully created
                                photoFile?.also {
                                    val photoURI: Uri = FileProvider.getUriForFile(
                                            this,
                                            "com.bapercoding.simplecrud.fileprovider",
                                            it
                                    )
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                                }
                            }
                        }
                        dialog.dismiss()
                    } else if (options[item].equals("Choose from Gallery")) {
                        val intent = Intent()
                        intent.type = "image/*"
                        intent.action = Intent.ACTION_GET_CONTENT
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2)
                        dialog.dismiss()
                    } else if (options[item].equals("Delete")) {
                        val loading = ProgressDialog(this)
                        loading.setMessage("Loading...")
                        loading.show()
                        val handler = Handler()
                        handler.postDelayed(Runnable { // Do something after 5s = 5000ms
                            loading.dismiss()
                            dbReference3 = firebaseDatabase.getReference("imageProfil")
                            dbReference3.child(id).child("url").setValue(null)
                        }, 3000)
                    }
                }.show()
    }

    class Item(val text: String, val icon: Int) {
        override fun toString(): String {
            return text
        }

    }

    lateinit var currentPhotoPath: String

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/${id}")
        return File.createTempFile(
                "JPEG_${id}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            2 -> if (resultCode == Activity.RESULT_OK) {
                filePath = data!!.data
                uploadImage()
//                try {
//                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
//                    listPhoto2.add(bitmap)
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
            }
            1 -> if (resultCode == Activity.RESULT_OK) {
                val imgFile = File(currentPhotoPath)
                filePath = Uri.fromFile(imgFile)
                uploadImage()
//                val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
//                listPhoto2.add(Photo2(myBitmap))
//                saveToInternalStorage(imageBitmap)
//                listPhoto2.add(Photo2(imageBitmap))
//                loadImage()
            }
        }
    }

    private fun uploadImage(){
        firebaseDatabase = FirebaseDatabase.getInstance()
        dbReference3 = firebaseDatabase.getReference("imageProfil")
        val progressDialog = ProgressDialog(this)
        progressDialog.show()
        if(filePath != null){
            val ref = storageReference?.child("imageProfile/${id}/foto")
            ref?.putFile(filePath!!)?.addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                ref.downloadUrl.addOnSuccessListener {
                    val name = taskSnapshot.metadata!!.name
                    val url = it.toString()
                    writeNewImageInfoToDB(name!!, url)
                }.addOnFailureListener {}
                progressDialog.dismiss()
                Toast.makeText(this@AboutMe, "Image proflie changed", Toast.LENGTH_LONG).show()
            })?.addOnFailureListener(OnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this@AboutMe, "Image Uploading Failed " + e.message, Toast.LENGTH_LONG).show()
            })?.addOnProgressListener (OnProgressListener{ taskSnapshot ->
                val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                progressDialog.setMessage("Uploaded " + progress.toInt() + "%...")
            })
        }else{
            Toast.makeText(this, "Please Select an Image", Toast.LENGTH_LONG).show()
        }
    }

    private fun writeNewImageInfoToDB(name: String, url: String) {
        val info = Upload(name, url)
        dbReference3.child(id).setValue(info)
    }
}
