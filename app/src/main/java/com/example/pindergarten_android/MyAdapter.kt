package com.example.pindergarten_android

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class MyAdapter(private val postImage:ArrayList<Uri>, private val postText:ArrayList<String>, private val userImage:ArrayList<Uri>,private val userId:ArrayList<String>,private val postLiked:ArrayList<Int>, val context: Fragment_socialPet): RecyclerView.Adapter<MyAdapter.ViewHolder>(){

    override fun getItemCount(): Int = postImage.size

    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    private lateinit var itemClickListner: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType: Int): ViewHolder {
        val inflateView = LayoutInflater.from(parent.context).inflate(R.layout.socialpet_post_item,parent,false)
        return ViewHolder(inflateView)
    }

    class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        private var view : View = v
        var post_Img = v.findViewById<ImageView>(R.id.postImg)
        var post_Text = v.findViewById<TextView>(R.id.postText)
        var user_Img = v.findViewById<ImageView>(R.id.userImg)
        var user_Id = v.findViewById<TextView>(R.id.userId)
        var constraintLayout = v.findViewById<ConstraintLayout>(R.id.constraintLayout)
        var post_Liked = v.findViewById<ImageView>(R.id.postLike)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item_postImage = postImage[position]
        val item_userImg = userImage[position]


        Glide.with(context)
            .load(item_postImage)
            .centerCrop()
            .into(holder.post_Img)

        Glide.with(context)
            .load(item_userImg)
            .centerCrop()
            .circleCrop()
            .into(holder.user_Img)

        holder.post_Text.text = postText[position]
        holder.user_Id.text=userId[position]

        when(position%4){
            0-> {
                holder.post_Img.layoutParams.height  = 550
                //holder.constraintLayout.maxHeight = 550
            }
            1-> {
                holder.post_Img.layoutParams.height  = 450
                //holder.constraintLayout.maxHeight = 450
            }
            2-> {
                holder.post_Img.layoutParams.height  = 450
                //holder.constraintLayout.maxHeight = 450
            }
            3-> {
                holder.post_Img.layoutParams.height  = 550
                //holder.constraintLayout.maxHeight = 550
            }
        }

        when(postLiked[position]){
            0->{
                holder.post_Liked.setImageResource(R.drawable.post_unliked)
            }
            1->{
                holder.post_Liked.setImageResource(R.drawable.post_liked)
            }
        }

        holder.constraintLayout.layoutParams.height= ConstraintLayout.LayoutParams.WRAP_CONTENT

        holder.itemView.setOnClickListener{
            itemClickListner.onClick(it,position)
        }

    }

}