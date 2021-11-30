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
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Fragment_likePindergarten : Fragment() {

    private var myContext: FragmentActivity? = null


    var pindergartenId = ArrayList<Int>()
    var pindergartenName = ArrayList<String>()
    var pindergartenAddress = ArrayList<String>()
    var pindergartenThumbnail = ArrayList<Uri>()
    var pindergartenRating = ArrayList<Double>()
    var pindergartenIsLiked = ArrayList<Int>()
    var pindergartenDistance = ArrayList<String>()

    val adapter = pindergartenAdapter2(pindergartenThumbnail,pindergartenName,pindergartenAddress,pindergartenRating,pindergartenIsLiked,pindergartenDistance,this)

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    var current_latitude :Double ?=null
    var current_longitude :Double ?=null

    private lateinit var callback: OnBackPressedCallback
    var mainAct : MainActivity ?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_likepindergarten,container,false)

        //navigate hide
        mainAct = activity as MainActivity
        mainAct!!.HideBottomNavigation(true)

        var bundle: Bundle
        if(arguments!=null){
            bundle = arguments as Bundle
            current_latitude = bundle.getDouble("latitude")
            current_longitude = bundle.getDouble("longitude")
            Log.i("current_latitude", current_latitude.toString())
            Log.i("current_longitude", current_longitude.toString())
        }
        else {
            Log.i("current location", null.toString())
        }


        var recyclerview_main = view.findViewById<RecyclerView>(R.id.recyclerView)
        var recyclerView = recyclerview_main // recyclerview id
        var layoutManager = LinearLayoutManager(container?.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }
        Log.i("jwt : ",sharedPreferences.toString())

        //서버: 좋아요한 유치원 출력
        apiService.likedPindergartenAPI(sharedPreferences.toString(),current_latitude!!,current_longitude!!)?.enqueue(object : Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                Log.i("likedPindergartenAPI", "success")

                pindergartenId.clear()
                pindergartenName.clear()
                pindergartenAddress.clear()
                pindergartenThumbnail.clear()
                pindergartenRating.clear()
                pindergartenIsLiked.clear()
                pindergartenDistance.clear()

                for (i in 0 until response.body()?.likePindergartenList?.size!!) {
                    pindergartenId.add(Integer.parseInt(response.body()?.likePindergartenList!![i].id.toString()))
                    pindergartenName.add(response.body()?.likePindergartenList!![i].name.toString())
                    pindergartenAddress.add(response.body()?.likePindergartenList!![i].address.toString())
                    pindergartenThumbnail.add(Uri.parse(response.body()?.likePindergartenList!![i].thumbnail.toString()))
                    pindergartenRating.add(
                        response.body()?.likePindergartenList!![i].rating.toString().toDouble()
                    )
                    //좋아요한 유치원
                    pindergartenIsLiked.add(1)
                    pindergartenDistance.add(String.format("%.1f", response.body()?.likePindergartenList!![i].distance!!.toDouble()) + "Km")
                    Log.i("${i + 1}번째 pindergarten 조회", pindergartenName[i])
                }

                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<Post?>, t: Throwable) {
                Log.i("likedPindergartenAPI", "실패")
            }

        })


        var backBtn = view.findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener{

            mainAct!!.onBackPressed()

        }

        adapter.setLikedClickListener(object:pindergartenAdapter2.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                if(pindergartenIsLiked[position]==0){
                    //좋아요 누름
                    pindergartenIsLiked[position]=1
                }
                else{
                    //좋아요 취소
                    pindergartenIsLiked[position]=0
                }

                //일정 부분의 adapter update
                adapter.notifyItemChanged(position)
                //좋아요 변경 API
                val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }
                Log.i("jwt : ",sharedPreferences.toString())
                var temp: HashMap<String, String> = HashMap()
                apiService.pindergartenLikeAPI(pindergartenId[position],sharedPreferences.toString(),temp)?.enqueue(object : Callback<Post?> {
                    override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                        Log.i("pindergarten Liked: ","수정 성공")
                    }
                    override fun onFailure(call: Call<Post?>, t: Throwable) {
                        Log.i("pindergarten Detail: ","fail")
                    }
                })
            }
        })

        adapter.setItemClickListener(object : pindergartenAdapter2.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                Log.i("item click",pindergartenName[position])

                val transaction = myContext!!.supportFragmentManager.beginTransaction()
                val fragment : Fragment = Fragment_detailPindergarten()
                val bundle = Bundle()
                current_latitude?.let { it1 -> bundle.putDouble("latitude", it1) }
                current_longitude?.let { it1 -> bundle.putDouble("longitude", it1) }
                bundle.putInt("pindergartenId",pindergartenId[position])
                bundle.putString("moved","liked")
                fragment.arguments=bundle
                transaction.replace(R.id.container,fragment)
                transaction.addToBackStack(null)
                transaction.commit()

            }

        })

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