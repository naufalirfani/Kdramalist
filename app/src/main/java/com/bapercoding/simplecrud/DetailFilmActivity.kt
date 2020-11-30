package com.bapercoding.simplecrud

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.support.design.widget.TabLayout
import android.support.design.widget.TabLayout.OnTabSelectedListener
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.FileProvider
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_detail_film.*
import java.io.File
import java.io.IOException
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DetailFilmActivity : AppCompatActivity() {

    private var list: ArrayList<Film> = arrayListOf()
    var letak: Int = 0
    lateinit var judul:String
    lateinit var rating:String
    lateinit var episode:String
    lateinit var sinopsis:String
    lateinit var imagepage:String
    lateinit var watch:String
    private var listDetail: ArrayList<String> = arrayListOf()
    private val  ratingUser = ArrayList<String>()
    private  var jumlahuserRating: Int = 0
    private lateinit var layout: RelativeLayout
    private lateinit var tabLayout1: TabLayout
    val iterator = arrayOf('a','b','c','d','e','f','g','h','i','j','k')
    private val listPhoto2 = ArrayList<String>()
    var listPhoto3: ArrayList<Int> = arrayListOf()
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var filePath: Uri? = null
    private lateinit var dbReference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var id: String

    object Constants {
        const val STORAGE_PATH_UPLOADS = "uploads/"
        const val DATABASE_PATH_UPLOADS = "uploads"
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_film)

        layout = findViewById(R.id.relativeLayout)
//        layout4 = findViewById(R.id.rvPager)
//        layout4.setOnTouchListener(object : OnSwipeTouchListener(this@DetailFilmActivity) {
//            override fun onSwipeRight() {
//                super.onSwipeRight()
//                onBackPressed()
//            }
//        })

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        judul = intent.getStringExtra("judul")
        rating = intent.getStringExtra("rating")
        episode = intent.getStringExtra("episode")
        sinopsis = intent.getStringExtra("sinopsis")
        imagepage = intent.getStringExtra("imagePage")
        letak = intent.getIntExtra("position",0)
        listDetail = intent.getStringArrayListExtra("detail")
        watch = intent.getStringExtra("watch")

        val tvDataReceived: TextView = findViewById(R.id.tv_data_received)
        tvDataReceived.text = judul

        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = ""

        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        list.addAll(Data.listData)


//        val bottomBorder: LayerDrawable? = getBorders(Color.WHITE, Color.LTGRAY,0,0,0,15)
//        tvDetail.setBackground(bottomBorder)

        mFloatingActionButton2.visibility = View.GONE

        tabLayout1 = findViewById<View>(R.id.tabLayout) as TabLayout
        tabLayout1.addTab(tabLayout1.newTab().setText("Details"))
        tabLayout1.addTab(tabLayout1.newTab().setText("Cast"))
        tabLayout1.addTab(tabLayout1.newTab().setText("Episodes"))
        tabLayout1.addTab(tabLayout1.newTab().setText("Photos"))

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        id = user!!.uid

        getPhotos()
        getRating()
        tabClick()

        val pagerAdapter = PagerAdapter(supportFragmentManager, ratingUser, listPhoto2, letak, judul, rating, episode, sinopsis, imagepage, id, listDetail, watch)
        val pager = findViewById<View>(R.id.pager) as ViewPager
        pager.adapter = pagerAdapter
        tabLayout1.setupWithViewPager(pager)
    }

    override fun onRestart() {
        super.onRestart()
        listPhoto2.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu3, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.refresh) {
            val loading = ProgressDialog(this)
            loading.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            loading.setIndeterminate(true)
            loading.setCancelable(true)
            loading.show()
            loading.setContentView(R.layout.progressdialog)
            val handler = Handler()
            handler.postDelayed(Runnable { // Do something after 5s = 5000ms
                loading.dismiss()
                finish()
                startActivity(intent)
            }, 4000)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val db = FirebaseFirestore.getInstance()
        db.collection("kdramas").document(judul)
                .update("watch", (watch.toInt() + 1).toString())
                .addOnSuccessListener { result ->
                }
                .addOnFailureListener { exception ->
                }

        val intentMain = Intent(this@DetailFilmActivity, MainActivity::class.java)
        startActivity(intentMain)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        finish()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun getPhotos(){
        val dbReference2 = FirebaseDatabase.getInstance().getReference("images")
        val postListener2 = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(listPhoto2.isNotEmpty()){
                    listPhoto2.clear()
                }
                for (data: DataSnapshot in dataSnapshot.children){
                    val hasil = data.getValue(Upload::class.java)
                    listPhoto2.add(hasil?.url!!)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        var judul2 = judul
        if(judul2.contains(".")){
            judul2 = judul2.replace(".", "")
            dbReference2.child(judul2).addValueEventListener(postListener2)
        }
        else{
            dbReference2.child(judul2).addValueEventListener(postListener2)
        }
    }

    fun getRating(){
        ratingUser.add(0,"0")
        ratingUser.add(1,"0")
        jumlahuserRating = 0
        firebaseDatabase = FirebaseDatabase.getInstance()
        val dbReferenceR = firebaseDatabase.getReference("userRating")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                for(data: DataSnapshot in dataSnapshot.children){
                    val user2 = data.getValue(Upload::class.java)
                    if(user2?.name!!.isNotEmpty()){
                        if(user2.name == judul){
                            ratingUser.add(0, user2.url!!)
                        }
                        jumlahuserRating += 1
                        ratingUser.add(1, jumlahuserRating.toString())
                    }
                    else{
                        ratingUser.add("0")
                        ratingUser.add("0")
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                ratingUser.add("0")
                ratingUser.add("0")
            }
        }
        var judul2 = judul
        if(judul2.contains(".")){
            judul2 = judul2.replace(".", "")
            dbReferenceR.child(judul2).addValueEventListener(postListener)
        }
        else{
            dbReferenceR.child(judul2).addValueEventListener(postListener)
        }
    }

    fun getFileExtension(uri: Uri?): String? {
        val cR: ContentResolver = contentResolver
        val mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

    fun tabClick(){
        tabLayout1.setTabTextColors(Color.parseColor("#e1f5fe"), Color.parseColor("#2196F3"))
        tabLayout1.setSelectedTabIndicatorHeight(7)
        tabLayout1.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tabLayout1.selectedTabPosition == 0) {
                    mFloatingActionButton2.visibility = View.GONE

                } else if (tabLayout1.selectedTabPosition == 1) {
                    mFloatingActionButton2.visibility = View.GONE

                } else if (tabLayout1.selectedTabPosition == 2) {
                    mFloatingActionButton2.visibility = View.GONE

                } else if (tabLayout1.selectedTabPosition == 3) {
                    mFloatingActionButton2.visibility = View.VISIBLE
                    mFloatingActionButton2.setOnClickListener {
                        dispatchTakePictureIntent()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    val REQUEST_TAKE_PHOTO = 1

    private fun dispatchTakePictureIntent() {
        val options =
                arrayOf<CharSequence>("Camera", "Choose from Gallery", "Cancel")
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@DetailFilmActivity)
        builder.setItems(options) { dialog, item ->
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
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    lateinit var currentPhotoPath: String

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/${judul}")
        return File.createTempFile(
                "JPEG_${judul}_", /* prefix */
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

    object GenerateNama{
        const val DATA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        var RANDOM = Random()
        fun randomString(len: Int): String {
            val sb = StringBuilder(len)
            for (i in 0 until len) {
                sb.append(DATA[RANDOM.nextInt(DATA.length)])
            }
            return sb.toString()
        }
    }

    private fun uploadImage(){
        firebaseDatabase = FirebaseDatabase.getInstance()
        dbReference = firebaseDatabase.getReference("images")
        val progressDialog = ProgressDialog(this)
        progressDialog.show()
        if(filePath != null){
            val ref = storageReference?.child("images/${judul}/" + judul + "_" + GenerateNama.randomString(10))
            ref?.putFile(filePath!!)?.addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> {taskSnapshot ->
                ref.downloadUrl.addOnSuccessListener {
                    val name = taskSnapshot.metadata!!.name
                    val url = it.toString()
                    writeNewImageInfoToDB(name!!, url)
                    listPhoto2.add(url)
                }.addOnFailureListener {}
                progressDialog.dismiss()
                Toast.makeText(this@DetailFilmActivity, "Image Uploaded", Toast.LENGTH_LONG).show()
                finish()
                startActivity(getIntent())
            })?.addOnFailureListener(OnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this@DetailFilmActivity, "Image Uploading Failed " + e.message, Toast.LENGTH_LONG).show()
            })?.addOnProgressListener (OnProgressListener{taskSnapshot ->
                val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                progressDialog.setMessage("Uploaded " + progress.toInt() + "%...")
            })
        }else{
            Toast.makeText(this, "Please Select an Image", Toast.LENGTH_LONG).show()
        }
    }

    private fun writeNewImageInfoToDB(name: String, url: String) {
        val info = Upload(name, url)
        val key: String? = dbReference.push().getKey()
        if(judul.contains(".")){
            judul = judul.replace(".", "")
            dbReference.child(judul).child(key!!).setValue(info)
        }
        else{
            dbReference.child(judul).child(key!!).setValue(info)
        }
    }

//    private fun loadImage(){
//        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/${judul}")
//        val dirlist = storageDir.listFiles()
//        try {
//            for(i in dirlist.indices){
//                val b = BitmapFactory.decodeStream(FileInputStream(dirlist[i]))
//                listPhoto2.add(b)
//            }
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//        }
//        tabLayout1 = findViewById<View>(R.id.tabLayout) as TabLayout
//        val pagerAdapter = PagerAdapter(supportFragmentManager, listPhoto3, listPhoto2, letak, judul, rating, episode, sinopsis)
//        val pager = findViewById<View>(R.id.pager) as ViewPager
//        pager.adapter = pagerAdapter
//        tabLayout1.setupWithViewPager(pager)
//    }
//
//    private fun saveToInternalStorage(bitmapImage: Bitmap): String? {
//
//        val namajpg = GenerateNama.randomString(10)
//        val cw = ContextWrapper(applicationContext)
//        // path to /data/data/yourapp/app_data/imageDir
//        val directory = cw.getDir("imageDir" + "/${judul}", Context.MODE_PRIVATE)
//        // Create imageDir
//        val mypath = File(directory, "$namajpg.jpg")
//        var fos: FileOutputStream? = null
//        try {
//            fos = FileOutputStream(mypath)
//            // Use the compress method on the BitMap object to write image to the OutputStream
//            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        } finally {
//            try {
//                fos?.close()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//        return directory.absolutePath
//    }

//    private fun setPic() {
//        // Get the dimensions of the View
//        val targetW: Int = imageView.width
//        val targetH: Int = imageView.height
//
//        val bmOptions = BitmapFactory.Options().apply {
//            // Get the dimensions of the bitmap
//            inJustDecodeBounds = true
//
//            val photoW: Int = outWidth
//            val photoH: Int = outHeight
//
//            // Determine how much to scale down the image
//            val scaleFactor: Int = Math.min(photoW / targetW, photoH / targetH)
//
//            // Decode the image file into a Bitmap sized to fill the View
//            inJustDecodeBounds = false
//            inSampleSize = scaleFactor
//            inPurgeable = true
//        }
//        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
//            imageView.setImageBitmap(bitmap)
//        }
//    }

//    private fun loadImageFromStorage() {
//        val cw = ContextWrapper(applicationContext)
//        // path to /data/data/yourapp/app_data/imageDir
//        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
//        val dirlist = directory.listFiles()
//        try {
//            for(i in dirlist.indices){
//                if(i == 0){
//                    val b = BitmapFactory.decodeStream(FileInputStream(dirlist[i]))
//                    click_image.setImageBitmap(b)
//                }
//                else{
//                    val b = BitmapFactory.decodeStream(FileInputStream(dirlist[i]))
//                    click_image.setImageBitmap(b)
//                }
//            }
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//        }
//    }

//    protected fun getBorders(
//            bgColor: Int, borderColor: Int,
//            left: Int, top: Int, right: Int, bottom: Int
//    ): LayerDrawable? {
//        // Initialize new color drawables
//        val borderColorDrawable = ColorDrawable(borderColor)
//        val backgroundColorDrawable = ColorDrawable(bgColor)
//
//        // Initialize a new array of drawable objects
//        val drawables = arrayOf<Drawable>(
//                borderColorDrawable,
//                backgroundColorDrawable
//        )
//
//        // Initialize a new layer drawable instance from drawables array
//        val layerDrawable = LayerDrawable(drawables)
//
//        // Set padding for background color layer
//        layerDrawable.setLayerInset(
//                1,  // Index of the drawable to adjust [background color layer]
//                left,  // Number of pixels to add to the left bound [left border]
//                top,  // Number of pixels to add to the top bound [top border]
//                right,  // Number of pixels to add to the right bound [right border]
//                bottom // Number of pixels to add to the bottom bound [bottom border]
//        )
//
//        // Finally, return the one or more sided bordered background drawable
//        return layerDrawable
//    }
}
