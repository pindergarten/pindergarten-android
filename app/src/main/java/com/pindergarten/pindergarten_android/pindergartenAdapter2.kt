package com.pindergarten.pindergarten_android

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class pindergartenAdapter2(private val pindergartenThumbnail:ArrayList<Uri>,private val pindergartenName:ArrayList<String>,private val pindergartenAddress:ArrayList<String>,private val pindergartenRating:ArrayList<Double>,private val pindergartenIsLiked:ArrayList<Int>,private val pindergartenDistance:ArrayList<String>,val context: Fragment_likePindergarten): RecyclerView.Adapter<pindergartenAdapter2.ViewHolder>(){

    override fun getItemCount(): Int = pindergartenThumbnail.size

    interface ItemClickListener{
        fun onClick(view: View, position: Int)
    }

    private lateinit var itemClickListener: ItemClickListener
    private lateinit var likedClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
    fun setLikedClickListener(likedClickListener: ItemClickListener){
        this.likedClickListener = likedClickListener
    }


    override fun onCreateViewHolder(parent : ViewGroup, viewType: Int): ViewHolder {
        val inflateView = LayoutInflater.from(parent.context).inflate(R.layout.pindergarten_panel_item,parent,false)
        return ViewHolder(inflateView)
    }

    class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        private var view : View = v
        var pindergarten_Img = v.findViewById<ImageView>(R.id.pindergartenImg)
        var pindergarten_Name = v.findViewById<TextView>(R.id.pindergartenName)
        var pindergarten_Address = v.findViewById<TextView>(R.id.pindergartenAddress)
        var pindergarten_RatingBar = v.findViewById<RatingBar>(R.id.pindergartenRating)
        var rating_Text = v.findViewById<TextView>(R.id.rating_text)
        var pindergarten_Distance = v.findViewById<TextView>(R.id.pindergartenDistance)
        var pindergarten_IsLiked = v.findViewById<ImageButton>(R.id.pindergartenLike)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val pindergarten_thumbnail = pindergartenThumbnail[position]

        Glide.with(context)
            .load(pindergarten_thumbnail)
            .centerCrop()
            .into(holder.pindergarten_Img)

        holder.pindergarten_Name.text = pindergartenName[position]
        holder.pindergarten_Address.text = pindergartenAddress[position]
        holder.pindergarten_RatingBar.rating = pindergartenRating[position].toFloat()
        holder.rating_Text.text = "${pindergartenRating[position].toFloat()}/5"
        holder.pindergarten_Distance.text = pindergartenDistance[position]

        when(pindergartenIsLiked[position]){
            0->{
                holder.pindergarten_IsLiked.setImageResource(R.drawable.post_unliked)
            }
            1->{
                holder.pindergarten_IsLiked.setImageResource(R.drawable.post_liked)
            }
        }


        holder.itemView.setOnClickListener{
            itemClickListener.onClick(it,position)
        }

        holder.pindergarten_IsLiked.setOnClickListener{
            likedClickListener.onClick(it,position)
        }


    }

    private fun changeDP(value: Int): Int {
        var displayMetrics = context.resources.displayMetrics
        return Math.round(value * displayMetrics.density)
    }

}