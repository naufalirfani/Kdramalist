package com.bapercoding.simplecrud

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
        getPhotos()
        val film = listFilm[letak]
        Glide.with(holder.itemView.context)
                .load(imagePage)
//                .apply(RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL))
                .into(holder.view.img_item_photo2)

        holder.view.ratingBar1.setOnRatingBarChangeListener(object : SimpleRatingBar.OnRatingBarChangeListener {
            override fun onRatingChanged(simpleRatingBar: SimpleRatingBar?, rating: Float, fromUser: Boolean) {
                val df = DecimalFormat("#.#")
                holder.view.tv_rating_user.text = df.format(rating*2) + "/10"
            }
        })
        holder.view.tv_rating_user.text = listPhoto2.size.toString()
        holder.view.tv_rating.text = rating
        holder.view.tv_item_rating2.text = "Your rating:"
        holder.view.tv_rating_all.text = Html.fromHtml("Ratings: " + "<b>" + rating + "</b>" + " from users")
        holder.view.tv_view_user.text = "Views:"
        holder.view.tv_item_rating.text = film.detail
        holder.view.tv_sinopsis.text = sinopsis
    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

    fun getPhotos(){
        val dbReference2 = FirebaseDatabase.getInstance().getReference("images")
        val postListener2 = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(listPhoto2.isNotEmpty()){
                    listPhoto2.clear()
                }
                for (data: DataSnapshot in dataSnapshot.children){
                    val hasil = data.getValue(Upload::class.java)
                    listPhoto2.add(hasil?.url!!)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        var judul2 = judul
        if(judul2.contains(".")){
            judul2 = judul2.replace(".", "")
            dbReference2.child(judul2).addValueEventListener(postListener2)
        }
        else{
            dbReference2.child(judul2).addValueEventListener(postListener2)
        }
    }

}