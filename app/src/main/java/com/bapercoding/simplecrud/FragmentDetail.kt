package com.bapercoding.simplecrud

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class FragmentDetail : Fragment() {

    private var judul: String? = null
    private var rating: String? = null
    private var episode: String? = null
    private var sinopsis: String? = null
    private var imagePage: String? = null
    private var letak = 0

    // newInstance constructor for creating fragment with arguments
    fun newInstance(letak: Int, judul: String?, rating: String?, episode: String?, sinopsis: String?, imagePage: String?): FragmentDetail? {
        val fragmentDetail = FragmentDetail()
        val args = Bundle()
        args.putInt("letak", letak)
        args.putString("judul", judul)
        args.putString("rating", rating)
        args.putString("episode", episode)
        args.putString("sinopsis", sinopsis)
        args.putString("imagePage", imagePage)
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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val view: View = inflater.inflate(R.layout.fragment_detail, container, false) //Inflate Layout
        val list = ArrayList<Film>()
        list.addAll(Data.listData)
        val rv:RecyclerView = view.findViewById(R.id.rvDetail)
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(context)
        val adapter = DetailFilmAdapter(context!!, list, judul!!, rating!!, episode!!, sinopsis!!, imagePage!!, letak)
        adapter?.notifyDataSetChanged()
        rv.adapter = adapter

        return view //return view

    }
}
