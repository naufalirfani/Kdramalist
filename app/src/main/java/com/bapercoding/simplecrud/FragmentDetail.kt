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
    private var letak = 0

    // newInstance constructor for creating fragment with arguments
    fun newInstance(letak: Int, judul: String?, rating: String?, episode: String?, sinopsis: String?): FragmentDetail? {
        val fragmentFirst = FragmentDetail()
        val args = Bundle()
        args.putInt("letak", letak)
        args.putString("judul", judul)
        args.putString("rating", rating)
        args.putString("episode", episode)
        args.putString("sinopsis", sinopsis)
        fragmentFirst.setArguments(args)
        return fragmentFirst
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        letak = arguments!!.getInt("letak", 0)
        judul = arguments!!.getString("judul")
        rating = arguments!!.getString("rating")
        episode = arguments!!.getString("episode")
        sinopsis = arguments!!.getString("sinopsis")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val view: View = inflater.inflate(R.layout.fragment_detail, container, false) //Inflate Layout
//        val judul = arguments!!.getString("judul")
//        val rating = arguments!!.getString("rating")
//        val episode = arguments!!.getString("episode")
//        val sinopsis = arguments!!.getString("sinopsis")
//        val letak = arguments!!.getInt("letak")
        val list = ArrayList<Film>()
        list.addAll(Data.listData)
        val rv:RecyclerView = view.findViewById(R.id.rvDetail)
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(context)
        val adapter = context?.let { judul?.let { it1 -> rating?.let { it2 -> episode?.let { it3 -> sinopsis?.let { it4 -> DetailFilmAdapter(it,list, it1, it2, it3, it4, letak) } } } } }
        adapter?.notifyDataSetChanged()
        rv.adapter = adapter

        return view //return view

    }
}
