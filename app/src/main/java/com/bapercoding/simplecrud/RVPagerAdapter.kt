package com.bapercoding.simplecrud

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class RVPagerAdapter(val colorList: List<Int>) : RecyclerView.Adapter<SampleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleViewHolder {
        return SampleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_photo, parent, false))
    }

    override fun getItemCount() = colorList.size

    override fun onBindViewHolder(holder: SampleViewHolder, position: Int) {
        holder.setData(colorList[position])
    }

}

class SampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun setData(colorCode: Int) {
        itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, colorCode))
    }
}