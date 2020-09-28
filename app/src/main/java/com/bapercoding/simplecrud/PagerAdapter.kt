package com.bapercoding.simplecrud

import android.graphics.Bitmap
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

@Suppress("DEPRECATION")
class PagerAdapter (fm: FragmentManager,
                    private val list: ArrayList<String>,
                    private val list2: ArrayList<String>,
                    private val letak: Int,
                    private val judul: String,
                    private val rating: String,
                    private val episode: String,
                    private val sinopsis: String,
                    private val imagePage: String,
                    private var id: String,
                    private val listDetail: ArrayList<String>,
                    private val watch: String) : FragmentStatePagerAdapter(fm){

    private val tabName : Array<String> = arrayOf("Details", "Cast", "Episodes", "Photos")

    override fun getItem(position: Int): Fragment {
        return when (position) {

            0 -> FragmentDetail().newInstance(letak,judul, rating, episode, sinopsis, imagePage,list2, id, list, listDetail, watch)!!
            1 -> FragmentCast()
            2 -> FragmentEpisode()
            else -> FragmentPhoto().newInstance(list,list2,judul)!!
        }
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence? = tabName[position]
}