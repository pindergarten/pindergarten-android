package com.example.pindergarten_android

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter

class OnboardingAdapter(var context: Context) : PagerAdapter(){

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var inflater = LayoutInflater.from(context)
        var view : View = inflater.inflate(R.layout.onboarding_item,container,false);
        var imageView = view.findViewById<ImageView>(R.id.imageView)
        var goBtn = view.findViewById<ImageButton>(R.id.startbtn)

        goBtn.setOnClickListener {
            context.startActivity( Intent(context, LoginActivity::class.java))
        }

        if(position==0){
            imageView.setImageResource(R.drawable.onboarding1);
            goBtn.visibility = View.INVISIBLE
        }
        else if(position==1){
            imageView.setImageResource(R.drawable.onboarding2);
            goBtn.visibility = View.INVISIBLE
        }
        else if(position==2){
            imageView.setImageResource(R.drawable.onboarding3);
            goBtn.visibility = View.VISIBLE
        }

        container.addView(view)

        return view
    }



    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View?)
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return 3
    }
}