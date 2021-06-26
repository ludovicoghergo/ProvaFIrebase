package com.example.provafirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_spesa.*
import kotlinx.android.synthetic.main.activity_create_group.*
import java.lang.reflect.Array

class AddSpesa : AppCompatActivity() {
    private val mColumnCount = 1
    private val enableStaggeredGrid = false
    private val staggeredGridOrientation = StaggeredGridLayoutManager.VERTICAL
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var listaSpesa = ArrayList<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_spesa)

        if (mColumnCount <= 1) {
            viewManager = LinearLayoutManager(this.applicationContext)
        } else {
            if (enableStaggeredGrid) {
                viewManager = StaggeredGridLayoutManager(mColumnCount, staggeredGridOrientation)
            } else {
                viewManager = GridLayoutManager(this, mColumnCount)
            }
        }
        viewAdapter = AddSpesaAdapter(loadUserFromGroup())

        elenco_utenti.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun loadUserFromGroup(): List<DummyList.Utente> {
        var db = FirebaseFirestore.getInstance()
        var groupRef = db.collection("groups")
        var userRef = db.collection("users")

        var listaMemb = java.util.ArrayList<DummyList.Utente>()
        var listaRef = java.util.ArrayList<DocumentReference>()
        var listMembri = java.util.ArrayList<HashMap<String, Any>>()

        var filter = groupRef.whereEqualTo("name", "NomeGruppo")
        try {
            filter.get().addOnSuccessListener { result ->
                for (document in result) {
                    var docRef = document.reference
                    listMembri = document.get("membri") as java.util.ArrayList<HashMap<String, Any>>

                    for (membro in listMembri) {
                        listaRef.add(membro.get("user") as DocumentReference)

                    }
                    Log.d("a", "a")
                }
                for (ref in listaRef) {
                    var filterUser = db.document(ref.path)
                    filterUser.get().addOnSuccessListener { result2 ->
                        var nuovo = DummyList.Utente(
                            result2.get("first").toString(),
                            result2.get("last").toString(),
                            result2.id.toString()
                        )
                        listaMemb.add(nuovo)
                        viewAdapter = AddSpesaAdapter(listaMemb)
                        elenco_utenti.apply {
                            layoutManager = viewManager
                            adapter = viewAdapter
                        }
                    }
                }

            }
        } catch (e: FirebaseException) {
            Toast.makeText(applicationContext, "Errore", Toast.LENGTH_SHORT).show()

        }
        return listaMemb
    }
}