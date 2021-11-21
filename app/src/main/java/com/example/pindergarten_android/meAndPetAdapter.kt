package com.example.pindergarten_android

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class meAndPetAdapter(private val postImage:ArrayList<Uri>,val context: Fragment_meAndPet): RecyclerView.Adapter<meAndPetAdapter.ViewHolder>(){

    override fun getItemCount(): Int = postImage.size

    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType: Int): ViewHolder {
        val inflateView = LayoutInflater.from(parent.context).inflate(R.layout.meandpet_post_item,parent,false)
        return ViewHolder(inflateView)
    }

    class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        private var view : View = v
        var post_Img = v.findViewById<ImageView>(R.id.postImg)
        var constraintLayout = v.findViewById<ConstraintLayout>(R.id.constraintLayout)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item_postImage = postImage[position]

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


        holder.post_Img.layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT

        //간격유지
        /*
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

         */


        holder.itemView.setOnClickListener{
            itemClickListener.onClick(it,position)
        }

    }

    private fun changeDP(value: Int): Int {
        var displayMetrics = context.resources.displayMetrics
        return Math.round(value * displayMetrics.density)
    }

}