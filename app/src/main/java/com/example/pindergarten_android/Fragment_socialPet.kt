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
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
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
    var postLiked = ArrayList<Int>()

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    val adapter = MyAdapter(postImage,postText,userImage,userId,postLiked,this)
    var mainAct: MainActivity ?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_socialpet,container,false)

        //navigate hide
        mainAct = activity as MainActivity
        mainAct!!.HideBottomNavigation(false)


        //fragment popup
        val fm: FragmentManager = requireActivity().supportFragmentManager
        for (i in 0 until fm.backStackEntryCount) {
            fm.popBackStack()
        }


        var recyclerview_main = view.findViewById<RecyclerView>(R.id.recyclerview_main)
        var recyclerView = recyclerview_main // recyclerview id


        recyclerView.adapter = adapter

        val StaggeredGridLayoutManager = StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = StaggeredGridLayoutManager

        val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }
        Log.i("jwt : ",sharedPreferences.toString())


        //서버: 전체 게시글 확인
        apiService.searchAllPostAPI(sharedPreferences.toString())?.enqueue(object : Callback<Post?> {
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
                    Log.i("${i}번째 postLiked: ",response.body()?.allPostList!![i].isLiked.toString())

                    postImage.add(Uri.parse(response.body()?.allPostList!![i].thumbnail.toString()))
                    userImage.add(Uri.parse(response.body()?.allPostList!![i].user_image.toString()))
                    userId.add(response.body()?.allPostList!![i].user_id.toString())
                    postText.add(response.body()?.allPostList!![i].content.toString())
                    postId.add((response.body()?.allPostList!![i].id.toString()).toInt())
                    response.body()?.allPostList!![i].isLiked?.let { postLiked.add(it) }

                }

                adapter.notifyDataSetChanged()
                Log.i("socialPet: ","성공")
            }

            override fun onFailure(call: Call<Post?>, t: Throwable) {
                Log.i("socialPet 실패: ",t.toString())
            }

        })


        adapter.setItemClickListener( object : MyAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                Log.i("clicked: ", "${userId.get(position)} 님의 게시물이 클릭되었습니다. ")
                Log.i("게시물 내용","${postText.get(position)}")

                val transaction = myContext!!.supportFragmentManager.beginTransaction()
                val fragment : Fragment = Fragment_postdetail()
                val bundle = Bundle()
                bundle.putInt("postId", postId[position])
                bundle.putString("moveFragment", "socialPet")
                fragment.arguments=bundle
                transaction.replace(R.id.container,fragment)
                transaction.addToBackStack(null)
                transaction.commit()

            }
        })

        adapter.setLikedClickListener(object:MyAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                if(postLiked[position]==0){
                    //좋아요 누름
                    postLiked[position]=1
                }
                else{
                    //좋아요 취소
                    postLiked[position]=0
                }

                //일정 부분의 adapter update
                adapter.notifyItemChanged(position)
                //좋아요 변경 API
                var temp: HashMap<String, String> = HashMap()
                apiService.postLikeAPI(postId[position],sharedPreferences.toString(),temp)?.enqueue(object : Callback<Post?> {
                    override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                        Log.i("post Liked: ","수정 성공")
                    }
                    override fun onFailure(call: Call<Post?>, t: Throwable) {
                        Log.i("post Detail: ","fail")
                    }

                })


            }

        })


        var plusBtn : ImageButton = view!!.findViewById(R.id.plusBtn)
        plusBtn.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_addPost()
            val bundle = Bundle()
            bundle.putString("fragment", "socialPet")
            fragment.arguments=bundle
            transaction.replace(R.id.container,fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        var eventBtn : ImageButton = view!!.findViewById(R.id.eventBtn)
        eventBtn.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_event()
            transaction.replace(R.id.container,fragment)
            transaction.addToBackStack(null)
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





}

