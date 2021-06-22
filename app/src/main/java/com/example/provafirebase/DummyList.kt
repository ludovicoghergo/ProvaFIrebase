package com.example.provafirebase

import android.util.Log
import java.util.ArrayList

object DummyList {

    private val lista = ArrayList<String>()

    fun getLista() = lista

    init {
        lista.add("Ciao")
        lista.add("Salmone")
        lista.add("Pietra")
        lista.add("Ludovico")
        lista.add("Mattia")
    }

}