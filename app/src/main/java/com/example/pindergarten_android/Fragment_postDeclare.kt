package com.example.pindergarten_android

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class Fragment_postDeclare : Fragment() {

    private var myContext: FragmentActivity? = null

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)


    private lateinit var callback: OnBackPressedCallback
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_post_declare,container,false)

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


        var type : Int = 0
        //spinner data
        val items = listOf("","광고성 글","스팸 컨텐츠","욕설/비방/혐오")
        var titleDeclare = view.findViewById<EditText>(R.id.textTitle)
        var declareText = view.findViewById<EditText>(R.id.declareText)
        val spinner: Spinner = view.findViewById(R.id.spinner)

        val adapter = ArrayAdapter(requireContext(),R.layout.support_simple_spinner_dropdown_item, items)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                type = position
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }
        Log.i("jwt : ",sharedPreferences.toString())

        val addDeclareBtn = view.findViewById<Button>(R.id.addDeclareBtn)

        addDeclareBtn.setOnClickListener{
            Log.i("신고 접수 버튼눌림","ok")
            var declare: HashMap<String, String> = HashMap()
            declare["title"] = titleDeclare?.text.toString()
            declare["content"] = declareText?.text.toString()

            Log.i("type",type.toString())
            if(type!=0){
                apiService.declarePostAPI(sharedPreferences.toString(),postId, declare,type)?.enqueue(object :
                    Callback<Post?> {
                    override fun onResponse(call: Call<Post?>, response: Response<Post?>) {

                        //신고 완료 메세지
                        val dialogBuilder = AlertDialog.Builder(requireActivity())
                        val view = inflater.inflate(R.layout.join_popup, null)
                        var text : TextView = view.findViewById(R.id.text)
                        var button : Button = view.findViewById(R.id.button)
                        text.text="신고접수 되었습니다."
                        button.text="확인"
                        val alertDialog = dialogBuilder.create()
                        button.setOnClickListener{
                            val transaction = myContext!!.supportFragmentManager.beginTransaction()
                            val fragment : Fragment = Fragment_postdetail()
                            val bundle = Bundle()
                            bundle.putInt("postId", postId)
                            fragment.arguments=bundle
                            transaction.replace(R.id.container,fragment)
                            transaction.addToBackStack(null)
                            transaction.commit()
                            alertDialog!!.dismiss()
                        }
                        alertDialog.setView(view)
                        alertDialog.show()
                    }

                    override fun onFailure(call: Call<Post?>, t: Throwable) {
                        Log.i("declare",t.stackTraceToString())
                    }

                })
            }


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


}

