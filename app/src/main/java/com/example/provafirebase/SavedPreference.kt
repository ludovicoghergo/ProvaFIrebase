package com.example.provafirebase

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.firebase.firestore.DocumentReference

object SavedPreference {


    var EMAIL= "email"
    var USERNAME="username"


    private  fun getSharedPreference(ctx: Context?): SharedPreferences? {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }



    fun getEmail(context: Context)= getSharedPreference(
        context
    )?.getString(EMAIL,"")

    fun setEmail(context: Context, email: String){
        EMAIL=email
    }

    fun setUsername(context: Context, username:String){
       USERNAME=username
    }

    fun getUsername(context: Context) = getSharedPreference(
        context
    )?.getString(USERNAME,"")





}