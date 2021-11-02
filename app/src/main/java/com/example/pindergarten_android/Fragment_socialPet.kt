package com.example.pindergarten_android

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Fragment_socialPet : Fragment() {

    private var myContext: FragmentActivity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_socialpet,container,false)
        var recyclerview_main = view.findViewById<RecyclerView>(R.id.recyclerview_main)
        var recyclerView = recyclerview_main // recyclerview id
        var layoutManager = LinearLayoutManager(container?.context)

        recyclerView.layoutManager = layoutManager

        var adapter = MyAdapter()
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

