package com.example.pindergarten_android

import android.app.Activity
import android.app.AlertDialog
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
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Fragment_postdetail : Fragment() {

    private var myContext: FragmentActivity? = null

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    var likeTemp : Int = 0
    var dialog : AlertDialog ?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_postdetail,container,false)

        //navigate hide
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavigation(true)

        var postId : Int
        //fragment 데이터 전달 받는
        var bundle: Bundle
        if(arguments!=null){
            bundle = arguments as Bundle
            postId = bundle.getInt("postId")
            Log.i("bundle_postId",postId.toString())
        }
        else {
            postId  = -1
        }

        var userImage : ImageView = view.findViewById(R.id.user_image)
        var userId : TextView = view.findViewById(R.id.user_id)
        var noticeBtn : ImageButton = view.findViewById(R.id.noticeBtn)
        var postImage : ImageView = view.findViewById(R.id.post_image)
        var likeCount : TextView = view.findViewById(R.id.likeCount)
        var ReviewCount : TextView =view.findViewById(R.id.ReviewCount)
        var date : TextView = view.findViewById(R.id.date)
        var postText : TextView = view.findViewById(R.id.postText)
        var likeId : ImageButton = view.findViewById(R.id.likeId)
        var ReviewId :ImageButton = view.findViewById(R.id.ReviewId)

        likeId.setOnClickListener{
            if(likeTemp == 1){
                //좋아요
                likeId.setImageResource(R.drawable.unliked)
                likeCount.text = ("${Integer.parseInt(likeCount.text.toString())-1}")
                likeTemp = 0
            }
            else{
                //좋아요 아님
                likeId.setImageResource(R.drawable.liked)
                likeCount.text = ("${Integer.parseInt(likeCount.text.toString())+1}")
                likeTemp = 1
            }
            //서버 연결 (게시물 좋아요)
        }

        ReviewId.setOnClickListener{
            //게시물 댓글 보기
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_postcomment()
            val bundle = Bundle()
            bundle.putInt("postId", postId)
            fragment.arguments=bundle
            transaction.replace(R.id.container,fragment)
            transaction.commit()
        }

        noticeBtn.setOnClickListener{
            //신고하기 기능
            val builder = AlertDialog.Builder(myContext)
            val view: View = LayoutInflater.from(myContext).inflate(R.layout.post_declare, null)
            val declareBtn = dialog?.findViewById<ImageButton>(R.id.delcareBtn)
            val cancelBtn = dialog?.findViewById<ImageButton>(R.id.cancelBtn)
            dialog = builder.create()
            dialog!!.setView(view)
            dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)

            cancelBtn?.setOnClickListener{
                dialog!!.dismiss()
            }
            declareBtn?.setOnClickListener{
                Log.i("신고","!!")
            }

        }


        //서버: 게시글 세부확인
        apiService.postDetailAPI(postId)?.enqueue(object : Callback<Post?> {
                override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                    Log.i("post Detail: ","success")
                    Log.i("post Detail: ",response.body()?.postList?.postText.toString())
                    Log.i("post Detail: ",response.body()?.postList?.date.toString())


                    context?.let { it1 ->
                        Glide.with(it1)
                            .load(Uri.parse(response.body()?.postList?.postUserImage.toString()))
                            .centerCrop()
                            .circleCrop()
                            .into(userImage)

                        Glide.with(it1)
                            .load(Uri.parse(response.body()?.postList?.postImage?.get(0)?.postImageUrl.toString()))
                            .centerCrop()
                            .into(postImage)

                    }
                    userId.text = response.body()?.postList?.postUserId
                    date.text = response.body()?.postList?.date
                    postText.text = response.body()?.postList?.postText
                    likeCount.text = response.body()?.postList?.likeCount.toString()
                    ReviewCount.text = response.body()?.postList?.commentCount.toString()
                }

                override fun onFailure(call: Call<Post?>, t: Throwable) {
                    Log.i("post Detail: ","fail")
                }

            })

        var postLike: HashMap<String, String> = HashMap()
        postLike["x-access-token"] = PreferenceManager.getString(myContext!!.applicationContext, "jwt").toString()
        Log.i("jwt: ",PreferenceManager.getString(myContext!!.applicationContext, "jwt").toString())

        apiService.postLikeAPI(postId,postLike)?.enqueue(object : Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                Log.i("post Liked: ","success")
                if(response.body()?.getResultList()?.liked==1 ){
                    //좋아요
                    Log.i("post Liked: ","좋아요")
                    likeId.setImageResource(R.drawable.liked)
                    likeTemp = 1
                }
                else{
                    //좋아요 x
                    Log.i("post Liked: ","좋아요 x")
                    likeId.setImageResource(R.drawable.unliked)
                    likeTemp = 0
                }

            }

            override fun onFailure(call: Call<Post?>, t: Throwable) {
                Log.i("post Liked: ","fail")
            }

        })


        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onAttach(activity: Activity) {
        myContext = activity as FragmentActivity
        super.onAttach(activity)
    }



}

