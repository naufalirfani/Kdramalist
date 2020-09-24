package com.bapercoding.simplecrud


import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_photo.*
import java.lang.reflect.Type

/**
 * A simple [Fragment] subclass.
 */
class FragmentPhoto : Fragment() {

    private var list: ArrayList<String> = arrayListOf()
    private var list2: ArrayList<String> = arrayListOf()
    private var judul: String = ""

    fun newInstance(list: ArrayList<String>, list2: ArrayList<String>, judul:String): FragmentPhoto? {
        val fragmentPhoto= FragmentPhoto()
        val args = Bundle()
        args.putStringArrayList("list", list)
        args.putStringArrayList("list2", list2)
        args.putString("judul", judul)
        fragmentPhoto.setArguments(args)
        return fragmentPhoto
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        list.clear()
        list2.clear()
        list = arguments!!.getStringArrayList("list")
        list2 = arguments!!.getStringArrayList("list2")
        judul = arguments!!.getString("judul")
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

        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val gson = Gson()
        if(list2.isNotEmpty()){
            val editor: SharedPreferences.Editor = sharedPrefs.edit()
            val json = gson.toJson(list2)

            editor.putString("list", json)
            editor.commit()
        }
        val json = sharedPrefs.getString("list", "")
        val type: Type = object : TypeToken<ArrayList<String?>?>() {}.getType()
        val arrayList: ArrayList<String> = gson.fromJson(json, type)

        rvPhoto.setHasFixedSize(true)
        rvPhoto.layoutManager = GridLayoutManager(context, 3)
        val adapter = context?.let { PhotoFilmAdapter2(it,arrayList, judul) }
        adapter?.notifyDataSetChanged()
        rvPhoto.adapter = adapter
    }
}
