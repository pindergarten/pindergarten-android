package com.android.pindergarten_android

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Fragment_eventdetail : Fragment() {

    private var myContext: FragmentActivity? = null


    var eventId : Int = 0
    var liked = -1

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site/")
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

    var dialog : AlertDialog ?=null

    private lateinit var callback: OnBackPressedCallback
    var mainAct : MainActivity ?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_event_detail,container,false)

        //navigate hide
        mainAct = activity as MainActivity
        mainAct!!.HideBottomNavigation(true)

        var recyclerview_main = view.findViewById<RecyclerView>(R.id.recyclerView)
        var recyclerView = recyclerview_main // recyclerview id

        val spaceDecoration = VerticalSpaceItemDecoration(0)
        recyclerView.addItemDecoration(spaceDecoration)

        var layoutManager = LinearLayoutManager(container?.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter


        //fragment ????????? ?????? ??????
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

        //??????: ????????? ????????????
        apiService.eventDetailAPI(eventId,sharedPreferences.toString())?.enqueue(object :
            Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                Log.i("event Detail","success")

                Glide.with(context!!)
                    .load(Uri.parse(response.body()?.eventList?.image.toString()))
                    .fitCenter()
                    .into(event_Image)

                event_title.text = response.body()?.eventList?.title.toString()
                ReviewCount.text= response.body()?.eventList?.commentCount.toString()
                likeCount.text= response.body()?.eventList?.likeCount.toString()

                //????????????
                val dateFormat = SimpleDateFormat("yyyyMMdd")
                var today  = Calendar.getInstance().apply{
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.time.time

                var eventday = dateFormat.parse(response.body()?.eventList?.expired_at.toString().replace(".","")).time
                day.text="D-${((eventday - today) / (24 * 60 * 60 * 1000))}"


                var tempLiked = response.body()?.eventList?.isLiked
                if(tempLiked ==0){
                    //????????? x
                    Log.i("event Liked: ","????????? x")
                    likeId.setImageResource(R.drawable.unliked2)
                    liked = 0
                }
                else{
                    //?????????
                    Log.i("event Liked: ","?????????")
                    likeId.setImageResource(R.drawable.liked2)
                    liked = 1
                }

            }

            override fun onFailure(call: Call<Post?>, t: Throwable) {
                Log.i("event Detail","fail")
                Log.i("event Detail",t.toString())
            }

        })

        //??????: ????????? ?????? ??????
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

                    Log.i("${i}?????? date: ",response.body()?.commentList!![i].date.toString())
                    Log.i("${i}?????? userImage: ",response.body()?.commentList!![i].user_image.toString())
                    Log.i("${i}?????? nickName: ",response.body()?.commentList!![i].nickname.toString())
                    Log.i("${i}?????? comment: ",response.body()?.commentList!![i].content.toString())
                    Log.i("${i}?????? commentId: ",response.body()?.commentList!![i].id.toString())


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
                Log.i("event Comment: ", "??????")
            }

        })

        //?????? ????????????
        adapter.setItemClickListener(object :CommentAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                var myId =  myContext?.let { PreferenceManager.getInt(it,"userId") }
                if(userId[position]==myId){
                    //???????????? ??????
                    Log.i("???????????? ??????","!!")
                    val builder = AlertDialog.Builder(myContext)
                    val view: View = LayoutInflater.from(myContext).inflate(R.layout.post_delete_comment, null)
                    val deleteBtn = view?.findViewById<ImageButton>(R.id.deleteBtn)
                    val cancelBtn = view?.findViewById<ImageButton>(R.id.cancelBtn)
                    dialog = builder.create()
                    dialog!!.setView(view)
                    dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    dialog!!.window!!.setGravity(Gravity.BOTTOM)
                    dialog!!.show()

                    cancelBtn?.setOnClickListener{
                        dialog!!.dismiss()
                    }
                    deleteBtn?.setOnClickListener{
                        //?????? ??????
                        apiService.deleteEventCommentAPI(sharedPreferences.toString(),eventId,commentId[position])?.enqueue(object :
                            Callback<Post?> {
                            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                                Log.i("delete event Comment: ", response.body()?.success.toString())

                                val transaction = myContext!!.supportFragmentManager.beginTransaction()
                                val fragment : Fragment = Fragment_comment()
                                val bundle = Bundle()
                                bundle.putInt("eventId", eventId)
                                fragment.arguments=bundle
                                transaction.replace(R.id.container,fragment)
                                transaction.addToBackStack(null)
                                transaction.commit()

                                dialog!!.dismiss()
                            }

                            override fun onFailure(call: Call<Post?>, t: Throwable) {
                                Log.i("delete event Comment: ", "??????")

                                val transaction = myContext!!.supportFragmentManager.beginTransaction()
                                val fragment : Fragment = Fragment_comment()
                                val bundle = Bundle()
                                bundle.putInt("eventId", eventId)
                                fragment.arguments=bundle
                                transaction.replace(R.id.container,fragment)
                                transaction.addToBackStack(null)
                                transaction.commit()

                                dialog!!.dismiss()
                            }

                        })

                    }

                }

            }})

        adapter.setUserClickListener(object :CommentAdapter.UserClickListener{
            override fun onClick(view: View, position: Int) {

                val transaction = myContext!!.supportFragmentManager.beginTransaction()
                val fragment : Fragment = Fragment_others()
                val bundle = Bundle()
                bundle.putInt("userId", userId[position])
                bundle.putString("userNickname",nickName[position])
                fragment.arguments=bundle
                transaction.replace(R.id.container,fragment)
                transaction.addToBackStack(null)
                transaction.commit()

            }})


        var button =view.findViewById<ImageButton>(R.id.button)
        button.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_comment()
            val bundle = Bundle()
            bundle.putInt("eventId", eventId)
            fragment.arguments=bundle
            transaction.replace(R.id.container,fragment)
            transaction.addToBackStack(null)
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
            transaction.addToBackStack(null)
            transaction.commit()
        }

        likeId?.setOnClickListener{
            //????????? ?????????
            if(liked==0){
                Log.i("event Liked: ","?????????")
                likeId.setImageResource(R.drawable.liked2)
                liked = 1
                likeCount.text = "${ Integer.parseInt(likeCount.text.toString()) + 1 }"
            }
            else{
                Log.i("event Liked: ","????????? ??????")
                likeId.setImageResource(R.drawable.unliked2)
                liked = 0
                likeCount.text = "${ Integer.parseInt(likeCount.text.toString()) - 1 }"
            }

            var temp: HashMap<String, String> = HashMap()

            //????????? ?????? API
            apiService.eventLikeAPI(eventId,sharedPreferences.toString(),temp)?.enqueue(object : Callback<Post?> {
                override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                    Log.i("event Liked: ","?????? ??????")
                }
                override fun onFailure(call: Call<Post?>, t: Throwable) {
                    Log.i("event Detail: ","fail")
                }

            })


        }


        var backBtn = view.findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener{

            mainAct!!.onBackPressed()

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

    inner class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int) :
        RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.bottom = verticalSpaceHeight
        }
    }

}