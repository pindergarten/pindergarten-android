package com.pindergarten_android.pindergarten_android

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

class Fragment_postcomment : Fragment() {

    private var myContext: FragmentActivity? = null

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    var userImg = ArrayList<Uri>()
    var nickName = ArrayList<String>()
    var userId = ArrayList<Int>()
    var userDetail = ArrayList<String>()
    var userDate = ArrayList<String>()
    var commentId = ArrayList<Int>()
    var dialog : AlertDialog ?=null

    var comment : EditText?=null
    var moveFragment : String ?=null
    var imm : InputMethodManager ?=null

    private lateinit var callback: OnBackPressedCallback
    val adapter = postCommentAdapter(userImg,nickName,userDetail,userDate,this)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_postcomment,container,false)

        //navigate hide
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavigation(true)

        var postId : Int
        //fragment 데이터 전달 받는
        var bundle: Bundle
        if(arguments!=null){
            bundle = arguments as Bundle
            postId = bundle.getInt("postId")
            moveFragment = bundle.getString("moveFragment")
            Log.i("bundle_postId",postId.toString())
        }
        else {
            postId  = -1
        }


        var recyclerview_main = view.findViewById<RecyclerView>(R.id.recyclerView)
        var recyclerView = recyclerview_main // recyclerview id

        val spaceDecoration = VerticalSpaceItemDecoration(0)
        recyclerView.addItemDecoration(spaceDecoration)

        var layoutManager = LinearLayoutManager(container?.context)

        var parentlayout : ConstraintLayout = view.findViewById(R.id.parentlayout)
        parentlayout.setOnClickListener{
            //keyboard control
            imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(view.windowToken,0)

        }


        val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }
        Log.i("jwt : ",sharedPreferences.toString())

        //서버: 게시글 댓글 확인
        apiService.postCommentAPI(postId, sharedPreferences.toString())?.enqueue(object : Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                Log.i("post Comment: ", "success")


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
                Log.i("post Comment: ", "실패")
            }

        })

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        var button =  view.findViewById<TextView>(R.id.button)
        comment = view.findViewById(R.id.editText)
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



        val sharedPreferences2 = myContext?.let { PreferenceManager.getString(it,"jwt") }
        Log.i("jwt : ",sharedPreferences2.toString())


        button.setOnClickListener{
            if(comment!!.text.isNotEmpty()){
            //서버에 댓글 저장
            apiService.addPostCommentAPI(postId,sharedPreferences2.toString(),comment?.text.toString())?.enqueue(object : Callback<Post?> {
                override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                    Log.i("add Comment: ", response.body()?.success.toString())
                    Log.i("add comment: ",comment?.text.toString())


                    val fm: FragmentManager = requireActivity().supportFragmentManager
                    fm.popBackStack()

                    val transaction = myContext!!.supportFragmentManager.beginTransaction()
                    val fragment : Fragment = Fragment_postcomment()
                    val bundle = Bundle()
                    bundle.putInt("postId", postId)
                    fragment.arguments=bundle
                    transaction.replace(R.id.container,fragment)
                    transaction.addToBackStack(null)
                    transaction.commit()


                }

                override fun onFailure(call: Call<Post?>, t: Throwable) {
                    Log.i("add Comment: ", "실패")
                }

            })
        }
        }

        //댓글 삭제하기
        adapter.setItemClickListener(object :postCommentAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                var myId =  myContext?.let { PreferenceManager.getInt(it,"userId") }
                if(userId[position]==myId){
                    //삭제하기 기능
                    Log.i("삭제하기 기능","!!")
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
                        //댓글 삭제
                        apiService.deletePostCommentAPI(sharedPreferences.toString(),postId,commentId[position])?.enqueue(object :
                            Callback<Post?> {
                            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                                Log.i("delete post Comment: ", response.body()?.success.toString())

                                val fm: FragmentManager = requireActivity().supportFragmentManager
                                fm.popBackStack()

                                val transaction = myContext!!.supportFragmentManager.beginTransaction()
                                val fragment : Fragment = Fragment_postcomment()
                                val bundle = Bundle()
                                bundle.putInt("postId", postId)
                                fragment.arguments=bundle
                                transaction.replace(R.id.container,fragment)
                                transaction.addToBackStack(null)
                                transaction.commit()

                                dialog!!.dismiss()
                            }

                            override fun onFailure(call: Call<Post?>, t: Throwable) {
                                Log.i("delete post Comment: ", "실패")

                                dialog!!.dismiss()
                            }

                        })

                    }

                }

            }})

        adapter.setUserClickListener(object :postCommentAdapter.UserClickListener{
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

            mainAct.onBackPressed()
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