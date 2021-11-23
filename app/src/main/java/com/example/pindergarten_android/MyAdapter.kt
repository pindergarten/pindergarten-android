package com.example.pindergarten_android

import android.net.Uri
import android.util.Log
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

    private lateinit var itemClickListener: ItemClickListener
    private lateinit var likedClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
    fun setLikedClickListener(likedClickListener: ItemClickListener){
        this.likedClickListener = likedClickListener
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

        Log.i("item Count",itemCount.toString())
        if(position==(itemCount-1)){
            Log.i("last item",position.toString())
            var param :ConstraintLayout.LayoutParams = ConstraintLayout.LayoutParams(changeDP(180),ConstraintLayout.LayoutParams.WRAP_CONTENT)
            param.setMargins(changeDP(10),changeDP(10),changeDP(10),250)
            holder.constraintLayout.layoutParams = param
        }


        Glide.with(context)
            .load(item_postImage)
            .fitCenter()
            .into(holder.post_Img)

        Glide.with(context)
            .load(item_userImg)
            .centerCrop()
            .circleCrop()
            .into(holder.user_Img)

        holder.post_Text.text = postText[position]
        holder.user_Id.text=userId[position]

        //holder.post_Img.layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT

        //간격유지

        when(position%4){

            0-> {
                holder.post_Img.layoutParams.height  = changeDP(153)
            }
            1-> {
                holder.post_Img.layoutParams.height  = changeDP(193)
            }
            2-> {
                holder.post_Img.layoutParams.height  = changeDP(193)
            }
            3-> {
                holder.post_Img.layoutParams.height  = changeDP(153)
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

        holder.itemView.setOnClickListener{
            itemClickListener.onClick(it,position)
        }

        holder.post_Liked.setOnClickListener{
            likedClickListener.onClick(it,position)
        }



    }

    private fun changeDP(value: Int): Int {
        var displayMetrics = context.resources.displayMetrics
        return Math.round(value * displayMetrics.density)
    }

}