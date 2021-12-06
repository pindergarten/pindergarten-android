package com.pindergarten_android.pindergarten_android

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MultiImageAdapter(private val items:ArrayList<Uri>, val context: Fragment_addPost): RecyclerView.Adapter<MultiImageAdapter.ViewHolder>(){

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MultiImageAdapter.ViewHolder, position: Int) {
        val item = items[position]
        Glide.with(context)
            .load(item)
            .override(500,500)
            .centerCrop()
            .into(holder.image)
    }

    override fun onCreateViewHolder(parent :ViewGroup, viewType: Int): ViewHolder {
        val inflateView = LayoutInflater.from(parent.context).inflate(R.layout.gallery_image_item,parent,false)
        return ViewHolder(inflateView)
    }

    class ViewHolder(v: View):RecyclerView.ViewHolder(v){
        private var view : View = v
        var image = v.findViewById<ImageView>(R.id.photo)

        fun bind(listener: View.OnClickListener,item:String){
            view.setOnClickListener(listener)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }



}
