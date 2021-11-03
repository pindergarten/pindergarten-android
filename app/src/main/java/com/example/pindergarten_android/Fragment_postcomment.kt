package com.example.pindergarten_android

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Fragment_postcomment : Fragment() {

    private var myContext: FragmentActivity? = null

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    var userImg = ArrayList<Uri>()
    var userId = ArrayList<String>()
    var userDetail = ArrayList<String>()
    var userDate = ArrayList<String>()
    var commentId = ArrayList<String>()
    val adapter = postCommentAdapter(userImg,userId,userDetail,userDate,this)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_postcomment,container,false)

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


        var recyclerview_main = view.findViewById<RecyclerView>(R.id.recyclerView)
        var recyclerView = recyclerview_main // recyclerview id
        var layoutManager = LinearLayoutManager(container?.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter


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

        /*
        //서버: 게시글 댓글 확인
        apiService.postCommentAPI(postId)?.enqueue(object : Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                Log.i("post Comment: ", "success")


                userImg.clear()
                userId.clear()
                userDetail.clear()
                userDate.clear()
                commentId.clear()

                for( i in 0 until response.body()?.commentList!!.size){

                    Log.i("${i}번째 date: ",response.body()?.commentList!![i].date.toString())
                    Log.i("${i}번째 userImage: ",response.body()?.commentList!![i].user_image.toString())
                    Log.i("${i}번째 userId: ",response.body()?.commentList!![i].user_id.toString())
                    Log.i("${i}번째 comment: ",response.body()?.commentList!![i].content.toString())
                    Log.i("${i}번째 commentId: ",response.body()?.commentList!![i].id.toString())


                    userImg.add(Uri.parse(response.body()?.commentList!![i].user_image.toString()))
                    userId.add(response.body()?.commentList!![i].user_id.toString())
                    userDetail.add(response.body()?.commentList!![i].content.toString())
                    userDate.add(response.body()?.commentList!![i].date.toString())
                    commentId.add(response.body()?.commentList!![i].id.toString())
                }


            }

            override fun onFailure(call: Call<Post?>, t: Throwable) {
                Log.i("post Comment: ", "실패")
            }

        })

         */

        adapter.notifyDataSetChanged()

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