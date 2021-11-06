package com.example.pindergarten_android


import android.net.Uri
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class postCommentAdapter(private val userImg:ArrayList<Uri>, private val userId:ArrayList<String>, private val userDetail:ArrayList<String>,private val userDate:ArrayList<String>, val context: Fragment_postcomment): RecyclerView.Adapter<postCommentAdapter.ViewHolder>(){


    override fun getItemCount(): Int = userImg.size

    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }
    interface ItemLongClickListener {
        fun onClick(view: View, position: Int)
    }

    private lateinit var itemClickListner: ItemClickListener
    private lateinit var itemLongClickListner: AdapterView.OnItemLongClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }

    fun setItemLongClickListener(itemClickListener: ItemLongClickListener) {
        this.itemLongClickListner = itemLongClickListner
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType: Int): ViewHolder {
        val inflateView = LayoutInflater.from(parent.context).inflate(R.layout.comment_item,parent,false)
        return ViewHolder(inflateView)
    }

    class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        var user_img = v.findViewById<ImageButton>(R.id.user_image)
        var user_comment = v.findViewById<TextView>(R.id.user_comment)
        var user_date = v.findViewById<TextView>(R.id.user_date)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = userImg[position]

        Glide.with(context)
            .load(item)
            .centerCrop()
            .circleCrop()
            .into(holder.user_img)

        holder.user_date.text = userDate[position]
        holder.user_comment.text = Html.fromHtml("<b>${userId[position]}</b> ${userDetail[position]}")


    }

}