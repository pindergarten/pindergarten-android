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
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Fragment_petInfo : Fragment() {

    private var myContext: FragmentActivity? = null
    private lateinit var callback: OnBackPressedCallback

    var backBtn : ImageButton ?=null
    var petId : Int ?=null

    var petCategory : EditText ?=null
    var petInfo : EditText ?=null
    var genderGroup : RadioGroup?=null
    var preventGroup : RadioGroup?=null
    var neuteringGroup : RadioGroup?=null
    var noticeBtn : ImageButton ?=null

    var dialog : AlertDialog ?=null

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_petinfo,container,false)

        //navigate hide
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavigation(true)

        backBtn = view.findViewById(R.id.backBtn)
        backBtn!!.setOnClickListener{
            val transaction = myContext!!.supportFragmentManager.beginTransaction()
            val fragment : Fragment = Fragment_meAndPet()
            transaction.replace(R.id.container,fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        var petNameText : TextView = view.findViewById(R.id.petNameText)
        var petImg : ImageView = view.findViewById(R.id.petImg)
        var petName : EditText = view.findViewById(R.id.petName)

        petCategory = view.findViewById(R.id.petCategory)
        petInfo = view.findViewById(R.id.petInfo)
        genderGroup = view.findViewById(R.id.genderGroup)
        preventGroup= view.findViewById(R.id.preventGroup)
        neuteringGroup= view.findViewById(R.id.neuteringGroup)


        var bundle: Bundle
        if(arguments!=null){
            bundle = arguments as Bundle
            petId = bundle.getInt("petId")
            petNameText.text = bundle.getString("petName")
            petName.setText(bundle.getString("petName"))

            if(bundle.getString("petImgUri")!=null){
                context?.let {
                    Glide.with(it)
                        .load(Uri.parse(bundle.getString("petImgUri")))
                        .centerCrop()
                        .circleCrop()
                        .into(petImg)
                }
            }
        }

        val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }
        Log.i("jwt : ",sharedPreferences.toString())

        //pet 상세정보
        apiService.petDetailAPI(sharedPreferences.toString(),petId!!)?.enqueue(object : Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {

                petCategory!!.setText(response.body()!!.pet.breed.toString())
                petInfo!!.setText(response.body()!!.pet.birth.toString())

                if(response.body()!!.pet.gender== 0){
                    genderGroup!!.check(R.id.female)
                }
                else{
                    genderGroup!!.check(R.id.male)
                }

                if(response.body()!!.pet.vaccination== 0){
                    preventGroup!!.check(R.id.prevent_yes)
                }
                else{
                    preventGroup!!.check(R.id.prevent_no)
                }


                if(response.body()!!.pet.neutering== 0){
                    neuteringGroup!!.check(R.id.neutering_yes)
                }
                else{
                    neuteringGroup!!.check(R.id.neutering_no)
                }

                Log.i("petDetail","성공")
            }

            override fun onFailure(call: Call<Post?>, t: Throwable) {
                Log.i("petDetail 실패: ",t.toString())
            }

        })

        noticeBtn = view.findViewById(R.id.noticeBtn)
        noticeBtn!!.setOnClickListener{
            //삭제하기 기능
            Log.i("삭제하기 기능","!!")
            val builder = AlertDialog.Builder(myContext)
            val view: View = LayoutInflater.from(myContext).inflate(R.layout.pet_delete, null)
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
                apiService.deletePetAPI(sharedPreferences.toString(),petId!!)?.enqueue(object :
                    Callback<Post?> {
                    override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                        Log.i("delete pet ", response.body()?.success.toString())

                        val transaction = myContext!!.supportFragmentManager.beginTransaction()
                        val fragment : Fragment = Fragment_meAndPet()
                        val bundle = Bundle()
                        fragment.arguments=bundle
                        transaction.replace(R.id.container,fragment)
                        transaction.addToBackStack(null)
                        transaction.commit()

                        dialog!!.dismiss()
                    }

                    override fun onFailure(call: Call<Post?>, t: Throwable) {
                        Log.i("delete pet: ", "실패")
                    }

                })

            }
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


}