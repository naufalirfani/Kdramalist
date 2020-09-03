package com.bapercoding.simplecrud

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

@Suppress("DEPRECATION")
class PagerAdapter (fm: FragmentManager) : FragmentStatePagerAdapter(fm){

    private val tabName : Array<String> = arrayOf("Details", "Cast", "Episodes", "Photos")

    override fun getItem(position: Int): Fragment {
        return when (position) {

            0 -> FragmentDetail()
            1 -> FragmentCast()
            2 -> FragmentEpisode()
            else -> FragmentPhoto()
        }
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence? = tabName[position]
}