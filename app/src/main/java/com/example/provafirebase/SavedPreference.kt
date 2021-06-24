package com.example.provafirebase

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.firebase.firestore.DocumentReference

object SavedPreference {


    const val EMAIL= "email"
    const val USERNAME="username"
     var REFERENCE: DocumentReference = TODO()

    private  fun getSharedPreference(ctx: Context?): SharedPreferences? {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    private fun  editor(context: Context, const:String, string: String){
        getSharedPreference(
            context
        )?.edit()?.putString(const,string)?.apply()
    }

    fun getEmail(context: Context)= getSharedPreference(
        context
    )?.getString(EMAIL,"")

    fun setEmail(context: Context, email: String){
        editor(
            context,
            EMAIL,
            email
        )
    }

    fun setUsername(context: Context, username:String){
        editor(
            context,
            USERNAME,
            username
        )
    }

    fun getUsername(context: Context) = getSharedPreference(
        context
    )?.getString(USERNAME,"")

    fun setReference(context: Context, id: DocumentReference) {
        REFERENCE = id
    }



}