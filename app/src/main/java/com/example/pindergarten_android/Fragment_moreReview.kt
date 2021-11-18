package com.example.pindergarten_android

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Fragment_moreReview : Fragment() {

    private var myContext: FragmentActivity? = null

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    private lateinit var callback: OnBackPressedCallback

    var pindergartenId : Int ?=null
    var current_latitude :Double ?=null
    var current_longitude :Double ?=null
    var moved : String ?=null
    var query : String ?=null

    var blogTitle = ArrayList<String>()
    var blogLink = ArrayList<String>()
    var blogDescription = ArrayList<String>()
    var blogPostdate = ArrayList<String>()

    val adapter = pindergartenBlogAdapter2(blogTitle,blogDescription,blogPostdate,this)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_morereview,container,false)

        //navigate hide
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavigation(true)

        var recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        var layoutManager = LinearLayoutManager(container?.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()


        var bundle: Bundle
        if(arguments!=null){
            bundle = arguments as Bundle
            pindergartenId = bundle.getInt("pindergartenId")
            Log.i("pindergartenId", pindergartenId.toString())
            if(bundle.getDouble("latitude")!=null){
                current_latitude = bundle.getDouble("latitude")
            }
            if(bundle.getDouble("longitude")!=null){
                current_longitude = bundle.getDouble("longitude")
            }
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


        var backBtn = view.findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_detailPindergarten()
            val bundle = Bundle()
            bundle.putInt("pindergartenId",pindergartenId!!)
            if(moved!=null){
                bundle.putString("moved",moved!!)
            }
            if(query!=null){
                bundle.putString("query",query!!)
            }
            if(current_latitude!=null){
                bundle.putDouble("latitude", current_latitude!!)
            }
            if(current_longitude!=null){
                bundle.putDouble("longitude", current_longitude!!)
            }
            fragment.arguments=bundle
            transaction.replace(R.id.container,fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        //블로그 api
        apiService.pindergartenBlogAPI(pindergartenId!!)?.enqueue(object : Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {

                blogTitle.clear()
                blogDescription.clear()
                blogLink.clear()
                blogPostdate.clear()

                for( i in 0 until response.body()?.blogReviews!!.size ){

                    blogTitle.add(response.body()?.blogReviews!![i].title.toString())
                    blogDescription.add(response.body()?.blogReviews!![i].content.toString())
                    blogLink.add(response.body()?.blogReviews!![i].link.toString())
                    blogPostdate.add(response.body()?.blogReviews!![i].date.toString())
                }

                adapter.notifyDataSetChanged()
                Log.i("blog review", "성공")
            }

            override fun onFailure(call: Call<Post?>, t: Throwable) {
                Log.i("blog review 실패", t.toString())
            }

        })

        //blog link
        adapter.setItemClickListener(object: pindergartenBlogAdapter2.ItemClickListener {
            override fun onClick(view: View, position: Int) {
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse(blogLink[position]))
                startActivity(intent)
            }

        })

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


    }



}