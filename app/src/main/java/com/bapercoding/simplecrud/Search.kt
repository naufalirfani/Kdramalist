package com.bapercoding.simplecrud

import android.annotation.SuppressLint

class Search {

    var listSearch: ArrayList<Kdramas> = ArrayList()
    var list2: ArrayList<Film> = arrayListOf()
    var list3: ArrayList<String> = arrayListOf()
    @SuppressLint("DefaultLocale")
    fun searchJudul(judul: String, kdrama: ArrayList<Kdramas>, listFilm: ArrayList<Film>, listPage: ArrayList<String>){
        val dicari = judul.toLowerCase()
        val pattern = dicari.toRegex()
        for (position in 0 until kdrama.size){
            val list = kdrama[position]
            val judulLower = list.judul.toLowerCase()
            if(pattern.containsMatchIn(judulLower)){
                listSearch.add(list)
                list2.add(listFilm[position])
                list3.add(listPage[position])
            }
        }

    }
}