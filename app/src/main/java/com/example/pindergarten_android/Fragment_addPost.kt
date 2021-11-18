package com.example.pindergarten_android

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Fragment_addPost: Fragment() {

    private var myContext: FragmentActivity? = null
    var addPhoto : ImageButton ?=null
    private lateinit var callback: OnBackPressedCallback

    var list = ArrayList<Uri>()
    val adapter = MultiImageAdapter(list,this)

    var image_count : TextView ?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_addpost,container,false)

        //navigate hide
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavigation(true)

        var recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        image_count = view.findViewById(R.id.image_count)

        addPhoto = view.findViewById(R.id.addPhoto_btn)
        addPhoto!!.setOnClickListener{
            var intent = Intent(Intent.ACTION_PICK)
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
            intent.action= Intent.ACTION_GET_CONTENT

            startActivityForResult(intent,200)
        }

        val addPostBtn : ImageButton = view.findViewById<ImageButton>(R.id.addPostBtn)
        addPostBtn!!.setOnClickListener{
            if(list.size ==0){
                //1개이상 사진업로드 공고메세지
                val view2 = View.inflate(context,R.layout.join_popup,null)
                var text : TextView = view2.findViewById(R.id.text)
                var button : Button = view2.findViewById(R.id.button)
                text.text="게시물 등록시 1개 이상의\n" + "사진이 필요합니다."
                button.text="확인"
                val alertDialog = AlertDialog.Builder(context).create()
                button.setOnClickListener{ alertDialog.dismiss() }
                alertDialog.setView(view2)
                alertDialog.show()
            }
            else{
                //서버요청
                val view2 = View.inflate(context,R.layout.join_popup,null)
                var text : TextView = view2.findViewById(R.id.text)
                var button : Button = view2.findViewById(R.id.button)
                text.text="게시물이 정상적으로 등록 되었습니다."
                button.text="확인"
                val alertDialog = AlertDialog.Builder(context).create()
                button.setOnClickListener{
                    val transaction = myContext!!.supportFragmentManager.beginTransaction()
                    val fragment : Fragment = Fragment_socialPet()
                    transaction.replace(R.id.container,fragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                    alertDialog.dismiss()
                }
                alertDialog.setView(view2)
                alertDialog.show()
            }
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

        if(resultCode==RESULT_OK &&requestCode==200){
            list.clear()

            if(data?.clipData!=null){
                val count = data.clipData!!.itemCount
                if(count>10){
                    Toast.makeText(context,"사진은 10장까지 가능합니다.",Toast.LENGTH_LONG)
                    return
                }
                for(i in 0 until count){
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    list.add(imageUri)

                    image_count!!.text = "${count}/10"
                }
            }
            else{
                data?.data?.let{
                    uri ->
                    val imageUri : Uri?= data?.data
                    if(imageUri!=null){
                        list.add(imageUri)
                    }
                }
            }
            adapter.notifyDataSetChanged()
        }


    }
}