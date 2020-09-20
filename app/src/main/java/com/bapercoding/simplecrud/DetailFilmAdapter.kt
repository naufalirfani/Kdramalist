package com.bapercoding.simplecrud

import android.R.attr.name
import android.app.Dialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.iarcuschin.simpleratingbar.SimpleRatingBar
import kotlinx.android.synthetic.main.detail_film.view.*
import java.text.DecimalFormat


@Suppress("DEPRECATION")
class DetailFilmAdapter(private val context: Context, private val listFilm: ArrayList<Film>, private val judul: String, private val rating: String, private val episode: String, private val sinopsis: String, private val imagePage: String, private val letak: Int, private val list2: ArrayList<String>) : RecyclerView.Adapter<DetailFilmAdapter.Holder>() {

    val listPhoto2: ArrayList<String> = arrayListOf()
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

        holder.view.ratingBar1.setOnRatingBarChangeListener(object : SimpleRatingBar.OnRatingBarChangeListener {
            override fun onRatingChanged(simpleRatingBar: SimpleRatingBar?, rating: Float, fromUser: Boolean) {
                val df = DecimalFormat("#.#")
                holder.view.tv_rating_user.text = df.format(rating*2) + "/10"

                val rankDialog = Dialog(context)
                rankDialog.setContentView(R.layout.rating_dialog)
                rankDialog.setCancelable(true)
                val ratingBar = rankDialog.findViewById(R.id.dialog_ratingbar) as RatingBar
                ratingBar.setRating(rating)

                val text = rankDialog.findViewById(R.id.rank_dialog_text2) as TextView
                text.text = df.format(rating*2) + "/10"
                rankDialog.show()
            }
        })
        holder.view.tv_rating_user.text = "0/10"
        holder.view.tv_rating.text = rating
        holder.view.tv_item_rating2.text = "Your rating:"
        holder.view.tv_rating_all.text = Html.fromHtml("Ratings: " + "<b>" + rating + "</b>" + " from users")
        holder.view.tv_view_user.text = "Views:"
        holder.view.tv_item_rating.text = film.detail
        holder.view.tv_sinopsis.text = sinopsis
    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)
}