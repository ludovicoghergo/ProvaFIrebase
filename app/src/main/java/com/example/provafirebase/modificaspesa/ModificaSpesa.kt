package com.example.provafirebase.modificaspesa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.provafirebase.R
import com.example.provafirebase.singleGroup.SingleSpesaAdapter
import com.example.provafirebase.singleGroup.UtenteAndSpesa
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_modifica_spesa.*
import kotlinx.android.synthetic.main.activity_single_spesa.*

class ModificaSpesa : AppCompatActivity() {
    private lateinit var viewAdapter: ModificaSpesaAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var gruppo = ""
    private var spesa = ""
    private var data = ArrayList<ModificaSpesaAdapter.UtenteConSpesaModifica>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modifica_spesa)
        gruppo = intent.getStringExtra("docref").toString()
        spesa = intent.getStringExtra("spesaref").toString()
        Log.d("msg","$gruppo")
        Log.d("msg","$spesa")
        viewManager= LinearLayoutManager(this.applicationContext)
        viewAdapter = ModificaSpesaAdapter(data)


        modifica_spesa_list.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        loadSpeseAndMembriModifica()
    }

    private fun loadSpeseAndMembriModifica(){
        var db = FirebaseFirestore.getInstance()

        var membriRef = ArrayList<DocumentReference>()
        val actionbar = supportActionBar

        var dbSpesaRef = db.document("spese/"+spesa)
        dbSpesaRef.get().addOnSuccessListener {
                documentSpesa ->
            var debiti = documentSpesa.get("debiti") as ArrayList<HashMap<String,Any>>
            for (prova in debiti){
                var utenteRef = db.document((prova.get("refUtente") as DocumentReference).path)
                utenteRef.get().addOnSuccessListener {
                        result ->
                    data.add(
                        ModificaSpesaAdapter.UtenteConSpesaModifica(
                            result.get("first").toString(),
                            result.get("last").toString(),
                            result.reference,
                            prova.get("daPagare").toString().toFloat(),
                            prova.get("pagato").toString().toFloat()
                        )
                    )
                    viewAdapter = ModificaSpesaAdapter(data)
                    modifica_spesa_list.apply {
                        layoutManager = viewManager
                        adapter = viewAdapter
                    }

                }
            }
        }

    }
    fun confermaModifica(view: View){
        //var firstName: String, var lastName: String, var id: DocumentReference,
        //               var daPagare: Float, var pagato: Float)
        var daSalvare = viewAdapter.getLista()
        var db = FirebaseFirestore.getInstance()
        var dbSpesaRef = db.document("spese/"+spesa)
        var debiti = ArrayList<HashMap<String,Any>>()
        for(debito in daSalvare){
            debiti.add(
                hashMapOf(
                    "daPagare" to debito.daPagare,
                    "pagato" to debito.pagato,
                    "refUtente" to debito.id
                )
            )
        }
        dbSpesaRef.update("debiti", debiti).addOnSuccessListener {
            document ->
            Log.d("fatto", "aggiornato")
        }

    }

}