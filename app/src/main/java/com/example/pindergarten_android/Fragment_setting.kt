package com.example.pindergarten_android

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Fragment_setting : Fragment() {

    private var myContext: FragmentActivity? = null

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    var userId : Int ? = null


    private lateinit var callback: OnBackPressedCallback
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_setting,container,false)

        //navigate hide
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavigation(true)

        var bundle: Bundle
        if(arguments!=null){
            bundle = arguments as Bundle
            userId = bundle.getInt("userId")
        }

        var backBtn = view.findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_meAndPet()
            val bundle = Bundle()
            fragment.arguments=bundle
            transaction.replace(R.id.container,fragment)
            transaction.commit()
        }

        var text4 = view.findViewById<TextView>(R.id.text4)
        text4.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_term1()
            val bundle = Bundle()
            fragment.arguments=bundle
            transaction.replace(R.id.container,fragment)
            transaction.commit()
        }

        var text6 = view.findViewById<TextView>(R.id.text6)
        text6.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_term2()
            val bundle = Bundle()
            fragment.arguments=bundle
            transaction.replace(R.id.container,fragment)
            transaction.commit()
        }

        val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }
        Log.i("jwt : ",sharedPreferences.toString())

        var logout : TextView = view.findViewById(R.id.logout)
        logout.setOnClickListener{

            apiService.logoutAPI(sharedPreferences.toString())?.enqueue(object :
                Callback<Post?> {
                override fun onResponse(call: Call<Post?>, response: Response<Post?>) {

                    PreferenceManager.setString(requireContext(), "jwt", null)

                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)

                    Log.i("logout","성공")
                }

                override fun onFailure(call: Call<Post?>, t: Throwable) {
                    Log.i("logout",t.toString())
                }

            })

        }

        var exituser : TextView = view.findViewById(R.id.exituser)
        exituser.setOnClickListener{


            apiService.exitAPI(sharedPreferences.toString(),userId!!)?.enqueue(object :
                Callback<Post?> {
                override fun onResponse(call: Call<Post?>, response: Response<Post?>) {

                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)

                    Log.i("exit","성공")
                }

                override fun onFailure(call: Call<Post?>, t: Throwable) {
                    Log.i("exit",t.toString())
                }

            })



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
    }


}