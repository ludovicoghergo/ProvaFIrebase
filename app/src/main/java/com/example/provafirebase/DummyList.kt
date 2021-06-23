package com.example.provafirebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

object DummyList {

    private val lista = ArrayList<Utente>()
    var db = FirebaseFirestore.getInstance()
    public fun getLista() = lista

    init {
        var pippo = db.collection("users").get()
        db.collection("users").get().addOnSuccessListener {
            result ->
            for(document in result) {
                 var nuovo =  Utente(document.data.get("first").toString(),document.data.get("last").toString(),document.id.toString())
                Log.d("out", "$nuovo")
                lista.add(nuovo)
            }
        }
    }

}