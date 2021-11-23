package com.example.pindergarten_android

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*


class Fragment_addPet : Fragment() {

    private var myContext: FragmentActivity? = null
    private lateinit var callback: OnBackPressedCallback

    var imm : InputMethodManager ?=null
    var backBtn : ImageButton ?=null
    var addPostBtn : ImageButton ?=null
    var petImgBtn : ImageButton ?=null
    var petImg : ImageView?=null
    var petUri : Uri ?=null
    var petName : EditText ?=null

    var tempFile: File? = null

    //Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://pindergarten.site:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(RetrofitAPI::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_addpet,container,false)

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


        petImg = view.findViewById(R.id.petImg)
        petImgBtn = view.findViewById(R.id.petImgBtn)
        petImgBtn!!.setOnClickListener{

            var intent = Intent(Intent.ACTION_PICK)
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            intent.action= Intent.ACTION_GET_CONTENT

            startActivityForResult(intent,200)
        }

        petName = view.findViewById(R.id.petName)
        petName?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if(petName!!.text!=null && petUri!=null){
                    addPostBtn!!.setImageResource(R.drawable.register_btn2)
                }
                else{
                    addPostBtn!!.setImageResource(R.drawable.register_btn)
                }
            }
            override fun afterTextChanged(editable: Editable) {
                if(petName!!.text!=null&& petUri!=null){
                    addPostBtn!!.setImageResource(R.drawable.register_btn2)
                }
                else{
                    addPostBtn!!.setImageResource(R.drawable.register_btn)
                }
            }
        })

        var petCategory : EditText = view.findViewById(R.id.petCategory)
        var petInfo : EditText =view.findViewById(R.id.petInfo)
        var genderGroup : RadioGroup = view.findViewById(R.id.genderGroup)
        var preventGroup : RadioGroup = view.findViewById(R.id.preventGroup)
        var neuteringGroup : RadioGroup = view.findViewById(R.id.neuteringGroup)

        var gender : Int ?=null
        var prevent : Int ?=null
        var neutering : Int ?=null

        genderGroup.setOnCheckedChangeListener{
            group, checkId -> when(checkId){
                R.id.female -> gender = 0
                R.id.male -> gender = 1
            }
        }

        preventGroup.setOnCheckedChangeListener{
                group, checkId -> when(checkId){
            R.id.prevent_yes -> prevent = 0
            R.id.prevent_no -> prevent = 1
            }
        }

        neuteringGroup.setOnCheckedChangeListener{
                group, checkId -> when(checkId){
            R.id.neutering_yes -> neutering = 0
            R.id.neutering_no -> neutering = 1 }
        }

        val sharedPreferences = myContext?.let { PreferenceManager.getString(it,"jwt") }
        Log.i("jwt : ",sharedPreferences.toString())



        addPostBtn = view.findViewById(R.id.addPostBtn)
        addPostBtn!!.setOnClickListener{
            if(petName!!.text!=null && petUri!=null && petCategory!!.text!=null && gender!=null&& prevent!=null&& neutering!=null){

                var pet: HashMap<String, Any> = HashMap()
                pet["name"] = petName!!.text.toString()
                //pet["profile_image"] = petUri!!.toString()
                pet["gender"] = gender!!
                pet["breed"] = petCategory!!.text.toString()
                pet["birth"] = petInfo!!.text.toString()
                pet["vaccination"] = prevent!!
                pet["neutering"] = neutering!!

                var fileBody : RequestBody = RequestBody.create(MediaType.parse("image/jpeg"),tempFile)
                var filepart : MultipartBody.Part = MultipartBody.Part.createFormData("profile_image",tempFile?.getName(),fileBody)

                //서버: 펫 등록
                apiService.addPetAPI(sharedPreferences.toString(),pet,filepart)?.enqueue(object :
                    Callback<Post?> {
                    override fun onResponse(call: Call<Post?>, response: Response<Post?>) {

                        Log.i("addPet","성공")

                        val view2 = View.inflate(context,R.layout.join_popup,null)
                        var text : TextView = view2.findViewById(R.id.text)
                        var button : Button = view2.findViewById(R.id.button)
                        text.text="펫이 정상적으로 등록 되었습니다."
                        button.text="확인"

                        val alertDialog = AlertDialog.Builder(context).create()
                        button.setOnClickListener{
                            alertDialog.dismiss()
                            val transaction = myContext!!.supportFragmentManager.beginTransaction()
                            val fragment : Fragment = Fragment_meAndPet()
                            transaction.replace(R.id.container,fragment)
                            transaction.addToBackStack(null)
                            transaction.commit()

                        }
                        alertDialog.setView(view2)
                        alertDialog.show()

                    }

                    override fun onFailure(call: Call<Post?>, t: Throwable) {
                        Log.i("addPet 실패: ",t.toString())
                    }

                })

            }
            else{
                //
            }

        }


        petInfo.setOnClickListener{
            val mcurrentTime = Calendar.getInstance()
            val year = mcurrentTime.get(Calendar.YEAR)
            val month = mcurrentTime.get(Calendar.MONTH)
            val day = mcurrentTime.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(),R.style.MySpinnerDatePickerStyle,
                { view, year, month, dayOfMonth -> petInfo.setText(String.format("%d-%d-%d", year, month + 1, dayOfMonth)) }, year, month, day)

            datePicker.show()

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

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == 200) {
            data?.data?.let {

                val imageUri: Uri? = data?.data
                if (imageUri != null) {
                    context?.let {
                        Glide.with(it)
                            .load(imageUri)
                            .centerCrop()
                            .circleCrop()
                            .into(petImg!!)
                    }
                }

                if (petName!!.text != null && petUri != null) {
                    addPostBtn!!.setImageResource(R.drawable.register_btn2)
                } else {
                    addPostBtn!!.setImageResource(R.drawable.register_btn)
                }

            }
        }
    }

}