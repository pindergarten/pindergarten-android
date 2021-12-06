package com.pindergarten_android.pindergarten_android

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
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import me.relex.circleindicator.CircleIndicator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Fragment_postdetail : Fragment() {

    private var myContext: FragmentActivity? = null

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    internal lateinit var viewpager : ViewPager
    var temp = ArrayList<Uri>()
    var user_Id : Int ?= null
    var postImageList = ArrayList<String>()
    var dialog : AlertDialog ?=null
    var liked = -1
    var moveFragment : String?=null

    private lateinit var callback: OnBackPressedCallback
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
            moveFragment = bundle.getString("moveFragment")
            Log.i("bundle_postId",postId.toString())
        }
        else {
            postId  = -1
        }

        var userImage : ImageView = view.findViewById(R.id.user_image)
        var userId : TextView = view.findViewById(R.id.user_id)
        var noticeBtn : ImageButton = view.findViewById(R.id.noticeBtn)
        viewpager = view.findViewById(R.id.post_image)
        var likeCount : TextView = view.findViewById(R.id.likeCount)
        var ReviewCount : TextView =view.findViewById(R.id.ReviewCount)
        var date : TextView = view.findViewById(R.id.date)
        var postText : TextView = view.findViewById(R.id.postText)
        var likeId : ImageButton = view.findViewById(R.id.likeId)
        var ReviewId :ImageButton = view.findViewById(R.id.ReviewId)
        var indicator : CircleIndicator = view.findViewById(R.id.indicator)

        val pagerAdapter = postImageList?.let { myContext?.let { it2 -> PostViewPagerAdapter(it, it2) } }


        ReviewId.setOnClickListener{
            //게시물 댓글 보기
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_postcomment()
            val bundle = Bundle()
            bundle.putInt("postId", postId)
            bundle.putString("moveFragment", moveFragment)
            fragment.arguments=bundle
            transaction.replace(R.id.container,fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        noticeBtn.setOnClickListener {

            var myId = myContext?.let { PreferenceManager.getInt(it, "userId") }
            if (user_Id == myId) {
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
                    //삭제
                    apiService.deletePostAPI(sharedPreferences.toString(),postId)?.enqueue(object :
                        Callback<Post?> {
                        override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                            Log.i("delete post ", response.body()?.success.toString())

                            if(moveFragment =="meAndPet"){
                                val transaction = myContext!!.supportFragmentManager.beginTransaction()
                                val fragment : Fragment = Fragment_meAndPet()
                                val bundle = Bundle()
                                fragment.arguments=bundle
                                transaction.replace(R.id.container,fragment)
                                transaction.addToBackStack(null)
                                transaction.commit()
                            }
                            else if( moveFragment =="socialPet"){
                                val transaction = myContext!!.supportFragmentManager.beginTransaction()
                                val fragment : Fragment = Fragment_socialPet()
                                val bundle = Bundle()
                                fragment.arguments=bundle
                                transaction.replace(R.id.container,fragment)
                                transaction.addToBackStack(null)
                                transaction.commit()
                            }

                            dialog!!.dismiss()
                        }

                        override fun onFailure(call: Call<Post?>, t: Throwable) {
                            Log.i("delete post: ", "실패")

                            val transaction = myContext!!.supportFragmentManager.beginTransaction()
                            val fragment : Fragment = Fragment_postdetail()
                            val bundle = Bundle()
                            bundle.putInt("postId", postId)
                            bundle.putString("moveFragment", moveFragment)
                            fragment.arguments=bundle
                            transaction.replace(R.id.container,fragment)
                            transaction.addToBackStack(null)
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
                    bundle.putString("moveFragment", moveFragment)
                    bundle.putInt("postId", postId)
                    fragment.arguments = bundle
                    transaction.replace(R.id.container, fragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                    dialog!!.dismiss()
                }
            }


        }

        userImage.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_others()
            val bundle = Bundle()
            bundle.putInt("userId", user_Id!!)
            bundle.putString("userNickname",userId.text.toString())
            fragment.arguments=bundle
            transaction.replace(R.id.container,fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        likeId?.setOnClickListener{
            //게시물 좋아요
            if(liked==0){
                Log.i("post Liked: ","좋아요")
                likeId.setImageResource(R.drawable.liked2)
                liked = 1
                likeCount.text = "${ Integer.parseInt(likeCount.text.toString()) + 1 }"
            }
            else{
                Log.i("post Liked: ","좋아요 취소")
                likeId.setImageResource(R.drawable.unliked2)
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

                    user_Id = response.body()?.postList?.userId
                    context?.let { it1 ->
                        Glide.with(it1)
                            .load(Uri.parse(response.body()?.postList?.postUserImage.toString()))
                            .centerCrop()
                            .circleCrop()
                            .into(userImage)
                    }

                    //viewpager
                    postImageList!!.clear()
                    for (i in 0 until response.body()?.postList?.postImage!!.size){
                        //indicator
                        temp.add(Uri.parse(response.body()?.postList?.postImage!![i]?.postImageUrl.toString()))
                        postImageList!!.add(response.body()?.postList?.postImage!![i]?.postImageUrl.toString())
                    }
                    viewpager.adapter=pagerAdapter
                    pagerAdapter!!.notifyDataSetChanged()

                    indicator.setViewPager(viewpager)
                    indicator.visibility= View.VISIBLE

                    if(postImageList.size==1){
                        indicator.visibility= View.INVISIBLE
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
                        likeId.setImageResource(R.drawable.unliked2)
                        liked = 0
                    }
                    else{
                        //좋아요
                        Log.i("post Liked: ","좋아요")
                        likeId.setImageResource(R.drawable.liked2)
                        liked = 1
                    }
                }

                override fun onFailure(call: Call<Post?>, t: Throwable) {
                    Log.i("post Detail: ","fail")
                }

            })

        var backBtn = view.findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener{
            mainAct.onBackPressed()
        }


        var view_moreText = view.findViewById<TextView>(R.id.view_moreText)
        view_moreText.setOnClickListener{
            postText.maxLines = 100
            view_moreText.visibility =View.GONE
        }

        if(postText.lineCount ==1){
            view_moreText.visibility =View.GONE
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

