package com.bapercoding.simplecrud

import android.app.ProgressDialog
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.annotation.Nullable
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.detail_film.*
import kotlinx.android.synthetic.main.fragment_detail.*
import java.lang.reflect.Type


@Suppress("DEPRECATION")
class FragmentDetail : Fragment() {

    private var judul: String? = null
    private var rating: String? = null
    private var episode: String? = null
    private var sinopsis: String? = null
    private var imagePage: String? = null
    private var letak = 0
    private var list2: ArrayList<String> = arrayListOf()
    private var listRating: ArrayList<String> = arrayListOf()
    private var listDetail: ArrayList<String> = arrayListOf()
    private var id: String? = null
    private var watch: String? = null

    // newInstance constructor for creating fragment with arguments
    fun newInstance(letak: Int, judul: String?, rating: String?, episode: String?, sinopsis: String?, imagePage: String?, list2: ArrayList<String>, id: String?, listRating: ArrayList<String>, listDetail: ArrayList<String>, watch: String?): FragmentDetail? {
        val fragmentDetail = FragmentDetail()
        val args = Bundle()
        args.putInt("letak", letak)
        args.putString("judul", judul)
        args.putString("rating", rating)
        args.putString("episode", episode)
        args.putString("sinopsis", sinopsis)
        args.putString("imagePage", imagePage)
        args.putStringArrayList("list2", list2)
        args.putStringArrayList("listRating", listRating)
        args.putStringArrayList("listDetail", listDetail)
        args.putString("id", id)
        args.putString("watch", watch)
        fragmentDetail.setArguments(args)
        return fragmentDetail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        letak = arguments!!.getInt("letak", 0)
        judul = arguments!!.getString("judul")
        rating = arguments!!.getString("rating")
        episode = arguments!!.getString("episode")
        sinopsis = arguments!!.getString("sinopsis")
        imagePage = arguments!!.getString("imagePage")
        list2 = arguments!!.getStringArrayList("list2")
        listRating = arguments!!.getStringArrayList("listRating")
        listDetail = arguments!!.getStringArrayList("listDetail")
        id = arguments!!.getString("id")
        watch = arguments!!.getString("watch")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(
            view: View,
            @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val list = ArrayList<Film>()
        list.addAll(Data.listData)
        rvDetail.setHasFixedSize(true)
        rvDetail.layoutManager = LinearLayoutManager(context)
        val adapter = context?.let { DetailFilmAdapter(it, list, judul!!, rating!!, episode!!, sinopsis!!, imagePage!!, letak, list2, id!!, activity!!, listRating, listDetail, watch!!) }
        adapter?.notifyDataSetChanged()
        rvDetail.adapter = adapter
    }
}
