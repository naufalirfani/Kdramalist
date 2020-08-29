package com.bapercoding.simplecrud

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        this.getSupportActionBar()?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        getSupportActionBar()?.setDisplayShowCustomEnabled(true)
        getSupportActionBar()?.setCustomView(R.layout.custom_action_bar)

        list.addAll(Data.listData)
        searchLayout.visibility = View.GONE
        tv_nothing.visibility = View.GONE
        btn_back_to_top.visibility = View.GONE

        mRecyclerView1.setHasFixedSize(true)
        mRecyclerView1.layoutManager = LinearLayoutManager(this)
        mRecyclerView1.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {

                //show button when not on top
                val visibility = if ((mRecyclerView1.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() !== 0) View.VISIBLE else View.GONE
                btn_back_to_top.setVisibility(visibility)

                //hide layout when scroll down
                if (dy > 0){
                    searchLayout.visibility = View.GONE
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
            searchLayout.visibility = View.GONE

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
            searchLayout.visibility = View.GONE
        }
        if(id == R.id.search_item){
            searchLayout.visibility = View.VISIBLE
            etSearch = findViewById(R.id.mytextText)
            etSearch.text = null
            etSearch.hint = getString(R.string.find_korean_dramas)
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

                            val jsonObject = jsonArray?.optJSONObject(i)
                            arrayList.add(Kdramas(jsonObject.getString("judul"),
                                    jsonObject.getString("rating"),
                                    jsonObject.getString("episode"),
                                    jsonObject.getString("sinopsis")))

                            if(jsonArray?.length() - 1 == i){

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
                        Toast.makeText(applicationContext,"Connection Failure",Toast.LENGTH_SHORT).show()
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
