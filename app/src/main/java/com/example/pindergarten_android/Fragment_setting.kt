package com.example.pindergarten_android

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Fragment_setting : Fragment() {

    private var myContext: FragmentActivity? = null

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site/")
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
            mainAct.onBackPressed()

        }

        var text4 = view.findViewById<TextView>(R.id.text4)
        text4.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_term1()
            val bundle = Bundle()
            fragment.arguments=bundle
            transaction.replace(R.id.container,fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        var text6 = view.findViewById<TextView>(R.id.text6)
        text6.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_term2()
            val bundle = Bundle()
            fragment.arguments=bundle
            transaction.replace(R.id.container,fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }
        Log.i("jwt : ",sharedPreferences.toString())

        var logout : TextView = view.findViewById(R.id.logout)
        logout.setOnClickListener{

            //로그아웃 확인 메세지
            val dialogBuilder = AlertDialog.Builder(requireActivity())
            val view = inflater.inflate(R.layout.join_popup, null)
            var text : TextView = view.findViewById(R.id.text)
            var button : Button = view.findViewById(R.id.button)
            text.text="로그아웃 하시겠습니까?"
            button.text="확인"
            val alertDialog = dialogBuilder.create()
            button.setOnClickListener{


                apiService.logoutAPI(sharedPreferences.toString())?.enqueue(object :
                    Callback<Post?> {
                    override fun onResponse(call: Call<Post?>, response: Response<Post?>) {

                        PreferenceManager.setString(requireContext(), "jwt", null)
                        //fragment popup
                        val fm: FragmentManager = requireActivity().supportFragmentManager
                        for (i in 0 until fm.backStackEntryCount) {
                            fm.popBackStack()
                        }

                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)

                        Log.i("logout","성공")
                    }

                    override fun onFailure(call: Call<Post?>, t: Throwable) {
                        Log.i("logout",t.toString())
                    }

                })
            }
            alertDialog.setView(view)
            alertDialog.show()


        }

        var exituser : TextView = view.findViewById(R.id.exituser)
        exituser.setOnClickListener{

            //탈퇴 확인 메세지
            val dialogBuilder = AlertDialog.Builder(requireActivity())
            val view = inflater.inflate(R.layout.join_popup, null)
            var text : TextView = view.findViewById(R.id.text)
            var button : Button = view.findViewById(R.id.button)
            text.text="정말 탈퇴 하시겠습니까?"
            button.text="확인"
            val alertDialog = dialogBuilder.create()
            button.setOnClickListener{

                apiService.exitAPI(sharedPreferences.toString(),userId!!)?.enqueue(object :
                    Callback<Post?> {
                    override fun onResponse(call: Call<Post?>, response: Response<Post?>) {

                        PreferenceManager.setString(requireContext(), "jwt", null)
                        //fragment popup
                        val fm: FragmentManager = requireActivity().supportFragmentManager
                        for (i in 0 until fm.backStackEntryCount) {
                            fm.popBackStack()
                        }

                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)

                        Log.i("exit","성공")
                    }

                    override fun onFailure(call: Call<Post?>, t: Throwable) {
                        Log.i("exit",t.toString())
                    }

                })

            }
            alertDialog.setView(view)
            alertDialog.show()


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