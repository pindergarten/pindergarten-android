package com.example.pindergarten_android

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class PindergartenViewModel : ViewModel(){
    val splash = ObservableField("")
    val join = ObservableField("")
    val login = ObservableField("")

    init{
        splash.set("splash_petbyung")
        join.set("join.petbyung")
        login.set("login.petbyung")
    }

    fun splashClick() = splash.set("splash_Click")
    fun joinClick() = join.set("join_Click")
    fun loginClick() = login.set("login_Click")

}