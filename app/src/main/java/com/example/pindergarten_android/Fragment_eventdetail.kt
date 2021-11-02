package com.example.pindergarten_android

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class Fragment_eventdetail : Fragment() {

    private var myContext: FragmentActivity? = null


    var userImg = ArrayList<Uri>()
    var userId = ArrayList<String>()
    var userDetail = ArrayList<String>()
    var userDate = ArrayList<String>()
    val adapter = CommentAdapter(userImg,userId,userDetail,userDate,this)

    var eventTitle : String =""
    var eventImage : String =""
    var eventDay : Int = 0
    var liked : Boolean ?= null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_event_detail,container,false)

        //navigate hide
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavigation(true)


        var recyclerview_main = view.findViewById<RecyclerView>(R.id.recyclerview_main)
        var recyclerView = recyclerview_main // recyclerview id
        var layoutManager = LinearLayoutManager(container?.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        //fragment 데이터 전달 받는
        var bundle: Bundle
        if(arguments!=null){
            bundle = arguments as Bundle

            eventTitle = bundle.getString("eventTitle").toString()
            eventImage = bundle.getString("eventImage").toString()
            eventDay = bundle.getInt("eventDay",0)

            var event_title = view.findViewById<TextView>(R.id.eventTitle)
            var event_Image = view.findViewById<ImageView>(R.id.eventImage)
            var day = view.findViewById<TextView>(R.id.day)

            event_title.text = eventTitle
            context?.let {
                Glide.with(it)
                    .load(eventImage)
                    .override(500,500)
                    .into(event_Image)
            }
            day.text="D-${eventDay}"

        }

        //수정필요
        liked = false

        //서버요청
        for(i in 0 until 10){
            val temp_img = Uri.parse("android.resource://com.example.pindergarten_android/drawable/test1")
            val temp_title = "재밌는 이벤트네요~"
            val temp_day = "2021-01-01"
            val temp_id = "지현"
            userImg.add(temp_img)
            userId.add(temp_id)
            userDetail.add(temp_title)
            userDate.add(temp_day)
        }
        adapter.notifyDataSetChanged()

        var button =view.findViewById<Button>(R.id.button)
        button.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_comment()
            transaction.replace(R.id.container,fragment)
            transaction.commit()
        }

        var ReviewId = view.findViewById<ImageButton>(R.id.ReviewId)
        var likeCount = view.findViewById<TextView>(R.id.likeCount)
        var likeId = view.findViewById<ImageButton>(R.id.likeId)

        ReviewId.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_comment()
            transaction.replace(R.id.container,fragment)
            transaction.commit()
        }
        likeId.setOnClickListener{
            if(liked==false){
                likeId.setImageResource(R.drawable.liked)
                liked = true
                likeCount.setText("${Integer.parseInt(likeCount.text.toString())+1}")
            }
            else{
                likeId.setImageResource(R.drawable.unliked)
                liked = false
                likeCount.setText("${Integer.parseInt(likeCount.text.toString())-1}")
            }
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onAttach(activity: Activity) {
        myContext = activity as FragmentActivity
        super.onAttach(activity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


    }


}