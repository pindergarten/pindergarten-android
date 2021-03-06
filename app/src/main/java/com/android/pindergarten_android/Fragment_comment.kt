package com.android.pindergarten_android

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
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

    var imm : InputMethodManager ?=null

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    var dialog : AlertDialog ?=null

    private lateinit var callback: OnBackPressedCallback
    var mainAct : MainActivity ?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_comment,container,false)

        //navigate hide
        mainAct = activity as MainActivity
        mainAct!!.HideBottomNavigation(true)

        //fragment ????????? ?????? ??????
        var bundle: Bundle
        if(arguments!=null){
            bundle = arguments as Bundle
            eventId = bundle.getInt("eventId")
        }


        var recyclerview_main = view.findViewById<RecyclerView>(R.id.recyclerView)
        var recyclerView = recyclerview_main // recyclerview id
        var layoutManager = LinearLayoutManager(container?.context)

        val spaceDecoration = VerticalSpaceItemDecoration(0)
        recyclerView.addItemDecoration(spaceDecoration)

        var parentlayout : ConstraintLayout = view.findViewById(R.id.parentlayout)
        parentlayout.setOnClickListener{
            //keyboard control
            imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(view.windowToken,0)

        }


        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter


        val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }
        Log.i("jwt : ",sharedPreferences.toString())

        //??????: ????????? ?????? ??????
        apiService.eventCommentAPI(eventId, sharedPreferences.toString())?.enqueue(object : Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                Log.i("event Comment: ", "success")

                userImg.clear()
                userId.clear()
                userDetail.clear()
                userDate.clear()
                commentId.clear()
                nickName.clear()

                for( i in 0 until response.body()?.commentList!!.size){

                    Log.i("${i}?????? date: ",response.body()?.commentList!![i].date.toString())
                    Log.i("${i}?????? userImage: ",response.body()?.commentList!![i].user_image.toString())
                    Log.i("${i}?????? nickName: ",response.body()?.commentList!![i].nickname.toString())
                    Log.i("${i}?????? comment: ",response.body()?.commentList!![i].content.toString())
                    Log.i("${i}?????? commentId: ",response.body()?.commentList!![i].id.toString())


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
                Log.i("event Comment: ", "??????")
            }

        })

        var button = view.findViewById<TextView>(R.id.button)
        var comment = view.findViewById<EditText>(R.id.editText)
        comment!!.requestFocus()
        comment?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if(comment!!.length()>0){
                    button!!.setTextColor(requireContext().resources.getColor(R.color.brown))
                }
                else{
                    button!!.setTextColor(Color.LTGRAY)
                }
            }
            override fun afterTextChanged(editable: Editable) {
                if(comment!!.length()>0){
                    button!!.setTextColor(requireContext().resources.getColor(R.color.brown))
                }
                else{
                    button!!.setTextColor(Color.LTGRAY)
                }
            }
        })

        button.setOnClickListener{
            if(comment.text.isNotEmpty()) {
                //????????? ?????? ??????
                apiService.addEventCommentAPI(eventId, sharedPreferences.toString(), comment.text.toString())?.enqueue(object : Callback<Post?> {
                    override fun onResponse(call: Call<Post?>, response: Response<Post?>) {

                        Log.i("add Comment", "??????")
                        Log.i("add comment", comment.text.toString())

                        val fm: FragmentManager = requireActivity().supportFragmentManager
                        fm.popBackStack()

                        val transaction = myContext!!.supportFragmentManager.beginTransaction()
                        val fragment : Fragment = Fragment_comment()
                        val bundle = Bundle()
                        bundle.putInt("eventId", eventId)
                        fragment.arguments=bundle
                        transaction.replace(R.id.container,fragment)
                        transaction.addToBackStack(null)
                        transaction.commit()

                    }

                    override fun onFailure(call: Call<Post?>, t: Throwable) {
                        Log.i("add Comment", "??????")
                    }

                })

            }
        }


        //?????? ????????????
        adapter.setItemClickListener(object :CommentAdapter2.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                var myId =  myContext?.let { PreferenceManager.getInt(it,"userId") }
                if(userId[position]==myId){
                //???????????? ??????
                Log.i("???????????? ??????","!!")
                val builder = AlertDialog.Builder(myContext)
                val view: View = LayoutInflater.from(myContext).inflate(R.layout.post_delete_comment, null)
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
                    //?????? ??????
                    apiService.deleteEventCommentAPI(sharedPreferences.toString(),eventId,commentId[position])?.enqueue(object : Callback<Post?> {
                        override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                            Log.i("delete event Comment: ", response.body()?.success.toString())

                            val transaction = myContext!!.supportFragmentManager.beginTransaction()
                            val fragment : Fragment = Fragment_comment()
                            val bundle = Bundle()
                            bundle.putInt("eventId", eventId)
                            fragment.arguments=bundle
                            transaction.replace(R.id.container,fragment)
                            transaction.addToBackStack(null)
                            transaction.commit()

                            val fm: FragmentManager = requireActivity().supportFragmentManager
                            fm.popBackStack()

                            dialog!!.dismiss()
                        }

                        override fun onFailure(call: Call<Post?>, t: Throwable) {
                            Log.i("delete event Comment: ", "??????")

                            dialog!!.dismiss()
                        }

                    })

                }

            }

        }})

        adapter.setUserClickListener(object :CommentAdapter2.UserClickListener{
            override fun onClick(view: View, position: Int) {

                val transaction = myContext!!.supportFragmentManager.beginTransaction()
                val fragment : Fragment = Fragment_others()
                val bundle = Bundle()
                bundle.putInt("userId", userId[position])
                bundle.putString("userNickname",nickName[position])
                fragment.arguments=bundle
                transaction.replace(R.id.container,fragment)
                transaction.addToBackStack(null)
                transaction.commit()

            }})

        var backBtn = view.findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener{
            mainAct!!.onBackPressed()
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

    inner class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int) :
        RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.bottom = verticalSpaceHeight
        }
    }


}