package com.example.pindergarten_android

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.pindergarten_android.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val fragmentSocialpet by lazy{Fragment_socialPet()}
    private val fragmentPindergarten by lazy{Fragment_pindergarten()}
    private val fragmentMeandpet by lazy{Fragment_meAndPet()}

    private lateinit var binding : ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.vm = PindergartenViewModel()
        binding.activity =  this@MainActivity

        //액션바 제거
        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()


        //상태바
        val window = window
        window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.WHITE
        initNavigationBar()

    }

    private fun initNavigationBar() {

        binding.bottomNavigation.run{
            setOnNavigationItemSelectedListener{
                when(it.itemId){
                    R.id.socialPet->{
                        changeFragment(fragmentSocialpet)
                    }
                    R.id.pindergarten->{
                        changeFragment(fragmentPindergarten)
                    }
                    R.id.Meandpet->{
                        changeFragment(fragmentMeandpet)
                    }
                }
                true
            }
            selectedItemId = R.id.socialPet
        }

    }


    private fun changeFragment(fragment : Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container,fragment)
            .commit()
    }

    fun HideBottomNavigation(state: Boolean){
        if(state) binding.bottomNavigation.visibility= View.GONE else binding.bottomNavigation.visibility=View.VISIBLE
    }







}
