package com.example.pindergarten_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(): RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    var titles = arrayOf("one", "two", "three", "four", "five")
    var details = arrayOf("Item one", "Item two", "Item three", "Item four", "Itme five")

    var images = intArrayOf(R.drawable.test1,
        R.drawable.test2,
        R.drawable.test3,
        R.drawable.test4,
        )


    class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var itemimage1: ImageView = itemview.findViewById(R.id.item_image1)
        var itemimage2: ImageView = itemview.findViewById(R.id.item_image2)
        var itemimage3: ImageView = itemview.findViewById(R.id.item_image3)
        var itemimage4: ImageView = itemview.findViewById(R.id.item_image4)
        //public var itemtitle: TextView = itemview.findViewById(R.id.item_title)
        //public var itemdetail: TextView = itemview.findViewById(R.id.item_detail)
    }

    override fun onCreateViewHolder(viewgroup: ViewGroup, position: Int): MyViewHolder {
        var v: View = LayoutInflater.from(viewgroup.context).inflate(R.layout.card_layout, viewgroup, false)

        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //holder.itemtitle.setText(titles.get(position))
        holder.itemimage1.setImageResource(images.get(0))
        holder.itemimage2.setImageResource(images.get(1))
        holder.itemimage3.setImageResource(images.get(2))
        holder.itemimage4.setImageResource(images.get(3))
        //holder.itemdetail.setText(details.get(position))
    }

    override fun getItemCount(): Int {
        return images.size
    }

}