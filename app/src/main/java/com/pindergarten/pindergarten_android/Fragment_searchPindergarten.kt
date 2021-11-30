package com.pindergarten.pindergarten_android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Fragment_searchPindergarten : Fragment(){

    private var myContext: FragmentActivity? = null

    var pindergartenId = ArrayList<Int>()
    var pindergartenName  = ArrayList<String>()
    var pindergartenAddress = ArrayList<String>()
    val adapter = searchPindergartenAdapter(pindergartenName,pindergartenAddress,this)
    var queryText : String ?=null
    var imm : InputMethodManager ?=null


    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    var current_latitude :Double ?=null
    var current_longitude :Double ?=null

    private lateinit var callback: OnBackPressedCallback
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_searchpindergarten,container,false)

        //navigate hide
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavigation(true)


        var searchView = view.findViewById<SearchView>(R.id.searchView)
        val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }
        Log.i("jwt : ",sharedPreferences.toString())

        var bundle: Bundle
        if(arguments!=null){
            bundle = arguments as Bundle
            current_latitude = bundle.getDouble("latitude")
            current_longitude = bundle.getDouble("longitude")
            Log.i("current_latitude", current_latitude.toString())
            Log.i("current_longitude", current_longitude.toString())

            if(bundle.getString("query")!=null){
                searchView.setQuery(bundle.getString("query"), false)
                queryText = bundle.getString("query")
                apiService.searchPindergartenAPI(sharedPreferences.toString(),queryText!!,current_latitude!!,current_longitude!!)?.enqueue(object :
                    Callback<Post?> {
                    override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                        Log.i("search Pindergarten", "success")

                        pindergartenId.clear()
                        pindergartenName.clear()
                        pindergartenAddress.clear()

                        for( i in 0 until response.body()?.searchpindergartenList!!.size){
                            pindergartenName.add(response.body()?.searchpindergartenList!![i].name.toString())
                            pindergartenAddress.add(response.body()?.searchpindergartenList!![i].address.toString())
                            pindergartenId.add(response.body()?.searchpindergartenList!![i].id!!.toInt())
                        }

                        //keyboard control
                        imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                        imm?.hideSoftInputFromWindow(view.windowToken,0)

                        adapter.notifyDataSetChanged()
                    }

                    override fun onFailure(call: Call<Post?>, t: Throwable) {
                        Log.i("search Pindergarten", "fail")
                    }
                })
            }
        }
        else {
            Log.i("current location", null.toString())
        }


        var recyclerview_main = view.findViewById<RecyclerView>(R.id.recyclerView)
        var recyclerView = recyclerview_main // recyclerview id
        var layoutManager = LinearLayoutManager(container?.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                queryText = query
                //서버: 유치원 검색 (변경필요)
                if (query != null) {
                    apiService.searchPindergartenAPI(sharedPreferences.toString(),query,current_latitude!!,current_longitude!!)?.enqueue(object :
                        Callback<Post?> {
                        override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                            Log.i("search Pindergarten", "success")

                            pindergartenId.clear()
                            pindergartenName.clear()
                            pindergartenAddress.clear()

                            for( i in 0 until response.body()?.searchpindergartenList!!.size){
                                pindergartenName.add(response.body()?.searchpindergartenList!![i].name.toString())
                                pindergartenAddress.add(response.body()?.searchpindergartenList!![i].address.toString())
                                pindergartenId.add(response.body()?.searchpindergartenList!![i].id!!.toInt())
                            }

                            adapter.notifyDataSetChanged()

                            //keyboard
                            imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                            imm?.hideSoftInputFromWindow(view.windowToken,0)
                        }

                        override fun onFailure(call: Call<Post?>, t: Throwable) {
                            Log.i("search Pindergarten", "fail")
                        }
                    })
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })


        adapter.setItemClickListener(object : searchPindergartenAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                Log.i("item click",pindergartenName[position])
                val transaction = myContext!!.supportFragmentManager.beginTransaction()
                val fragment : Fragment = Fragment_detailPindergarten()
                val bundle = Bundle()
                current_latitude?.let { it1 -> bundle.putDouble("latitude", it1) }
                current_longitude?.let { it1 -> bundle.putDouble("longitude", it1) }
                bundle.putString("moved","search")
                bundle.putString("query",queryText)
                bundle.putInt("pindergartenId",pindergartenId[position])
                fragment.arguments=bundle
                transaction.replace(R.id.container,fragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }

        })


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

}