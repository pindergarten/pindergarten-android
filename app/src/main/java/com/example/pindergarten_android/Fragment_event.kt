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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Fragment_event : Fragment() {

    private var myContext: FragmentActivity? = null

    var eventImage = ArrayList<Uri>()
    var eventTitle = ArrayList<String>()
    var eventDay = ArrayList<Int>()
    val adapter = EventAdapter(eventImage,eventTitle,eventDay,this)

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
                bundle.putString("eventTitle",eventTitle.get(position))
                bundle.putInt("eventDay",eventDay.get(position))
                bundle.putString("eventImage", eventImage.get(position).toString())
                fragment.arguments=bundle
                transaction.replace(R.id.container,fragment)
                transaction.commit()
            }
        })

        //서버요청
        for(i in 0 until 10){
            val event_img = Uri.parse("android.resource://com.example.pindergarten_android/drawable/test1")
            val event_title = "이벤트 ${i+1}"
            val event_day = 30
            eventImage.add(event_img)
            eventTitle.add(event_title)
            eventDay.add(event_day)
        }
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