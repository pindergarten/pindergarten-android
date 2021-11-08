package com.example.pindergarten_android

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Fragment_eventdetail : Fragment() {

    private var myContext: FragmentActivity? = null


    var eventId : Int = 0
    var liked = -1

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    var userImg = ArrayList<Uri>()
    var userId = ArrayList<Int>()
    var nickName = ArrayList<String>()
    var userDetail = ArrayList<String>()
    var userDate = ArrayList<String>()
    var commentId = ArrayList<Int>()
    var eventImg = ArrayList<Uri>()
    val adapter = CommentAdapter(userImg,nickName,userDetail,userDate,this)



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_event_detail,container,false)

        //navigate hide
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavigation(true)

        var recyclerview_main = view.findViewById<RecyclerView>(R.id.recyclerView)
        var recyclerView = recyclerview_main // recyclerview id
        var layoutManager = LinearLayoutManager(container?.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter


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
        var likeId = view.findViewById<ImageButton>(R.id.likeId)


        val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }
        Log.i("jwt : ",sharedPreferences.toString())

        //서버: 이벤트 세부확인
        apiService.eventDetailAPI(eventId,sharedPreferences.toString())?.enqueue(object :
            Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                Log.i("event Detail","success")

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


                var tempLiked = response.body()?.eventList?.isLiked
                if(tempLiked ==0){
                    //좋아요 x
                    Log.i("event Liked: ","좋아요 x")
                    likeId.setImageResource(R.drawable.unliked)
                    liked = 0
                }
                else{
                    //좋아요
                    Log.i("event Liked: ","좋아요")
                    likeId.setImageResource(R.drawable.liked)
                    liked = 1
                }

            }

            override fun onFailure(call: Call<Post?>, t: Throwable) {
                Log.i("event Detail","fail")
                Log.i("event Detail",t.toString())
            }

        })

        //서버: 게시글 댓글 확인
        apiService.eventCommentAPI(eventId, sharedPreferences.toString())?.enqueue(object :
            Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                Log.i("event Comment: ", "success")

                userImg.clear()
                userId.clear()
                nickName.clear()
                userDetail.clear()
                userDate.clear()
                commentId.clear()

                for( i in 0 until response.body()?.commentList!!.size){

                    Log.i("${i}번째 date: ",response.body()?.commentList!![i].date.toString())
                    Log.i("${i}번째 userImage: ",response.body()?.commentList!![i].user_image.toString())
                    Log.i("${i}번째 nickName: ",response.body()?.commentList!![i].nickname.toString())
                    Log.i("${i}번째 comment: ",response.body()?.commentList!![i].content.toString())
                    Log.i("${i}번째 commentId: ",response.body()?.commentList!![i].id.toString())


                    userImg.add(Uri.parse(response.body()?.commentList!![i].user_image.toString()))
                    nickName.add(response.body()?.commentList!![i].nickname.toString())
                    userDetail.add(response.body()?.commentList!![i].content.toString())
                    userDate.add(response.body()?.commentList!![i].date.toString())
                    commentId.add(response.body()?.commentList!![i].id!!.toInt())
                    userId.add(response.body()?.commentList!![i].userId!!.toInt())
                }

                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<Post?>, t: Throwable) {
                Log.i("event Comment: ", "실패")
            }

        })



        var button =view.findViewById<ImageButton>(R.id.button)
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

        ReviewId.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_comment()
            val bundle = Bundle()
            bundle.putInt("eventId", eventId)
            fragment.arguments=bundle
            transaction.replace(R.id.container,fragment)
            transaction.commit()
        }

        likeId?.setOnClickListener{
            //게시물 좋아요
            if(liked==0){
                Log.i("event Liked: ","좋아요")
                likeId.setImageResource(R.drawable.liked)
                liked = 1
                likeCount.text = "${ Integer.parseInt(likeCount.text.toString()) + 1 }"
            }
            else{
                Log.i("event Liked: ","좋아요 취소")
                likeId.setImageResource(R.drawable.unliked)
                liked = 0
                likeCount.text = "${ Integer.parseInt(likeCount.text.toString()) - 1 }"
            }

            var temp: HashMap<String, String> = HashMap()

            //좋아요 변경 API
            apiService.eventLikeAPI(eventId,sharedPreferences.toString(),temp)?.enqueue(object : Callback<Post?> {
                override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                    Log.i("event Liked: ","수정 성공")
                }
                override fun onFailure(call: Call<Post?>, t: Throwable) {
                    Log.i("event Detail: ","fail")
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