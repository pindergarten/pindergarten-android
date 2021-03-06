package com.android.pindergarten_android

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
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.naver.maps.geometry.LatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Fragment_detailPindergarten : Fragment() {

    private var myContext: FragmentActivity? = null


    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    var pindergartenId :Int?= null
    var current_latitude : Double ?=null
    var current_longitude : Double ?=null
    var moved : String ?=null

    var pindergartenName : TextView ?= null
    var pindergartenAddress : TextView ?= null
    var rating_text : TextView?= null
    var pindergartenRating : RatingBar?= null
    var openingHour:TextView ?= null
    var access_guide : TextView ?= null
    var pindergartenLatLng : LatLng ?=null

    var phoneNum : TextView?= null
    var address : TextView ?= null
    var website : LinearLayout ?= null
    var moreReviewBtn : Button?= null

    var phoneBtn : ImageButton ?= null
    var likeBtn : ImageButton ?= null

    var liked :Int ?=null
    var query : String ?=null

    internal lateinit var viewpager : ViewPager
    var temp = ArrayList<Uri>()
    var pindergartenImageList = ArrayList<String>()

    var blogTitle = ArrayList<String>()
    var blogLink = ArrayList<String>()
    var blogDescription = ArrayList<String>()
    var blogPostdate = ArrayList<String>()

    var dialog : AlertDialog ?=null

    private lateinit var callback: OnBackPressedCallback
    var mainAct : MainActivity ?=null
    val adapter = pindergartenBlogAdapter(blogTitle,blogDescription,blogPostdate,this)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_detail_pindergarten,container,false)

        //navigate hide
        mainAct = activity as MainActivity
        mainAct!!.HideBottomNavigation(true)

        var recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        var layoutManager = LinearLayoutManager(container?.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        var bundle: Bundle
        if(arguments!=null){
            bundle = arguments as Bundle
            pindergartenId = bundle.getInt("pindergartenId")
            current_latitude = bundle.getDouble("latitude")
            current_longitude = bundle.getDouble("longitude")
            Log.i("current_latitude", current_latitude.toString())
            Log.i("current_longitude", current_longitude.toString())
            Log.i("pindergartenId", pindergartenId.toString())
            if(bundle.getString("moved")!=null){
                moved = bundle.getString("moved")
            }
            if(bundle.getString("query")!=null){
                query = bundle.getString("query")
            }
        }
        else {
            Log.i("pindergartenId", null.toString())
        }

        viewpager = view.findViewById(R.id.pindergartenImg)
        val pagerAdapter = pindergartenImageList?.let { myContext?.let { it2 -> PindergartenPagerAdapter(it, it2) } }

        pindergartenName = view.findViewById(R.id.pindergartenName)
        pindergartenAddress = view.findViewById(R.id.pindergartenAddress)
        rating_text = view.findViewById(R.id.rating_text)
        pindergartenRating = view.findViewById(R.id.pindergartenRating)
        openingHour= view.findViewById(R.id.openingHour)
        access_guide = view.findViewById(R.id.access_guide)

        phoneNum = view.findViewById(R.id.phoneNum)
        address = view.findViewById(R.id.address)
        website = view.findViewById(R.id.website)

        moreReviewBtn = view.findViewById(R.id.moreReviewBtn)

        phoneBtn = view.findViewById(R.id.phoneBtn)
        phoneBtn!!.setOnClickListener{

            val builder = AlertDialog.Builder(myContext)
            val view: View = LayoutInflater.from(myContext).inflate(R.layout.call_dialog, null)
            val cancelBtn = view?.findViewById<ImageButton>(R.id.cancelBtn)
            val callBtn = view?.findViewById<ImageButton>(R.id.callBtn)
            val number = view?.findViewById<TextView>(R.id.phoneNum)
            number.text = phoneNum!!.text.toString().replace("-","")
            dialog = builder.create()
            dialog!!.setView(view)
            dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog!!.window!!.setGravity(Gravity.BOTTOM)
            dialog!!.show()

            cancelBtn?.setOnClickListener{
                dialog!!.dismiss()
            }
            callBtn?.setOnClickListener{
                val intent2 = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phoneNum!!.text.toString().replace("-","")}"))
                startActivity(intent2)
                dialog!!.dismiss()
            }
        }

        likeBtn = view.findViewById(R.id.likeBtn)
        val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }

        likeBtn!!.setOnClickListener{
            if(liked==0){
                //????????? ??????
                liked=1
                likeBtn!!.setImageResource(R.drawable.pindergarten_liked)
            }
            else{
                //????????? ??????
                liked=0
                likeBtn!!.setImageResource(R.drawable.pindergarten_unliked)
            }

            //????????? ?????? API
            Log.i("jwt : ",sharedPreferences.toString())
            var temp: HashMap<String, String> = HashMap()
            pindergartenId?.let { it1 ->
                apiService.pindergartenLikeAPI(it1,sharedPreferences.toString(),temp)?.enqueue(object : Callback<Post?> {
                    override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                        Log.i("pindergarten Liked: ","?????? ??????")
                    }

                    override fun onFailure(call: Call<Post?>, t: Throwable) {
                        Log.i("pindergarten Detail: ","fail")
                    }
                })
            }
        }

        var backBtn : ImageButton = view.findViewById(R.id.backBtn)
        backBtn.setOnClickListener{
            mainAct!!.onBackPressed()
        }

        Log.i("jwt : ",sharedPreferences.toString())
        Log.i("????????????", "??????: ${ current_latitude} | ??????: ${ current_longitude}")

        //??????: ?????? ???????????? ??????
        apiService.PindergartenAPI(sharedPreferences.toString(),pindergartenId!!)?.enqueue(object : Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {

                pindergartenName?.text=response.body()?.pindergarten?.name
                pindergartenAddress?.text=response.body()?.pindergarten?.address
                rating_text?.text="${response.body()?.pindergarten?.rating}/5"
                pindergartenRating?.rating= response.body()?.pindergarten?.rating?.toFloat()!!

                if(response.body()?.pindergarten?.opening_hours!=""){
                    openingHour?.text=response.body()?.pindergarten?.opening_hours
                }
                else{
                    openingHour?.text="-"
                }

                if(response.body()?.pindergarten?.access_guide!=""){
                    access_guide?.text=response.body()?.pindergarten?.access_guide
                }
                else{
                    access_guide?.text="-"
                }

                if(response.body()?.pindergarten?.phone!=""){
                    phoneNum?.text=response.body()?.pindergarten?.phone
                }
                else{
                    phoneNum?.text="-"
                }

                if(response.body()?.pindergarten?.address!=""){
                    address?.text=response.body()?.pindergarten?.address
                }
                else{
                    address?.text="-"
                }

                if(response.body()?.pindergarten?.website!=""){

                    response.body()?.pindergarten?.website?.let { Log.i("websiteList", it) }
                    var websiteList = response.body()?.pindergarten!!.website?.split("\n")


                    if (websiteList != null) {
                        for ( i in websiteList ){
                            val textView = TextView(myContext)
                            textView.text = "${i}\n"
                            val viewParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                            viewParams.setMargins(0, 0, 0,changeDP(3))
                            textView.layoutParams = viewParams
                            textView!!.setOnClickListener{
                                var intent = Intent(Intent.ACTION_VIEW,Uri.parse("${i}"))
                                startActivity(intent) }
                            website!!.addView(textView)
                        }
                    }


                }
                else{
                    val textView = TextView(myContext)
                    textView.text = "-"
                    website!!.addView(textView)
                }

                pindergartenLatLng = LatLng(response.body()?.pindergarten!!.latitude!!.toDouble(),response.body()?.pindergarten!!.longitude!!.toDouble())

                //viewpager
                pindergartenImageList!!.clear()
                temp.clear()

                Log.i("pindergarten image",response.body()?.pindergarten?.postImage?.size!!.toString())
                for (i in 0 until response.body()?.pindergarten?.postImage?.size!!){
                    //indicator
                    temp.add(Uri.parse(response.body()?.pindergarten?.postImage!![i]?.postImageUrl.toString()))
                    pindergartenImageList!!.add(response.body()?.pindergarten?.postImage!![i]?.postImageUrl.toString())
                }

                viewpager.adapter=pagerAdapter
                pagerAdapter!!.notifyDataSetChanged()


                var tempLiked = response.body()?.pindergarten?.isLiked
                if(tempLiked ==0){
                    //????????? x
                    Log.i("pindergarten Liked: ","????????? x")
                    likeBtn!!.setImageResource(R.drawable.pindergarten_unliked)
                    liked = 0
                }
                else{
                    //?????????
                    Log.i("pindergarten Liked: ","?????????")
                    likeBtn!!.setImageResource(R.drawable.pindergarten_liked)
                    liked = 1
                }

                Log.i("pindergarten ????????????", "??????")
            }

            override fun onFailure(call: Call<Post?>, t: Throwable) {
                Log.i("pindergarten ????????????", t.toString())
            }

        })


        var review_notexist = view.findViewById<ImageView>(R.id.review_notexist)

        //????????? api
        apiService.pindergartenBlogAPI(pindergartenId!!)?.enqueue(object : Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {

                blogTitle.clear()
                blogDescription.clear()
                blogLink.clear()
                blogPostdate.clear()

                var size: Int
                Log.i("review count",response.body()?.blogReviews!!.size.toString())
                if(response.body()?.blogReviews!!.size >3){
                    size =2
                    moreReviewBtn!!.visibility = View.VISIBLE
                    review_notexist!!.visibility = View.GONE
                    moreReviewBtn!!.text = "${response.body()?.blogReviews!!.size-2}??? ????????? ?????? ????????? "
                }
                else if (response.body()?.blogReviews!!.size==0){
                    size =0
                    moreReviewBtn!!.visibility = View.GONE
                    review_notexist!!.visibility = View.VISIBLE
                }
                else{
                    size = response.body()?.blogReviews!!.size
                    moreReviewBtn!!.visibility = View.GONE
                    review_notexist!!.visibility = View.GONE
                }

                for( i in 0 until size){

                    Log.i("blogTitle",response.body()?.blogReviews!![i].title.toString())
                    Log.i("blogDescription",response.body()?.blogReviews!![i].content.toString())

                    blogTitle.add(response.body()?.blogReviews!![i].title.toString())
                    blogDescription.add(response.body()?.blogReviews!![i].content.toString())
                    blogLink.add(response.body()?.blogReviews!![i].link.toString())
                    blogPostdate.add(response.body()?.blogReviews!![i].date.toString())
                }

                adapter.notifyDataSetChanged()
                Log.i("blog review", "??????")
            }

            override fun onFailure(call: Call<Post?>, t: Throwable) {
                Log.i("blog review ??????", t.toString())
            }

        })

        //blog link
        adapter.setItemClickListener(object:pindergartenBlogAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                var intent = Intent(Intent.ACTION_VIEW,Uri.parse(blogLink[position]))
                startActivity(intent)
            }

        })

        moreReviewBtn?.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_moreReview()
            val bundle = Bundle()
            bundle.putInt("pindergartenId",pindergartenId!!)

            if(moved!=null){
                bundle.putString("moved",moved!!)
            }
            if(query!=null){
                bundle.putString("query",query!!)
            }
            if(current_latitude!=null){
                current_latitude?.let { it1 -> bundle.putDouble("latitude", it1) }
            }
            if(current_longitude!=null){
                current_longitude?.let { it1 -> bundle.putDouble("longitude", it1) }
            }

            fragment.arguments=bundle
            transaction.replace(R.id.container,fragment)
            transaction.addToBackStack(null)
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


    private fun changeDP(value: Int): Int {
        var displayMetrics = requireContext().resources.displayMetrics
        return Math.round(value * displayMetrics.density)
    }



}

