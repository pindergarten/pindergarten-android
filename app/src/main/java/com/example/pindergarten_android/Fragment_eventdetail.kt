package com.example.pindergarten_android

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Fragment_eventdetail : Fragment() {

    private var myContext: FragmentActivity? = null


    var eventId : Int = 0
    var liked : Boolean ?= null

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_event_detail,container,false)

        //navigate hide
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavigation(true)


        //fragment 데이터 전달 받는
        var bundle: Bundle
        if(arguments!=null){
            bundle = arguments as Bundle
            eventId = bundle.getInt("eventId")
        }

        var event_title = view.findViewById<TextView>(R.id.eventTitle)
        var event_Image = view.findViewById<ImageView>(R.id.eventImage)
        var day = view.findViewById<TextView>(R.id.day)
        var ReviewCount = view.findViewById<TextView>(R.id.ReviewCount)
        var likeCount = view.findViewById<TextView>(R.id.likeCount)

        //수정필요
        liked = false

        val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }
        Log.i("jwt : ",sharedPreferences.toString())

        //서버: 이벤트 세부확인
        apiService.eventDetailAPI(eventId,sharedPreferences.toString())?.enqueue(object :
            Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                Log.i("event Detail: ","success")
                context?.let {
                    Glide.with(it)
                        .load(Uri.parse(response.body()?.eventList?.thumbnail))
                        .override(500,500)
                        .into(event_Image)
                }
                event_title.text = response.body()?.eventList?.title.toString()
                ReviewCount.text= response.body()?.eventList?.commentCount.toString()
                likeCount.text= response.body()?.eventList?.likeCount.toString()
                //계산 필요
                day.text="D-${0}"

            }

            override fun onFailure(call: Call<Post?>, t: Throwable) {
                Log.i("post Detail: ","fail")
            }

        })


        var button =view.findViewById<Button>(R.id.button)
        button.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_comment()
            val bundle = Bundle()
            bundle.putInt("eventId", eventId)
            fragment.arguments=bundle
            transaction.replace(R.id.container,fragment)
            transaction.commit()
        }

        var ReviewId = view.findViewById<ImageButton>(R.id.ReviewId)
        var likeId = view.findViewById<ImageButton>(R.id.likeId)
        var editText = view.findViewById<TextView>(R.id.editText)

        editText.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_comment()
            val bundle = Bundle()
            bundle.putInt("eventId", eventId)
            fragment.arguments=bundle
            transaction.replace(R.id.container,fragment)
            transaction.commit()
        }

        ReviewId.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_comment()
            val bundle = Bundle()
            bundle.putInt("eventId", eventId)
            fragment.arguments=bundle
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
            //좋아요 변경 API
            apiService.eventLikeAPI(eventId,sharedPreferences.toString())?.enqueue(object : Callback<Post?> {
                override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                    Log.i("eventId Liked: ","수정 성공")
                }
                override fun onFailure(call: Call<Post?>, t: Throwable) {
                    Log.i("eventId Detail: ","fail")
                }

            })
        }

        var backBtn = view.findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_event()
            val bundle = Bundle()
            bundle.putInt("eventId", eventId)
            fragment.arguments=bundle
            transaction.replace(R.id.container,fragment)
            transaction.commit()
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