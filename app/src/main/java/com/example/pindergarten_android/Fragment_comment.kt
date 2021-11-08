package com.example.pindergarten_android

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Fragment_comment : Fragment() {

    private var myContext: FragmentActivity? = null


    var eventId : Int = 0
    var userImg = ArrayList<Uri>()
    var nickName = ArrayList<String>()
    var userId = ArrayList<Int>()
    var userDetail = ArrayList<String>()
    var userDate = ArrayList<String>()
    var commentId = ArrayList<Int>()
    val adapter = CommentAdapter2(userImg,nickName,userDetail,userDate,this)

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    var dialog : AlertDialog ?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_comment,container,false)

        //navigate hide
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavigation(true)

        //fragment 데이터 전달 받는
        var bundle: Bundle
        if(arguments!=null){
            bundle = arguments as Bundle
            eventId = bundle.getInt("eventId")
        }


        var recyclerview_main = view.findViewById<RecyclerView>(R.id.recyclerView)
        var recyclerView = recyclerview_main // recyclerview id
        var layoutManager = LinearLayoutManager(container?.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter


        val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }
        Log.i("jwt : ",sharedPreferences.toString())

        //서버: 이벤트 댓글 확인
        apiService.eventCommentAPI(eventId, sharedPreferences.toString())?.enqueue(object :
            Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                Log.i("event Comment: ", "success")

                userImg.clear()
                userId.clear()
                userDetail.clear()
                userDate.clear()
                commentId.clear()
                nickName.clear()

                for( i in 0 until response.body()?.commentList!!.size){

                    Log.i("${i}번째 date: ",response.body()?.commentList!![i].date.toString())
                    Log.i("${i}번째 userImage: ",response.body()?.commentList!![i].user_image.toString())
                    Log.i("${i}번째 nickName: ",response.body()?.commentList!![i].nickname.toString())
                    Log.i("${i}번째 comment: ",response.body()?.commentList!![i].content.toString())
                    Log.i("${i}번째 commentId: ",response.body()?.commentList!![i].id.toString())


                    userImg.add(Uri.parse(response.body()?.commentList!![i].user_image.toString()))
                    nickName.add(response.body()?.commentList!![i].nickname.toString())
                    userDetail.add(response.body()?.commentList!![i].content.toString())
                    userDate.add(response.body()?.commentList!![i].date.toString())
                    commentId.add(response.body()?.commentList!![i].id!!.toInt())
                    userId.add(response.body()?.commentList!![i].userId!!.toInt())
                }

                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<Post?>, t: Throwable) {
                Log.i("event Comment: ", "실패")
            }

        })

        var button = view.findViewById<Button>(R.id.button)
        var comment = view.findViewById<EditText>(R.id.editText)

        var content: HashMap<String, String> = HashMap()
        content["content"] = comment.text.toString()

        button.setOnClickListener{
            //서버에 댓글 저장
            apiService.addEventCommentAPI(eventId,sharedPreferences.toString(),content)?.enqueue(object : Callback<Post?> {
                override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                    Log.i("add Comment", "성공")
                    Log.i("add comment",comment.text.toString())

                    val transaction = myContext!!.supportFragmentManager.beginTransaction()
                    val fragment : Fragment = Fragment_comment()
                    val bundle = Bundle()
                    bundle.putInt("eventId", eventId)
                    fragment.arguments=bundle
                    transaction.replace(R.id.container,fragment)
                    transaction.commit()
                }

                override fun onFailure(call: Call<Post?>, t: Throwable) {
                    Log.i("add Comment", "실패")
                }

            })
        }

        //댓글 삭제하기
        adapter.setItemLongClickListener(object :CommentAdapter2.ItemLongClickListener{
            override fun onClick(view: View, position: Int) {
                var myId =  myContext?.let { PreferenceManager.getInt(it,"userId") }
                if(userId[position]==myId){
                //삭제하기 기능
                Log.i("삭제하기 기능","!!")
                val builder = AlertDialog.Builder(myContext)
                val view: View = LayoutInflater.from(myContext).inflate(R.layout.post_delete, null)
                val deleteBtn = view?.findViewById<ImageButton>(R.id.deleteBtn)
                val cancelBtn = view?.findViewById<ImageButton>(R.id.cancelBtn)
                dialog = builder.create()
                dialog!!.setView(view)
                dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
                dialog!!.window!!.setGravity(Gravity.BOTTOM)
                dialog!!.show()

                cancelBtn?.setOnClickListener{
                    dialog!!.dismiss()
                }
                deleteBtn?.setOnClickListener{
                    //댓글 삭제
                    apiService.deleteEventCommentAPI(sharedPreferences.toString(),eventId,commentId[position])?.enqueue(object :
                        Callback<Post?> {
                        override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                            Log.i("delete event Comment: ", response.body()?.success.toString())

                            val transaction = myContext!!.supportFragmentManager.beginTransaction()
                            val fragment : Fragment = Fragment_comment()
                            val bundle = Bundle()
                            bundle.putInt("eventId", eventId)
                            fragment.arguments=bundle
                            transaction.replace(R.id.container,fragment)
                            transaction.commit()

                            dialog!!.dismiss()
                        }

                        override fun onFailure(call: Call<Post?>, t: Throwable) {
                            Log.i("delete event Comment: ", "실패")

                            val transaction = myContext!!.supportFragmentManager.beginTransaction()
                            val fragment : Fragment = Fragment_comment()
                            val bundle = Bundle()
                            bundle.putInt("eventId", eventId)
                            fragment.arguments=bundle
                            transaction.replace(R.id.container,fragment)
                            transaction.commit()

                            dialog!!.dismiss()
                        }

                    })

                }

            }

        }})

        var backBtn = view.findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_eventdetail()
            val bundle = Bundle()
            bundle.putInt("eventId", eventId)
            fragment.arguments=bundle
            transaction.replace(R.id.container,fragment)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


    }


}