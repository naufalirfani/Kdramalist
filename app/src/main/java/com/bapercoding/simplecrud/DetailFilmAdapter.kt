@file:Suppress("DEPRECATION")

package com.bapercoding.simplecrud

import android.R.attr.name
import android.R.attr.overScrollFooter
import android.app.Activity
import android.app.Dialog
import android.app.Fragment
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.firebase.database.*
import com.iarcuschin.simpleratingbar.SimpleRatingBar
import kotlinx.android.synthetic.main.activity_detail_film.*
import kotlinx.android.synthetic.main.detail_film.view.*
import java.text.DecimalFormat


@Suppress("DEPRECATION")
class DetailFilmAdapter(private val context: Context, private val listFilm: ArrayList<Film>, private val judul: String, private val rating: String, private val episode: String, private val sinopsis: String, private val imagePage: String, private val letak: Int, private val list2: ArrayList<String>, private val id: String, private val activity: FragmentActivity, private val loading: ProgressDialog) : RecyclerView.Adapter<DetailFilmAdapter.Holder>() {

    private lateinit var dbReference: DatabaseReference
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.detail_film,parent,false))
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val film = listFilm[letak]
        if(list2.isEmpty()){
            activity.finish()
            activity.startActivity(activity.intent)
        }
        else{
            loading.dismiss()
            holder.view.tv_jumlah_photo.text = "View all (${list2.size})"
            holder.view.tv_jumlah_photo.setOnClickListener {
                activity.pager.setCurrentItem(3)
            }
            holder.view.rvPhoto2.setHasFixedSize(true)
            holder.view.rvPhoto2.layoutManager = GridLayoutManager(context, 3)
            val adapter = context?.let { PhotoFilmAdapter3(it,list2, judul) }
            adapter?.notifyItemRangeRemoved(0, list2.size)
            adapter?.notifyDataSetChanged()
            holder.view.rvPhoto2.adapter = adapter
        }
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
                val ratingBar = rankDialog.findViewById(R.id.dialog_ratingbar) as SimpleRatingBar
                ratingBar.setRating(rating)
                ratingBar.setOnRatingBarChangeListener(object : SimpleRatingBar.OnRatingBarChangeListener {
                    override fun onRatingChanged(simpleRatingBar: SimpleRatingBar?, rating: Float, fromUser: Boolean) {
                        ratingBar.setRating(rating)
                    }
                })

                val text = rankDialog.findViewById(R.id.rank_dialog_text2) as TextView
                text.text = df.format(rating*2) + "/10"

                val btnCancel: Button = rankDialog.findViewById(R.id.rank_dialog_button2)
                btnCancel.setOnClickListener {
                    rankDialog.dismiss()
                    activity.finish()
                    activity.startActivity(activity.intent)
                }
                val btnSubmit: Button = rankDialog.findViewById(R.id.rank_dialog_button)
                btnSubmit.setOnClickListener {
                    dbReference = FirebaseDatabase.getInstance().getReference("userRaring")
                    val info = Upload(judul, rating.toString())
                    val loading = ProgressDialog(context)
                    loading.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    loading.setIndeterminate(true)
                    loading.setCancelable(true)
                    loading.show()
                    loading.setContentView(R.layout.progressdialog)
                    val handler = Handler()
                    handler.postDelayed(Runnable { // Do something after 5s = 5000ms
                        loading.dismiss()
                        dbReference.child(id).child(judul).setValue(info)
                        rankDialog.dismiss()
                    }, 3000)
                }
                rankDialog.show()
            }
        })
        Glide.with(holder.itemView.context)
                .load(R.drawable.comingsoon)
//                .apply(RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL))
                .into(holder.view.img_cast)
        holder.view.tv_jumla_cast.setOnClickListener {
            activity.pager.setCurrentItem(2)
        }
        holder.view.tv_rating_user.text = "0/10"
        holder.view.tv_rating.text = rating
        holder.view.tv_item_rating2.text = "Your rating"
        holder.view.tv_rating_all.text = Html.fromHtml("Ratings: " + "<b>" + rating + "</b>" + " from users")
        holder.view.tv_view_user.text = "Views:"
        holder.view.tv_item_rating.text = film.detail
        holder.view.tv_sinopsis.text = sinopsis
        holder.view.tv_photo.text = "Photos"
        holder.view.tv_cast.text = "Cast"
        holder.view.tv_jumla_cast.text = "View all (0)"

    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)
}