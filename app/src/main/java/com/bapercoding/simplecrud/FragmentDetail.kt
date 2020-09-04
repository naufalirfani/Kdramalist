package com.bapercoding.simplecrud

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_detail.*


class FragmentDetail : Fragment() {

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
        val adapter = context?.let { DetailFilmAdapter(it,list,"Ulk", "jJ", "Jk", "hjk", 1) }
        adapter?.notifyDataSetChanged()
        rv.adapter = adapter

        return view //return view

    }
}
