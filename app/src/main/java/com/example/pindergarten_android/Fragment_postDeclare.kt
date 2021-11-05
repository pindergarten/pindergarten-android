package com.example.pindergarten_android

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Fragment_postDeclare : Fragment() {

    private var myContext: FragmentActivity? = null

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)



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

        //spinner data
        val items = listOf("","광고성 글","스팸 컨텐츠","욕설/비방/혐오")
        var declareText = view.findViewById<EditText>(R.id.textTitle)
        val spinner: Spinner = view.findViewById(R.id.spinner)

        val adapter = ArrayAdapter(requireContext(),R.layout.support_simple_spinner_dropdown_item, items)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                when(position) {
                    0 -> { }
                    1 -> { declareText.setText(items[0]) }
                    2 -> { declareText.setText(items[1]) }
                    3 -> { declareText.setText(items[2]) }
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val addDeclareBtn = view.findViewById<Button>(R.id.addDeclareBtn)
        addDeclareBtn.setOnClickListener{
            //서버 연결
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
                val fragment : Fragment = Fragment_socialPet()
                val bundle = Bundle()
                bundle.putInt("postId", postId)
                fragment.arguments=bundle
                transaction.replace(R.id.container,fragment)
                transaction.commit()
                alertDialog!!.dismiss()
            }
            alertDialog.setView(view)
            alertDialog.show()
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

