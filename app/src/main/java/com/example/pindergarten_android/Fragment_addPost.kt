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
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
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
    var addPostBtn : ImageButton ?=null
    var imm : InputMethodManager ?=null
    var fragment : String ?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_addpost,container,false)

        //navigate hide
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavigation(true)

        var bundle: Bundle
        if(arguments!=null){
            bundle = arguments as Bundle
            fragment = bundle.getString("fragment").toString()
        }

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

        addPostBtn = view.findViewById(R.id.addPostBtn)
        addPostBtn!!.setOnClickListener{
            if(list.size ==0){
                //1개이상 사진업로드 공고메세지
                val view2 = View.inflate(context,R.layout.join_popup,null)
                var text : TextView = view2.findViewById(R.id.text)
                var button : Button = view2.findViewById(R.id.button)
                text.text="게시물 등록시 1개 이상의\n" + "사진이 필요합니다."
                button.text="확인"
                addPostBtn!!.setImageResource(R.drawable.register_btn)
                val alertDialog = AlertDialog.Builder(context).create()
                button.setOnClickListener{ alertDialog.dismiss() }
                alertDialog.setView(view2)
                alertDialog.show()
            }
            else{
                //서버요청

                //keyboard control
                imm = requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.hideSoftInputFromWindow(view.windowToken,0)

                val view2 = View.inflate(context,R.layout.join_popup,null)
                var text : TextView = view2.findViewById(R.id.text)
                var button : Button = view2.findViewById(R.id.button)
                text.text="게시물이 정상적으로 등록 되었습니다."
                button.text="확인"

                val alertDialog = AlertDialog.Builder(context).create()
                button.setOnClickListener{
                    alertDialog.dismiss()
                    if(fragment == "socialPet"){
                        val transaction = myContext!!.supportFragmentManager.beginTransaction()
                        val fragment : Fragment = Fragment_socialPet()
                        val bundle = Bundle()
                        fragment.arguments=bundle
                        transaction.replace(R.id.container,fragment)
                        transaction.commit()
                    }
                    else if (fragment == "meAndPet"){
                        val transaction = myContext!!.supportFragmentManager.beginTransaction()
                        val fragment : Fragment = Fragment_meAndPet()
                        val bundle = Bundle()
                        fragment.arguments=bundle
                        transaction.replace(R.id.container,fragment)
                        transaction.commit()
                    }

                }
                alertDialog.setView(view2)
                alertDialog.show()
            }
        }

        var backBtn = view.findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener{
            if(fragment == "socialPet"){
                val transaction = myContext!!.supportFragmentManager.beginTransaction()
                val fragment : Fragment = Fragment_socialPet()
                val bundle = Bundle()
                fragment.arguments=bundle
                transaction.replace(R.id.container,fragment)
                transaction.commit()
            }
            else if (fragment == "meAndPet"){
                val transaction = myContext!!.supportFragmentManager.beginTransaction()
                val fragment : Fragment = Fragment_meAndPet()
                val bundle = Bundle()
                fragment.arguments=bundle
                transaction.replace(R.id.container,fragment)
                transaction.commit()
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
                    val view2 = View.inflate(context,R.layout.join_popup,null)
                    var text : TextView = view2.findViewById(R.id.text)
                    var button : Button = view2.findViewById(R.id.button)
                    text.text="사진은 10장까지 가능합니다."
                    button.text="확인"
                    val alertDialog = AlertDialog.Builder(context).create()
                    button.setOnClickListener{ alertDialog.dismiss() }
                    alertDialog.setView(view2)
                    alertDialog.show()
                    addPostBtn!!.setImageResource(R.drawable.register_btn)
                    return
                }
                else{
                    for(i in 0 until count){
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        list.add(imageUri)
                        addPostBtn!!.setImageResource(R.drawable.register_btn2)

                        image_count!!.text = "${count}/10"
                    }
                }
            }
            else{
                data?.data?.let{
                    uri ->
                    val imageUri : Uri?= data?.data
                    if(imageUri!=null){
                        list.add(imageUri)
                        addPostBtn!!.setImageResource(R.drawable.register_btn2)
                    }
                }
            }
            adapter.notifyDataSetChanged()
        }


    }
}