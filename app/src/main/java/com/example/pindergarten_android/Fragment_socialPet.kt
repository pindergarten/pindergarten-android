package com.example.pindergarten_android

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Fragment_socialPet : Fragment() {

    private var myContext: FragmentActivity? = null

    var postImage = ArrayList<Uri>()
    var userImage = ArrayList<Uri>()
    var userId = ArrayList<String>()
    var postText = ArrayList<String>()
    var postId = ArrayList<Int>()

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_socialpet,container,false)
        var recyclerview_main = view.findViewById<RecyclerView>(R.id.recyclerview_main)
        var recyclerView = recyclerview_main // recyclerview id


        val gridLayoutManager = GridLayoutManager(context,2)
        recyclerView.layoutManager = gridLayoutManager

        //서버: 전체 게시글 확인
        apiService.searchAllPostAPI()?.enqueue(object : Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {

                Log.i("allPost count:", response.body()?.allPostList?.size.toString())
                Log.i("allPost success:", response.body()?.success.toString())

                postImage.clear()
                userImage.clear()
                userId.clear()
                postText.clear()
                postId.clear()

                for( i in 0 until response.body()?.allPostList?.size!!){

                    Log.i("${i}번째 postImage: ",response.body()?.allPostList!![i].thumbnail.toString())
                    Log.i("${i}번째 userImage: ",response.body()?.allPostList!![i].user_image.toString())
                    Log.i("${i}번째 userId: ",response.body()?.allPostList!![i].user_id.toString())
                    Log.i("${i}번째 postText: ",response.body()?.allPostList!![i].content.toString())
                    Log.i("${i}번째 postId: ",response.body()?.allPostList!![i].id.toString())

                    postImage.add(Uri.parse(response.body()?.allPostList!![i].thumbnail.toString()))
                    userImage.add(Uri.parse(response.body()?.allPostList!![i].user_image.toString()))
                    userId.add(response.body()?.allPostList!![i].user_id.toString())
                    postText.add(response.body()?.allPostList!![i].content.toString())
                    postId.add(Integer.parseInt(response.body()?.allPostList!![i].id.toString()))
                }

                Log.i("socialPet: ","성공")
            }

            override fun onFailure(call: Call<Post?>, t: Throwable) {
                Log.i("socialPet 실패: ",t.toString())
            }

        })

        val adapter = MyAdapter(postImage,postText,userImage,userId,this)

        adapter.setItemClickListener( object : MyAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                Log.i("clicked: ", "${userId.get(position)} 님의 게시물이 클릭되었습니다. ")
                Log.i("게시물 내용","${postText.get(position)}")

                val transaction = myContext!!.supportFragmentManager.beginTransaction()
                val fragment : Fragment = Fragment_postdetail()
                val bundle = Bundle()
                bundle.putInt("postId", postId[position])
                fragment.arguments=bundle
                transaction.replace(R.id.container,fragment)
                transaction.commit()

            }
        })

        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var plusBtn : ImageButton = myContext!!.findViewById<ImageButton>(R.id.plusBtn)
        plusBtn.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_addPost()
            transaction.replace(R.id.container,fragment)
            transaction.commit()
        }

        var eventBtn : ImageButton = myContext!!.findViewById<ImageButton>(R.id.eventBtn)
        eventBtn.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_event()
            transaction.replace(R.id.container,fragment)
            transaction.commit()
        }


    }

    override fun onAttach(activity: Activity) {
        myContext = activity as FragmentActivity
        super.onAttach(activity)
    }



}

