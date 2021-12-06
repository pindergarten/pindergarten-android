package com.pindergarten_android.pindergarten_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class searchPindergartenAdapter(private val pindergartenName:ArrayList<String>,private val pindergartenAddress:ArrayList<String>,val context: Fragment_searchPindergarten): RecyclerView.Adapter<searchPindergartenAdapter.ViewHolder>(){

    override fun getItemCount(): Int = pindergartenName.size

    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }


    override fun onCreateViewHolder(parent : ViewGroup, viewType: Int): ViewHolder {
        val inflateView = LayoutInflater.from(parent.context).inflate(R.layout.pindergarten_search_item,parent,false)
        return ViewHolder(inflateView)
    }

    class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        private var view : View = v
        var pindergarten_Name = v.findViewById<TextView>(R.id.pindergartenName)
        var pindergarten_Address = v.findViewById<TextView>(R.id.pindergartenAddress)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.pindergarten_Name.text = pindergartenName[position]
        holder.pindergarten_Address.text = pindergartenAddress[position]

        holder.itemView.setOnClickListener{
            itemClickListener.onClick(it,position)
        }

    }

    private fun changeDP(value: Int): Int {
        var displayMetrics = context.resources.displayMetrics
        return Math.round(value * displayMetrics.density)
    }

}