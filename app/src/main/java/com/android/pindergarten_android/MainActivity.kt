package com.android.pindergarten_android

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.android.pindergarten_android.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private val fragmentSocialpet by lazy{Fragment_socialPet()}
    private val fragmentPindergarten by lazy{Fragment_pindergarten()}
    private val fragmentMeandpet by lazy{Fragment_meAndPet()}

    private final var FINISH_INTERVAL_TIME: Long = 2000
    private var backPressedTime: Long = 0
    var bottomNavigation : BottomNavigationView ?=null

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 0) {
            var tempTime = System.currentTimeMillis()
            var intervalTime = tempTime - backPressedTime
            if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
                super.onBackPressed();
            } else {
                backPressedTime = tempTime
                Toast.makeText(this, "'뒤로' 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
                return
            }
        }
        super.onBackPressed()
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //액션바 제거
        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        bottomNavigation = findViewById(R.id.bottom_navigation)

        //상태바
        val window = window
        window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.WHITE
        initNavigationBar()

    }

    private fun initNavigationBar() {

        bottomNavigation?.run{
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
        if(state) bottomNavigation?.visibility= View.GONE else bottomNavigation?.visibility=View.VISIBLE
    }

    

}
