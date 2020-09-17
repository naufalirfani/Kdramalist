package com.bapercoding.simplecrud

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.detail_film.view.*

class DetailFilmAdapter(private val context: Context, private val listFilm: ArrayList<Film>, private val judul: String, private val rating: String, private val episode: String, private val sinopsis: String, private val imagePage: String, private val letak: Int) : RecyclerView.Adapter<DetailFilmAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.detail_film,parent,false))
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val film = listFilm[letak]
        Glide.with(holder.itemView.context)
                .load(imagePage)
//                .apply(RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL))
                .into(holder.view.img_item_photo2)

        holder.view.ratingbar.setOnRatingBarChangeListener(object : RatingBar.OnRatingBarChangeListener {
            override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {
                holder.view.tv_rating_user.text = (p1*2).toString()
            }
        })

        holder.view.tv_rating.text = rating
        holder.view.tv_item_rating2.text = "Your rating:"
        holder.view.tv_rating_all.text = "Ratings: ${rating} from users"
        holder.view.tv_view_user.text = "Views:"
        holder.view.tv_item_rating.text = film.detail
        holder.view.tv_sinopsis.text = sinopsis

    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

}