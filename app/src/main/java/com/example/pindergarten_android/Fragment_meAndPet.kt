package com.example.pindergarten_android

import android.app.Activity
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Fragment_meAndPet : Fragment() {

    private var myContext: FragmentActivity? = null

    var postImage = ArrayList<Uri>()
    var myImg  : ImageView?=null
    var plusBtn : ImageButton ?=null

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    val adapter = meAndPetAdapter(postImage,this)
    private lateinit var callback: OnBackPressedCallback
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_meandpet,container,false)

        //navigate hide
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavigation(false)


        var recyclerview_main = view.findViewById<RecyclerView>(R.id.recyclerview_main)
        var recyclerView = recyclerview_main // recyclerview id

        myImg = view.findViewById(R.id.myImg)
        plusBtn = view.findViewById(R.id.plusBtn)

        recyclerView.adapter = adapter

        val StaggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = StaggeredGridLayoutManager

        val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }
        Log.i("jwt : ",sharedPreferences.toString())


        //서버: 전체 게시글 확인
        apiService.searchAllPostAPI(sharedPreferences.toString())?.enqueue(object :
            Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {

                postImage.clear()

                for( i in 0 until response.body()?.allPostList?.size!!){
                    postImage.add(Uri.parse(response.body()?.allPostList!![i].thumbnail.toString()))
                }

                //임시
                Glide.with(context!!)
                    .load(Uri.parse(response.body()?.allPostList!![0].user_image.toString()))
                    .centerCrop()
                    .circleCrop()
                    .into(myImg!!)

                adapter.notifyDataSetChanged()
                Log.i("meAndPet: ","성공")
            }

            override fun onFailure(call: Call<Post?>, t: Throwable) {
                Log.i("meAndPet 실패: ",t.toString())
            }

        })

        adapter.setItemClickListener( object : meAndPetAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                Log.i("clicked: ", "게시물이 클릭되었습니다. ")
            }
        })


        plusBtn!!.setOnClickListener{
            val view2 = View.inflate(context,R.layout.meandpet_popup,null)
            var addPet : Button = view2.findViewById(R.id.addPet)
            var addPost : Button = view2.findViewById(R.id.addPost)

            val alertDialog = AlertDialog.Builder(context).create()
            addPet.setOnClickListener{
                val transaction = myContext!!.supportFragmentManager.beginTransaction()
                val fragment : Fragment = Fragment_addPet()
                transaction.replace(R.id.container,fragment)
                transaction.addToBackStack(null)
                transaction.commit()
                alertDialog.dismiss()
            }

            addPost.setOnClickListener{
                val transaction = myContext!!.supportFragmentManager.beginTransaction()
                val fragment : Fragment = Fragment_addPost()
                val bundle = Bundle()
                bundle.putString("fragment", "meAndPet")
                fragment.arguments=bundle
                transaction.replace(R.id.container,fragment)
                transaction.addToBackStack(null)
                transaction.commit()
                alertDialog.dismiss()
            }

            alertDialog.setView(view2)
            alertDialog.show()
        }

        var settingBtn : ImageButton = view!!.findViewById(R.id.settingBtn)
        settingBtn.setOnClickListener{
            /*
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_event()
            transaction.replace(R.id.container,fragment)
            transaction.addToBackStack(null)
            transaction.commit()

             */
        }

        return view
    }

    override fun onAttach(activity: Activity) {
        myContext = activity as FragmentActivity
        super.onAttach(activity)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.i("callback","뒤로가기")
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }





}
