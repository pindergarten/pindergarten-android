package com.example.pindergarten_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Fragment_socialPet : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_socialpet,container,false)
        var recyclerview_main = view.findViewById<RecyclerView>(R.id.recyclerview_main)
        var recyclerView = recyclerview_main // recyclerview id
        var layoutManager = LinearLayoutManager(container?.context)

        recyclerView.layoutManager = layoutManager

        var adapter = MyAdapter()
        recyclerView.adapter = adapter

        return view
    }


}
