package com.example.pindergarten_android

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel


class PindergartenViewModel : ViewModel(){
    val user_id = ObservableField("")
    val splash = ObservableField("")
    val join = ObservableField("")
    val login = ObservableField("")

    init{
        user_id.set("petbyung")
        splash.set("splash_petbyung")
        join.set("join.petbyung")
        login.set("login.petbyung")
    }


}