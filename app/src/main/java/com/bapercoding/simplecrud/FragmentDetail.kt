package com.bapercoding.simplecrud

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.detail_film.*
import kotlinx.android.synthetic.main.fragment_detail.*


class FragmentDetail : Fragment() {

    private var judul: String? = null
    private var rating: String? = null
    private var episode: String? = null
    private var sinopsis: String? = null
    private var imagePage: String? = null
    private var letak = 0
    private var list2: ArrayList<String> = arrayListOf()

    // newInstance constructor for creating fragment with arguments
    fun newInstance(letak: Int, judul: String?, rating: String?, episode: String?, sinopsis: String?, imagePage: String?, list2: ArrayList<String>): FragmentDetail? {
        val fragmentDetail = FragmentDetail()
        val args = Bundle()
        args.putInt("letak", letak)
        args.putString("judul", judul)
        args.putString("rating", rating)
        args.putString("episode", episode)
        args.putString("sinopsis", sinopsis)
        args.putString("imagePage", imagePage)
        args.putStringArrayList("list2", list2)
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
        val adapter = context?.let { DetailFilmAdapter(it, list, judul!!, rating!!, episode!!, sinopsis!!, imagePage!!, letak, list2) }
        adapter?.notifyDataSetChanged()
        rvDetail.adapter = adapter
    }
}
