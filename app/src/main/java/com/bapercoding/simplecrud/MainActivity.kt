package com.bapercoding.simplecrud

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.SmoothScroller
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject


@Suppress("DEPRECATION", "DEPRECATED_IDENTITY_EQUALS")
class MainActivity : AppCompatActivity() {

    var arrayList = ArrayList<Kdramas>()
    private var list: java.util.ArrayList<Film> = arrayListOf()
    val search = Search()
    private lateinit var etSearch: EditText
    var isShow = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        this.supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.custom_action_bar)

        list.addAll(Data.listData)
        searchLayout.visibility = View.GONE
        isShow = false
        tv_nothing.visibility = View.GONE
        btn_back_to_top.visibility = View.GONE

        mRecyclerView1.setHasFixedSize(true)
        mRecyclerView1.layoutManager = LinearLayoutManager(this)
        mRecyclerView1.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {

                //show button when not on top
                val visibility = if ((mRecyclerView1.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() !== 0) View.VISIBLE else View.GONE
                btn_back_to_top.visibility = visibility

                //hide layout when scroll down
                if (dy > 0){
                    val dialog: LinearLayout = findViewById(R.id.searchLayout)
                    dialog.setVisibility(LinearLayout.GONE)
                    val animation: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.anim_hide)
                    animation.setDuration(300)
                    dialog.setAnimation(animation)
                    dialog.animate()
                    animation.start()
                }
            }
        })

        //smooth scroll
        val smoothScroller: SmoothScroller = object : LinearSmoothScroller(applicationContext) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }

        btn_back_to_top.setOnClickListener{
            smoothScroller.targetPosition = 0
            mRecyclerView1.layoutManager.startSmoothScroll(smoothScroller)
            btn_back_to_top.visibility = View.GONE
        }

        etSearch = findViewById(R.id.mytextText)
        btn_search.setOnClickListener {
            val loading = ProgressDialog(this)
            loading.setMessage("Mencari data...")
            loading.show()
            search.listSearch.clear()
            search.list2.clear()
            search.searchJudul(etSearch.text.toString(), arrayList,list)
            loading.dismiss()
            if(search.listSearch.size == 0){
                tv_nothing.visibility = View.VISIBLE
                tv_nothing.text = getString(R.string.nothing_found)
            }
            else{
                tv_nothing.visibility = View.GONE
            }
            val adapter2 = RVAAdapterStudent(applicationContext,search.listSearch,search.list2)
            adapter2.notifyDataSetChanged()
            mRecyclerView1.adapter = adapter2
            val dialog: LinearLayout = findViewById(R.id.searchLayout)
            dialog.animate()
                    .translationY(0F)
                    .alpha(0.0f)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            dialog.setVisibility(View.GONE)
                        }
                    })

            //close virtual keyboard
            closeKeyBoard()
        }



    }

    override fun onResume() {
        super.onResume()
        loadAllStudents()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.about_item) {
            val intentAboutMe = Intent(this@MainActivity, AboutMe::class.java)
            startActivity(intentAboutMe)
            finish()
            searchLayout.visibility = View.GONE
        }
        if(id == R.id.search_item){
            etSearch = findViewById(R.id.mytextText)
            etSearch.text = null
            etSearch.hint = getString(R.string.find_korean_dramas)

            val dialog: LinearLayout = findViewById(R.id.searchLayout)
            val animation = AnimationUtils.loadAnimation(this, R.anim.anim_show)
            animation.duration = 300
            dialog.animation = animation
            dialog.animate()
            animation.start()
            dialog.visibility = LinearLayout.VISIBLE
        }
        return super.onOptionsItemSelected(item)
    }


    private fun loadAllStudents(){

        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()

        AndroidNetworking.get(ApiEndPoint.READ2)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{

                    override fun onResponse(response: JSONObject?) {

                        arrayList.clear()

                        val jsonArray = response?.optJSONArray("result")

                        if(jsonArray?.length() == 0){
                            loading.dismiss()
                            Toast.makeText(applicationContext,"Student data is empty, Add the data first",Toast.LENGTH_SHORT).show()
                        }

                        for(i in 0 until jsonArray?.length()!!){

                            val jsonObject = jsonArray.optJSONObject(i)
                            arrayList.add(Kdramas(jsonObject.getString("judul"),
                                    jsonObject.getString("rating"),
                                    jsonObject.getString("episode"),
                                    jsonObject.getString("sinopsis")))

                            if(jsonArray.length() - 1 == i){

                                loading.dismiss()
                                val adapter = RVAAdapterStudent(applicationContext,arrayList,list)
                                adapter.notifyDataSetChanged()
                                mRecyclerView1.adapter = adapter

                            }

                        }

                    }

                    override fun onError(anError: ANError?) {
                        loading.dismiss()
                        Log.d("ONERROR",anError?.errorDetail?.toString())
                        val snackBar = Snackbar.make(
                                currentFocus, "    Connection Failure",
                                Snackbar.LENGTH_INDEFINITE
                        )
                        val snackBarView = snackBar.view
                        snackBarView.setBackgroundColor(Color.BLACK)
                        val textView = snackBarView.findViewById<TextView>(android.support.design.R.id.snackbar_text)
                        textView.setTextColor(Color.WHITE)
                        textView.setTextSize(16F)
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.warning, 0, 0, 0)
                        val snack_action_view = snackBarView.findViewById<Button>(android.support.design.R.id.snackbar_action)
                        snack_action_view.setTextColor(Color.YELLOW)

                        // Set an action for snack bar
                        snackBar.setAction("Retry") {
                            loadAllStudents()

                        }
                        snackBar.show()
                    }
                })
    }

    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
