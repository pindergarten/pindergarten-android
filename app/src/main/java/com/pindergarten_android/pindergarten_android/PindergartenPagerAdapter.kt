package com.pindergarten_android.pindergarten_android

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide


class PindergartenPagerAdapter(Image : ArrayList<String>, private val context : Context) : PagerAdapter() {
    private var layoutInflater : LayoutInflater ?= null

    var Image : ArrayList<String> = Image

    override fun getCount(): Int {
        return Image.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as View
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        //destroyItem(container, position, `object`)
        (container as ViewPager).removeView(`object` as View)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = layoutInflater!!.inflate(R.layout.viewpager_pindergarten_layout,null)
        val image = v.findViewById<ImageView>(R.id.post_image)
        val textIndicator = v.findViewById<TextView>(R.id.textIndicator)
        image.clipToOutline = true

        textIndicator.text = "${position+1} / ${Image.size}"


        Glide.with(context)
            .load(Image[position])
            .centerCrop()
            .into(image)


        val vp = container as ViewPager
        vp.addView(v,0)
        return v
    }
}