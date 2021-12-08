package com.android.pindergarten_android

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class EventAdapter(private val eventImage:ArrayList<Uri>,private val eventTitle:ArrayList<String> ,private val eventDay:ArrayList<Int> , val context: Fragment_event): RecyclerView.Adapter<EventAdapter.ViewHolder>(){

    override fun getItemCount(): Int = eventImage.size

    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    private lateinit var itemClickListner: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType: Int): ViewHolder {
        val inflateView = LayoutInflater.from(parent.context).inflate(R.layout.event_image_item,parent,false)
        return ViewHolder(inflateView)
    }

    class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        private var view : View = v
        var image = v.findViewById<ImageView>(R.id.eventImage)
        var title = v.findViewById<TextView>(R.id.eventTitle)
        var day = v.findViewById<TextView>(R.id.day)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Log.i("eventAdapter",eventImage[position].toString())

        Glide.with(context)
            .load(eventImage[position].toString())
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .fitCenter()
            .into(holder.image)


        holder.title.text = eventTitle[position]
        holder.day.text="D-${eventDay[position]}"

        holder.itemView.setOnClickListener{
            itemClickListner.onClick(it,position)
        }

    }

}



