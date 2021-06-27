package com.example.provafirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_spesa.*


class AddSpesa : AppCompatActivity() {
    private val mColumnCount = 1
    private val enableStaggeredGrid = false
    private val staggeredGridOrientation = StaggeredGridLayoutManager.VERTICAL
    private lateinit var viewAdapter: AddSpesaAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager



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

    fun creaSpesa(view: View){
        //crea il gruppo e cambia activity
        var listaSpesa = viewAdapter.getLista()
        var totale = (findViewById(R.id.editTextTextPersonName4) as EditText).text.toString().toFloat()
        var nomeSpesa = (findViewById(R.id.editTextTextPersonName2) as EditText).text.toString()
        var debiti = ArrayList<DbDebito>()
        var dbSpesa = DbSpesa(nomeSpesa, totale, debiti)
        for(utente in listaSpesa){
            var debito= DbDebito(utente.spesa, 0F, utente.id)
            debiti.add(debito)
        }
        var db = FirebaseFirestore.getInstance()
        var speseRef = db.collection("spese")
        speseRef.add(dbSpesa).addOnSuccessListener {
                documentReference -> Log.d("db", "datiInseriti")
        }

    }

    private fun loadUserFromGroup(): List<AddSpesaAdapter.UtenteConSpesa> {
        var db = FirebaseFirestore.getInstance()
        var groupRef = db.collection("groups")
        var userRef = db.collection("users")

        var listaMemb = java.util.ArrayList<AddSpesaAdapter.UtenteConSpesa>()
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
                        var nuovo = AddSpesaAdapter.UtenteConSpesa(
                            result2.get("first").toString(),
                            result2.get("last").toString(),
                            result2.id.toString(),
                            0F
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
class DbSpesa(var nomeSpesa: String, var totale: Float, var debiti: ArrayList<DbDebito>)
class DbDebito(var daPagare: Float, var pagato: Float, var refUtente: String)
