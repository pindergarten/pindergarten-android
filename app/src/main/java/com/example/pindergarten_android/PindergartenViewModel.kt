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

    fun splashClick() = splash.set("splash_Click")
    fun joinClick() = join.set("join_Click")
    fun loginClick() = login.set("login_Click")

    fun onClick(view: View, view2: View) {
        val context: Context = view.context
        val intent = Intent(context, view2::class.java)
        context.startActivity(intent)
    }
}