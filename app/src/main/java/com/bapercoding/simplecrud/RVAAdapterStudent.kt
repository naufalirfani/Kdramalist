package com.bapercoding.simplecrud

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.student_list.view.*

class RVAAdapterStudent(private val activity: Activity, private val context: Context, private val arrayList: ArrayList<Kdramas>, private val listFilm: ArrayList<Film>, private val listPage: ArrayList<String>) : RecyclerView.Adapter<RVAAdapterStudent.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.student_list,parent,false))
    }

    override fun getItemCount(): Int = arrayList!!.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val film = listFilm[position]
        Glide.with(holder.itemView.context)
                .load(arrayList[position].image)
//                .apply(RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL))
                .into(holder.view.img_item_photo)


//        holder.view.tv_item_name.text = arrayList[position].nim
        val ratingValue = arrayList?.get(position)?.rating!!.toFloat() / 2
        holder.view.ratingbar.rating = ratingValue
        holder.view.tv_item_name.text = arrayList?.get(position)?.judul
        holder.view.tv_item_rating.text = arrayList?.get(position)?.rating
        holder.view.tv_item_detail.text = arrayList?.get(position)?.episode

        holder.view.cvList.setOnClickListener {

            val i = Intent(context,DetailFilmActivity::class.java)
            i.putExtra("position",position)
            i.putExtra("judul",arrayList?.get(position)?.judul)
            i.putExtra("rating",arrayList?.get(position)?.rating)
            i.putExtra("episode",arrayList?.get(position)?.episode)
            i.putExtra("sinopsis",arrayList?.get(position)?.sinopsis)
            i.putExtra("imagePage",arrayList[position].image)
            i.putExtra("detail", arrayList[position].detail)
            i.putExtra("watch", arrayList[position].watch)
            activity.startActivity(i)
            activity.overridePendingTransition(R.anim.enter, R.anim.exit)
        }

    }

    class Holder(val view:View) : RecyclerView.ViewHolder(view)

}