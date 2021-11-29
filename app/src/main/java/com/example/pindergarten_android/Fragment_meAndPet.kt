package com.example.pindergarten_android

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
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
    var myId : TextView ?=null
    var plusBtn : ImageButton ?=null
    var petLayout : LinearLayout?= null
    var postId = ArrayList<Int>()
    var userId : Int ?= null

    var nullPost : ImageView ?=null

    //petInfo
    var petId = ArrayList<Int>()
    var petImgUri = ArrayList<String>()
    var petName = ArrayList<String>()

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)


    val adapter = meAndPetAdapter(postImage,this)

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_meandpet,container,false)

        //navigate hide
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavigation(false)

        //fragment popup
        val fm: FragmentManager = requireActivity().supportFragmentManager
        for (i in 0 until fm.backStackEntryCount) {
            fm.popBackStack()
        }


        var recyclerview_main = view.findViewById<RecyclerView>(R.id.recyclerview_main)
        var recyclerView = recyclerview_main // recyclerview id

        myImg = view.findViewById(R.id.myImg)
        myId = view.findViewById(R.id.myId)
        plusBtn = view.findViewById(R.id.plusBtn)
        nullPost = view.findViewById(R.id.null_post)

        recyclerView.adapter = adapter

        val StaggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = StaggeredGridLayoutManager

        val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }
        Log.i("jwt : ",sharedPreferences.toString())


        //user 프로필조회
        myImg!!.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_userprofile()
            val bundle = Bundle()
            bundle.putInt("userId",userId!!)
            fragment.arguments=bundle
            transaction.replace(R.id.container,fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        //펫 정보
        petLayout = view.findViewById(R.id.petLayout)
        apiService.myPetSearchAPI(sharedPreferences.toString())?.enqueue(object : Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                Log.i("myPet search: ", "success")

                petImgUri.clear()
                petId.clear()
                petName.clear()

                Log.i("myPet",response.body()?.mypetList!!.size.toString())


                for( i in 0 until response.body()?.mypetList!!.size){

                    Log.i("${i}번째 myPet", response.body()?.mypetList!![i].id.toString())
                    Log.i("${i}번째 myPet", response.body()?.mypetList!![i].profile_image.toString())
                    Log.i("${i}번째 myPet", response.body()?.mypetList!![i].name.toString())

                    response.body()?.mypetList!![i].id?.let { petId.add(it) }
                    response.body()?.mypetList!![i].profile_image?.let { petImgUri.add(it) }
                    response.body()?.mypetList!![i].name?.let { petName.add(it) }
                }

                if(petId.size==0){
                    //버튼
                    val dynamicButton = ImageButton(myContext)
                    val viewParams = LinearLayout.LayoutParams(changeDP(328),LinearLayout.LayoutParams.MATCH_PARENT)
                    viewParams.setMargins(changeDP(16), 0, 0, 0)
                    dynamicButton.layoutParams = viewParams
                    dynamicButton.scaleType = ImageView.ScaleType.FIT_CENTER
                    dynamicButton.setBackgroundColor(Color.WHITE)
                    dynamicButton.setImageResource(R.drawable.app_pet_btn)

                    val alertDialog = AlertDialog.Builder(myContext).create()
                    dynamicButton.setOnClickListener{
                        val transaction = myContext!!.supportFragmentManager.beginTransaction()
                        val fragment : Fragment = Fragment_addPet()
                        transaction.replace(R.id.container,fragment)
                        transaction.addToBackStack(null)
                        transaction.commit()
                        alertDialog.dismiss()
                    }

                    petLayout!!.addView(dynamicButton)
                }
                else{
                    var nullSize  = 4 - petId.size
                    for( i in 0 until petId.size){

                        //레이아웃
                        var dynamicLayout = LinearLayout(myContext)
                        dynamicLayout.orientation = LinearLayout.VERTICAL
                        dynamicLayout.gravity = Gravity.CENTER
                        var params : LinearLayout.LayoutParams = LinearLayout.LayoutParams(changeDP(80),LinearLayout.LayoutParams.MATCH_PARENT)
                        dynamicLayout.layoutParams = params
                        dynamicLayout.setOnClickListener{
                            Log.i("clicked",petId[i].toString())

                            val transaction = myContext!!.supportFragmentManager.beginTransaction()
                            val fragment : Fragment = Fragment_petInfo()
                            val bundle = Bundle()
                            bundle.putInt("petId",petId[i])
                            bundle.putString("petName",petName[i])
                            bundle.putString("petImgUri",petImgUri[i].toString())
                            fragment.arguments=bundle
                            transaction.replace(R.id.container,fragment)
                            transaction.addToBackStack(null)
                            transaction.commit()
                        }

                        //사진
                        val dynamicView = ImageView(myContext)
                        val viewParams = LinearLayout.LayoutParams(changeDP(40),changeDP(40))
                        dynamicView.layoutParams = viewParams
                        dynamicView.adjustViewBounds=true

                        context?.let {
                            Glide.with(it)
                                .load(petImgUri[i])
                                .override(200, 200)
                                .centerCrop()
                                .circleCrop()
                                .into(dynamicView)
                        }

                        //텍스트
                        val dynamicText = TextView(myContext)
                        val textParams = LinearLayout.LayoutParams(changeDP(80),LinearLayout.LayoutParams.WRAP_CONTENT)
                        dynamicText.setTextColor(Color.GRAY)
                        dynamicText.textSize = 12.0f
                        dynamicText.layoutParams = textParams
                        dynamicText.text = petName[i]
                        dynamicText.gravity = Gravity.CENTER
                        textParams.setMargins(0, changeDP(3), 0, 0)

                        val lineView = ImageView(myContext)
                        val lineParams = LinearLayout.LayoutParams(changeDP(1), changeDP(70))
                        lineParams.gravity= Gravity.CENTER
                        lineView.layoutParams = lineParams
                        lineView.setImageResource(R.drawable.pet_line)

                        dynamicLayout.addView(dynamicView)
                        dynamicLayout.addView(dynamicText)
                        petLayout!!.addView(dynamicLayout)

                        if(i !=(petId.size-1)){
                            petLayout!!.addView(lineView)
                        }

                    }

                    for( j in 0 until nullSize){
                        // null 레이아웃
                        val dynamicLayout = LinearLayout(myContext)
                        dynamicLayout.orientation = LinearLayout.VERTICAL
                        dynamicLayout.gravity = Gravity.CENTER
                        val params : LinearLayout.LayoutParams = LinearLayout.LayoutParams(changeDP(80),LinearLayout.LayoutParams.MATCH_PARENT)
                        dynamicLayout.layoutParams = params

                        petLayout!!.addView(dynamicLayout)
                    }
                }


            }

            override fun onFailure(call: Call<Post?>, t: Throwable) {
                Log.i("myPet search: ", "실패")
            }

        })



        //서버: 사용자 게시글 확인
        apiService.myPostAPI(sharedPreferences.toString())?.enqueue(object :
            Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {

                postImage.clear()
                postId.clear()

                //default image
                if(response.body()?.mypostsList!!.size == 0){
                    nullPost!!.visibility = View.VISIBLE
                }
                else{
                    nullPost!!.visibility = View.INVISIBLE
                }

                for( i in 0 until response.body()?.mypostsList?.size!!){
                    postImage.add(Uri.parse(response.body()?.mypostsList!![i].thumbnail.toString()))
                    postId.add(Integer.parseInt(response.body()?.mypostsList!![i].id.toString()))
                }


                Glide.with(myContext!!)
                    .load(Uri.parse(response.body()?.user?.profile_img.toString()))
                    .centerCrop()
                    .circleCrop()
                    .into(myImg!!)

                myId!!.text = response.body()?.user?.nickname

                userId = response.body()?.user?.id

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

                val transaction = myContext!!.supportFragmentManager.beginTransaction()
                val fragment : Fragment = Fragment_postdetail()
                val bundle = Bundle()
                bundle.putInt("postId", postId[position])
                bundle.putString("moveFragment", "meAndPet")
                fragment.arguments=bundle
                transaction.replace(R.id.container,fragment)
                transaction.addToBackStack(null)
                transaction.commit()

            }
        })


        plusBtn!!.setOnClickListener{
            val view2 = View.inflate(myContext,R.layout.meandpet_popup,null)
            var addPet : Button = view2.findViewById(R.id.addPet)
            var addPost : Button = view2.findViewById(R.id.addPost)

            val alertDialog = AlertDialog.Builder(myContext).create()
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

            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_setting()
            val bundle = Bundle()
            bundle.putInt("userId",userId!!)
            fragment.arguments=bundle
            transaction.replace(R.id.container,fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }

    override fun onAttach(activity: Activity) {
        myContext = activity as FragmentActivity
        super.onAttach(activity)

    }


    private fun changeDP(value: Int): Int {
        var displayMetrics = myContext!!.resources.displayMetrics
        return Math.round(value * displayMetrics.density)
    }




}
