package com.bapercoding.simplecrud

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.list_photo.view.*
import kotlinx.android.synthetic.main.student_list.view.*


class PhotoFilmAdapter2(private val context: Context, private val listPhoto: ArrayList<String>, private val judul: String) : RecyclerView.Adapter<PhotoFilmAdapter2.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_photo,parent,false))
    }

    override fun getItemCount(): Int = listPhoto.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Glide.with(holder.itemView.context)
                .load(listPhoto[position])
                .apply(RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL))
                .into(holder.view.img_kdrama_photo)

//        holder.view.img_kdrama_photo.setImageResource(photo.photo)
        holder.itemView.setOnClickListener {

            val i = Intent(context,PhotoActivity::class.java)
            i.putExtra("position",position)
            i.putStringArrayListExtra("list", listPhoto)
            i.putExtra("judul", judul)
            context.startActivity(i)
        }

    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

}