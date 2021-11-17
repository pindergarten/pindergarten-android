package com.example.pindergarten_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class pindergartenBlogAdapter(private val blogTitle:ArrayList<String>,private val blogDescription:ArrayList<String>,private val blogPostdate:ArrayList<String>,val context: Fragment_detailPindergarten): RecyclerView.Adapter<pindergartenBlogAdapter.ViewHolder>(){

    override fun getItemCount(): Int = blogTitle.size

    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType: Int): ViewHolder {
        val inflateView = LayoutInflater.from(parent.context).inflate(R.layout.blog_review_item,parent,false)
        return ViewHolder(inflateView)
    }

    class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        private var view : View = v
        var review_title = v.findViewById<TextView>(R.id.review_title)
        var review_text = v.findViewById<TextView>(R.id.review_text)
        var review_date = v.findViewById<TextView>(R.id.review_date)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.review_title.text = blogTitle[position]
        holder.review_text.text = blogDescription[position]
        holder.review_date.text = blogPostdate[position]

        holder.itemView.setOnClickListener{
            itemClickListener.onClick(it,position)
        }


    }

    private fun changeDP(value: Int): Int {
        var displayMetrics = context.resources.displayMetrics
        return Math.round(value * displayMetrics.density)
    }

}