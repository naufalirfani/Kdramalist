package com.bapercoding.simplecrud


import android.graphics.Bitmap
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_photo.*

/**
 * A simple [Fragment] subclass.
 */
class FragmentPhoto : Fragment() {

    private var list: ArrayList<Int> = arrayListOf()
    private var list2: ArrayList<Bitmap> = arrayListOf()

    fun newInstance(list: ArrayList<Int>, list2: ArrayList<Bitmap>): FragmentPhoto? {
        val fragmentPhoto= FragmentPhoto()
        val args = Bundle()
        args.putIntegerArrayList("list", list)
        args.putParcelableArrayList("list2", list2)
        fragmentPhoto.setArguments(args)
        return fragmentPhoto
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        list = arguments!!.getIntegerArrayList("list")
        list2 = arguments!!.getParcelableArrayList("list2")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onViewCreated(
            view: View,
            @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        rvPhoto.setHasFixedSize(true)
        rvPhoto.layoutManager = LinearLayoutManager(context)
        val adapter = context?.let { PhotoFilmAdapter2(it,list2) }
        adapter?.notifyDataSetChanged()
        rvPhoto.adapter = adapter
    }
}
