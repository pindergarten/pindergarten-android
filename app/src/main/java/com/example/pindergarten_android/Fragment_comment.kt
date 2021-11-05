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
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Fragment_comment : Fragment() {

    private var myContext: FragmentActivity? = null


    var eventId : Int = 0
    var userImg = ArrayList<Uri>()
    var userId = ArrayList<String>()
    var userDetail = ArrayList<String>()
    var userDate = ArrayList<String>()
    val adapter = CommentAdapter2(userImg,userId,userDetail,userDate,this)

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_comment,container,false)

        //navigate hide
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavigation(true)

        //fragment 데이터 전달 받는
        var bundle: Bundle
        if(arguments!=null){
            bundle = arguments as Bundle
            eventId = bundle.getInt("eventId")
        }


        var recyclerview_main = view.findViewById<RecyclerView>(R.id.recyclerView)
        var recyclerView = recyclerview_main // recyclerview id
        var layoutManager = LinearLayoutManager(container?.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter


        val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }
        Log.i("jwt : ",sharedPreferences.toString())

        //서버: 게시글 댓글 확인
        apiService.postCommentAPI(eventId, sharedPreferences.toString())?.enqueue(object :
            Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                Log.i("event Comment: ", "success")

                userImg.clear()
                userId.clear()
                userDetail.clear()
                userDate.clear()


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
                }


            }

            override fun onFailure(call: Call<Post?>, t: Throwable) {
                Log.i("event Comment: ", "실패")
            }

        })


        adapter.notifyDataSetChanged()

        var button = view.findViewById<Button>(R.id.button)
        var comment = view.findViewById<EditText>(R.id.editText)

        var content: HashMap<String, String> = HashMap()
        content["content"] = comment.text.toString()

        button.setOnClickListener{
            if(comment.text.isNotEmpty()){
                //서버에 댓글 저장
                apiService.addPostCommentAPI(eventId,sharedPreferences.toString(),content)?.enqueue(object : Callback<Post?> {
                    override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                        Log.i("add Comment: ", "성공")
                        Log.i("add comment: ",comment.text.toString())

                        val transaction = myContext!!.supportFragmentManager.beginTransaction()
                        val fragment : Fragment = Fragment_comment()
                        val bundle = Bundle()
                        bundle.putInt("eventId", eventId)
                        fragment.arguments=bundle
                        transaction.replace(R.id.container,fragment)
                        transaction.commit()
                    }

                    override fun onFailure(call: Call<Post?>, t: Throwable) {
                        Log.i("add Comment: ", "실패")
                    }

                })

            }
        }

        var backBtn = view.findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_eventdetail()
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