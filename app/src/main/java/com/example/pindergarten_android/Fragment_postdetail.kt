package com.example.pindergarten_android

import android.app.Activity
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Fragment_postdetail : Fragment() {

    private var myContext: FragmentActivity? = null

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)


    var dialog : AlertDialog ?=null
    var liked = -1


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_postdetail,container,false)

        val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }
        Log.i("jwt : ",sharedPreferences.toString())

        //navigate hide
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavigation(true)

        var postId : Int
        //fragment 데이터 전달 받는
        var bundle: Bundle
        if(arguments!=null){
            bundle = arguments as Bundle
            postId = bundle.getInt("postId")
            Log.i("bundle_postId",postId.toString())
        }
        else {
            postId  = -1
        }

        var userImage : ImageView = view.findViewById(R.id.user_image)
        var userId : TextView = view.findViewById(R.id.user_id)
        var noticeBtn : ImageButton = view.findViewById(R.id.noticeBtn)
        var postImage : ImageView = view.findViewById(R.id.post_image)
        var likeCount : TextView = view.findViewById(R.id.likeCount)
        var ReviewCount : TextView =view.findViewById(R.id.ReviewCount)
        var date : TextView = view.findViewById(R.id.date)
        var postText : TextView = view.findViewById(R.id.postText)
        var likeId : ImageButton = view.findViewById(R.id.likeId)
        var ReviewId :ImageButton = view.findViewById(R.id.ReviewId)



        ReviewId.setOnClickListener{
            //게시물 댓글 보기
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_postcomment()
            val bundle = Bundle()
            bundle.putInt("postId", postId)
            fragment.arguments=bundle
            transaction.replace(R.id.container,fragment)
            transaction.commit()
        }

        noticeBtn.setOnClickListener {

            var myId = myContext?.let { PreferenceManager.getString(it, "nickName") }
            if (userId.text.toString() == myId) {
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
                    apiService.deletePostAPI(sharedPreferences.toString(),postId)?.enqueue(object :
                        Callback<Post?> {
                        override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                            Log.i("delete post ", response.body()?.success.toString())

                            val transaction = myContext!!.supportFragmentManager.beginTransaction()
                            val fragment : Fragment = Fragment_socialPet()
                            val bundle = Bundle()
                            fragment.arguments=bundle
                            transaction.replace(R.id.container,fragment)
                            transaction.commit()

                            dialog!!.dismiss()
                        }

                        override fun onFailure(call: Call<Post?>, t: Throwable) {
                            Log.i("delete post: ", "실패")

                            val transaction = myContext!!.supportFragmentManager.beginTransaction()
                            val fragment : Fragment = Fragment_postdetail()
                            val bundle = Bundle()
                            bundle.putInt("postId", postId)
                            fragment.arguments=bundle
                            transaction.replace(R.id.container,fragment)
                            transaction.commit()

                            dialog!!.dismiss()
                        }

                    })

                }

            } else {
                //신고하기 기능
                Log.i("신고하기 기능", "!!")
                val builder = AlertDialog.Builder(myContext)
                val view: View = LayoutInflater.from(myContext).inflate(R.layout.post_declare, null)
                val declareBtn = view?.findViewById<ImageButton>(R.id.delcareBtn)
                val cancelBtn = view?.findViewById<ImageButton>(R.id.cancelBtn)
                dialog = builder.create()
                dialog!!.setView(view)
                dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
                dialog!!.window!!.setGravity(Gravity.BOTTOM)
                dialog!!.show()

                cancelBtn?.setOnClickListener {
                    dialog!!.dismiss()
                }
                declareBtn?.setOnClickListener {
                    Log.i("신고", "!!")
                    //게시물 댓글 보기
                    val transaction = myContext!!.supportFragmentManager.beginTransaction()
                    val fragment: Fragment = Fragment_postDeclare()
                    val bundle = Bundle()
                    bundle.putInt("postId", postId)
                    fragment.arguments = bundle
                    transaction.replace(R.id.container, fragment)
                    transaction.commit()
                    dialog!!.dismiss()
                }
            }


        }


        likeId?.setOnClickListener{
            //게시물 좋아요
            if(liked==0){
                Log.i("post Liked: ","좋아요")
                likeId.setImageResource(R.drawable.liked)
                liked = 1
                likeCount.text = "${ Integer.parseInt(likeCount.text.toString()) + 1 }"
            }
            else{
                Log.i("post Liked: ","좋아요 취소")
                likeId.setImageResource(R.drawable.unliked)
                liked = 0
                likeCount.text = "${ Integer.parseInt(likeCount.text.toString()) - 1 }"
            }

            var temp: HashMap<String, String> = HashMap()

            //좋아요 변경 API
            apiService.postLikeAPI(postId,sharedPreferences.toString(),temp)?.enqueue(object : Callback<Post?> {
                override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                    Log.i("post Liked: ","수정 성공")
                }
                override fun onFailure(call: Call<Post?>, t: Throwable) {
                    Log.i("post Detail: ","fail")
                }

            })


        }


        //서버: 게시글 세부확인
        apiService.postDetailAPI(postId,sharedPreferences.toString())?.enqueue(object : Callback<Post?> {
                override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                    Log.i("post Detail: ","success")
                    Log.i("post Detail: ",response.body()?.postList?.postText.toString())
                    Log.i("post Detail: ",response.body()?.postList?.date.toString())


                    context?.let { it1 ->
                        Glide.with(it1)
                            .load(Uri.parse(response.body()?.postList?.postUserImage.toString()))
                            .centerCrop()
                            .circleCrop()
                            .into(userImage)

                        Glide.with(it1)
                            .load(Uri.parse(response.body()?.postList?.postImage!![0]?.postImageUrl.toString()))
                            .centerCrop()
                            .into(postImage)

                    }
                    userId.text = response.body()?.postList?.postUserId
                    date.text = response.body()?.postList?.date
                    postText.text = response.body()?.postList?.postText
                    likeCount.text = response.body()?.postList?.likeCount.toString()
                    ReviewCount.text = response.body()?.postList?.commentCount.toString()

                    var tempLiked = response.body()?.postList?.isLiked
                    if(tempLiked ==0){
                        //좋아요 x
                        Log.i("post Liked: ","좋아요 x")
                        likeId.setImageResource(R.drawable.unliked)
                        liked = 0
                    }
                    else{
                        //좋아요
                        Log.i("post Liked: ","좋아요")
                        likeId.setImageResource(R.drawable.liked)
                        liked = 1
                    }
                }

                override fun onFailure(call: Call<Post?>, t: Throwable) {
                    Log.i("post Detail: ","fail")
                }

            })

        var backBtn = view.findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_socialPet()
            val bundle = Bundle()
            bundle.putInt("postId", postId)
            fragment.arguments=bundle
            transaction.replace(R.id.container,fragment)
            transaction.commit()
        }


        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onAttach(activity: Activity) {
        myContext = activity as FragmentActivity
        super.onAttach(activity)
    }



}

