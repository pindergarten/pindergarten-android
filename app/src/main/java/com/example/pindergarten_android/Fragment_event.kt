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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class Fragment_event : Fragment() {

    private var myContext: FragmentActivity? = null

    var eventImage = ArrayList<Uri>()
    var eventTitle = ArrayList<String>()
    var eventDay = ArrayList<Int>()
    var eventId = ArrayList<Int>()
    val adapter = EventAdapter(eventImage,eventTitle,eventDay,this)

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    private lateinit var callback: OnBackPressedCallback
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_event,container,false)

        //navigate hide
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavigation(true)

        var recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val gridLayoutManager = GridLayoutManager(context,2)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adapter

        adapter.setItemClickListener( object : EventAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                Log.i("clicked: ", "${eventTitle.get(position)} ")
                val transaction = myContext!!.supportFragmentManager.beginTransaction()
                val fragment : Fragment = Fragment_eventdetail()
                val bundle = Bundle()
                bundle.putInt("eventId", eventId.get(position))
                fragment.arguments=bundle
                transaction.replace(R.id.container,fragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        })



        val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }
        Log.i("jwt : ",sharedPreferences.toString())


        //서버: 전체 이벤트 확인
        apiService.searchAllEventAPI()?.enqueue(object : Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {

                Log.i("allEvent success:", response.body()?.success.toString())

                eventImage.clear()
                eventTitle.clear()
                eventDay.clear()
                eventId.clear()

                //날짜계산
                var today : Calendar = Calendar.getInstance()
                var eventday : Calendar = Calendar.getInstance()

                for( i in 0 until response.body()?.allEventList?.size!!){

                    Log.i("${i}번째 eventImage: ",response.body()?.allEventList!![i].thumbnail.toString())
                    Log.i("${i}번째 eventTitle: ",response.body()?.allEventList!![i].title.toString())
                    Log.i("${i}번째 eventId: ",response.body()?.allEventList!![i].id.toString())


                    eventImage.add(Uri.parse(response.body()?.allEventList!![i].thumbnail.toString()))
                    eventTitle.add(response.body()?.allEventList!![i].title.toString())
                    eventId.add(Integer.parseInt(response.body()?.allEventList!![i].id.toString()))
                    //d-day 계산
                    var day_temp = response.body()?.allEventList!![i].expired_at.toString()
                    eventday.set(Integer.parseInt(day_temp.split(".")[0]),Integer.parseInt(day_temp.split(".")[1]),Integer.parseInt(day_temp.split(".")[2]))
                    var event_day : Long = eventday.timeInMillis/86400000
                    var to_day : Long = today.timeInMillis/86400000

                    eventDay.add((to_day-event_day+1).toInt())
                }

                adapter.notifyDataSetChanged()
                Log.i("allEvent: ","성공")
            }

            override fun onFailure(call: Call<Post?>, t: Throwable) {
                Log.i("allEvent 실패: ",t.toString())
            }

        })

        var backBtn = view.findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_socialPet()
            val bundle = Bundle()
            fragment.arguments=bundle
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


    }



}