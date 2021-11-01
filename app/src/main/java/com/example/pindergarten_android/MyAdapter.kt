package com.example.pindergarten_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class MyAdapter(): RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    var images = intArrayOf(R.drawable.test1, R.drawable.test2, R.drawable.test3, R.drawable.test4 )
    var users_id = arrayOf("one", "two", "three", "four")
    var users_details = arrayOf("Item one", "Item two", "Item three", "Item four")
    var users_img = intArrayOf(R.drawable.test1, R.drawable.test2, R.drawable.test3, R.drawable.test4 )


    class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var itemimage1: ImageView = itemview.findViewById(R.id.item_image1)
        var itemimage2: ImageView = itemview.findViewById(R.id.item_image2)
        var itemimage3: ImageView = itemview.findViewById(R.id.item_image3)
        var itemimage4: ImageView = itemview.findViewById(R.id.item_image4)

        var user1_id: TextView = itemview.findViewById(R.id.user1_id)
        var user2_id: TextView = itemview.findViewById(R.id.user2_id)
        var user3_id: TextView = itemview.findViewById(R.id.user3_id)
        var user4_id: TextView = itemview.findViewById(R.id.user4_id)

        var user1_text: TextView = itemview.findViewById(R.id.user1_text)
        var user2_text: TextView = itemview.findViewById(R.id.user2_text)
        var user3_text: TextView = itemview.findViewById(R.id.user3_text)
        var user4_text: TextView = itemview.findViewById(R.id.user4_text)

        var user1_img: ImageView = itemview.findViewById(R.id.user1_img)
        var user2_img: ImageView = itemview.findViewById(R.id.user2_img)
        var user3_img: ImageView = itemview.findViewById(R.id.user3_img)
        var user4_img: ImageView = itemview.findViewById(R.id.user4_img)
    }

    override fun onCreateViewHolder(viewgroup: ViewGroup, position: Int): MyViewHolder {
        var v: View = LayoutInflater.from(viewgroup.context).inflate(R.layout.card_layout, viewgroup, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemimage1.setImageResource(images[0])
        holder.itemimage2.setImageResource(images[1])
        holder.itemimage3.setImageResource(images[2])
        holder.itemimage4.setImageResource(images[3])

        holder.user1_id.text = users_id[0]
        holder.user2_id.text = users_id[1]
        holder.user3_id.text = users_id[2]
        holder.user4_id.text = users_id[3]

        holder.user1_text.text = users_details[0]
        holder.user2_text.text = users_details[1]
        holder.user3_text.text = users_details[2]
        holder.user4_text.text = users_details[3]


        Glide.with(holder.itemView)
            .load(users_img[0])
            .centerCrop()
            .circleCrop()
            .into(holder.user1_img)

        Glide.with(holder.itemView)
            .load(users_img[1])
            .centerCrop()
            .circleCrop()
            .into(holder.user2_img)

        Glide.with(holder.itemView)
            .load(users_img[2])
            .centerCrop()
            .circleCrop()
            .into(holder.user3_img)

        Glide.with(holder.itemView)
            .load(users_img[3])
            .centerCrop()
            .circleCrop()
            .into(holder.user4_img)


    }

    override fun getItemCount(): Int {
        return images.size
    }

}