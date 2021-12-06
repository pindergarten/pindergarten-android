package com.pindergarten_android.pindergarten_android

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter

class OnboardingAdapter(var context: Context) : PagerAdapter(){

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var inflater = LayoutInflater.from(context)
        var view : View = inflater.inflate(R.layout.onboarding_item,container,false);
        var imageView = view.findViewById<ImageView>(R.id.imageView)
        var goBtn = view.findViewById<ImageView>(R.id.startbtn)
        var starttext = view.findViewById<TextView>(R.id.starttext)

        goBtn.setOnClickListener {

            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)

        }

        if(position==0){
            imageView.setImageResource(R.drawable.onboarding1);
            goBtn.visibility = View.INVISIBLE
            starttext.visibility = View.INVISIBLE
        }
        else if(position==1){
            imageView.setImageResource(R.drawable.onboarding2);
            goBtn.visibility = View.INVISIBLE
            starttext.visibility = View.INVISIBLE
        }
        else if(position==2){
            imageView.setImageResource(R.drawable.onboarding3);
            goBtn.visibility = View.VISIBLE
            starttext.visibility = View.VISIBLE
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