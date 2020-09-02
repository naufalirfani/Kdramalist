package com.bapercoding.simplecrud

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.list_photo.view.*

class RVPagerAdapter(val colorList: List<Int>) : RecyclerView.Adapter<SampleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleViewHolder {
        return SampleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_photo, parent, false))
    }

    override fun getItemCount() = colorList.size

    override fun onBindViewHolder(holder: SampleViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
                .load(colorList[position])
                .apply(RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL))
                .into(holder.itemView.img_kdrama_photo)
    }
}

class SampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
}