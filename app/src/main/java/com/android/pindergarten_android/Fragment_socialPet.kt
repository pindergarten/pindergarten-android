package com.android.pindergarten_android

import android.app.Activity
import android.graphics.Color
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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

    var adapter = MyAdapter(postImage,postText,userImage,userId,postLiked,this)
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

        val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }
        Log.i("jwt : ",sharedPreferences.toString())



        var recyclerview_main = view.findViewById<RecyclerView>(R.id.recyclerview_main)
        var recyclerView = recyclerview_main // recyclerview id


        var refresh_layout = view.findViewById<SwipeRefreshLayout>(R.id.refresh_layout)
        refresh_layout.setColorSchemeColors(requireContext().resources.getColor(R.color.brown))
        refresh_layout.setOnRefreshListener {

            //??????: ?????? ????????? ?????????
            apiService.searchAllPostAPI(sharedPreferences.toString())?.enqueue(object : Callback<Post?> {
                override fun onResponse(call: Call<Post?>, response: Response<Post?>) {

                    Log.i("refresh count:", response.body()?.allPostList?.size.toString())
                    Log.i("refresh success:", response.body()?.success.toString())

                    postImage.clear()
                    userImage.clear()
                    userId.clear()
                    postText.clear()
                    postId.clear()
                    postLiked.clear()

                    for( i in 0 until response.body()?.allPostList?.size!!){

                        Log.i("${i}?????? postImage: ",response.body()?.allPostList!![i].thumbnail.toString())
                        Log.i("${i}?????? userImage: ",response.body()?.allPostList!![i].user_image.toString())
                        Log.i("${i}?????? userId: ",response.body()?.allPostList!![i].user_id.toString())
                        Log.i("${i}?????? postText: ",response.body()?.allPostList!![i].content.toString())
                        Log.i("${i}?????? postId: ",response.body()?.allPostList!![i].id.toString())
                        Log.i("${i}?????? postLiked: ",response.body()?.allPostList!![i].isLiked.toString())

                        postImage.add(Uri.parse(response.body()?.allPostList!![i].thumbnail.toString()))
                        userImage.add(Uri.parse(response.body()?.allPostList!![i].user_image.toString()))
                        userId.add(response.body()?.allPostList!![i].user_id.toString())
                        postText.add(response.body()?.allPostList!![i].content.toString())
                        postId.add((response.body()?.allPostList!![i].id.toString()).toInt())
                        response.body()?.allPostList!![i].isLiked?.let { postLiked.add(it) }

                    }

                    adapter.notifyDataSetChanged()


                    Log.i("refresh","??????")
                }

                override fun onFailure(call: Call<Post?>, t: Throwable) {
                    Log.i("refresh ??????: ",t.toString())
                }

            })

            refresh_layout.isRefreshing = false
        }

        recyclerView.adapter = adapter
        recyclerView.itemAnimator = null

        adapter.notifyDataSetChanged()

        val StaggeredGridLayoutManager = StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = StaggeredGridLayoutManager


        //??????: ?????? ????????? ??????
        apiService.searchAllPostAPI(sharedPreferences.toString())?.enqueue(object : Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {

                Log.i("allPost count:", response.body()?.allPostList?.size.toString())
                Log.i("allPost success:", response.body()?.success.toString())

                postImage.clear()
                userImage.clear()
                userId.clear()
                postText.clear()
                postId.clear()
                postLiked.clear()

                for( i in 0 until response.body()?.allPostList?.size!!){

                    Log.i("${i}?????? postImage: ",response.body()?.allPostList!![i].thumbnail.toString())
                    Log.i("${i}?????? userImage: ",response.body()?.allPostList!![i].user_image.toString())
                    Log.i("${i}?????? userId: ",response.body()?.allPostList!![i].user_id.toString())
                    Log.i("${i}?????? postText: ",response.body()?.allPostList!![i].content.toString())
                    Log.i("${i}?????? postId: ",response.body()?.allPostList!![i].id.toString())
                    Log.i("${i}?????? postLiked: ",response.body()?.allPostList!![i].isLiked.toString())

                    postImage.add(Uri.parse(response.body()?.allPostList!![i].thumbnail.toString()))
                    userImage.add(Uri.parse(response.body()?.allPostList!![i].user_image.toString()))
                    userId.add(response.body()?.allPostList!![i].user_id.toString())
                    postText.add(response.body()?.allPostList!![i].content.toString())
                    postId.add((response.body()?.allPostList!![i].id.toString()).toInt())
                    response.body()?.allPostList!![i].isLiked?.let { postLiked.add(it) }

                }

                adapter.notifyDataSetChanged()

                Log.i("socialPet: ","??????")
            }

            override fun onFailure(call: Call<Post?>, t: Throwable) {
                Log.i("socialPet ??????: ",t.toString())
            }

        })


        adapter.setItemClickListener( object : MyAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                Log.i("clicked: ", "${userId.get(position)} ?????? ???????????? ?????????????????????. ")
                Log.i("????????? ??????","${postText.get(position)}")

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
                    //????????? ??????
                    Log.i("Liked","like")
                    postLiked[position]=1
                }
                else{
                    //????????? ??????
                    Log.i("Liked","unlike")
                    postLiked[position]=0
                }

                //?????? ????????? adapter update
                adapter.notifyItemChanged(position)

                //????????? ?????? API
                var temp: HashMap<String, String> = HashMap()
                apiService.postLikeAPI(postId[position],sharedPreferences.toString(),temp)?.enqueue(object : Callback<Post?> {
                    override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                        Log.i("postLiked: ","?????? ??????")
                        Log.i("Liked", postLiked[position].toString())
                    }
                    override fun onFailure(call: Call<Post?>, t: Throwable) {
                        Log.i("postLiked: ","fail")
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

