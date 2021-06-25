package com.example.provafirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            } }
        viewAdapter = AddSpesaAdapter(loadUserFromGroup())

        elenco_utenti.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun loadUserFromGroup(): List<DummyList.Utente> {
        var db = FirebaseFirestore.getInstance()
        var groupRef = db.collection("groups")

        var listaMemb = java.util.ArrayList<DummyList.Utente>()
        var listaRef = java.util.ArrayList<DocumentReference>()
        var listMembri = java.util.ArrayList<HashMap<String, Any>>()

        var filter = groupRef.whereEqualTo("name","NomeGruppo")
        try {
            filter.get().addOnSuccessListener {
                    result ->
                for(document in result) {
                    var docRef = document.reference
                    listMembri = document.data.get("membri") as
                    for (membro in listMembri){
                        var nuovo = DummyList.Utente(
                            document.data.get("first").toString(),
                            document.data.get("last").toString(),
                            document.id.toString()
                        )
                        listaMemb.add(nuovo)
                        listaRef.add(docRef)
                    }

                }

            }
        } catch(e: FirebaseException){
            Toast.makeText(getApplicationContext(), "Errore", Toast.LENGTH_SHORT).show();
        }

        return listaMemb
    }
}