package com.example.provafirebase.singleGroup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.provafirebase.AddSpesa
import com.example.provafirebase.R
import com.example.provafirebase.modificaspesa.ModificaSpesa
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_single_spesa.*

class SingleSpesa : AppCompatActivity() {
    private lateinit var viewAdapter: SingleSpesaAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var gruppo = ""
    private var spesa = ""
    private var data = ArrayList<UtenteAndSpesa>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_spesa)
        gruppo = intent.getStringExtra("docRef").toString()
        spesa = intent.getStringExtra("spesaRef").toString()
        Log.d("msg","$gruppo")
        Log.d("msg","$spesa")


        viewManager= LinearLayoutManager(this.applicationContext)
        viewAdapter = SingleSpesaAdapter(data)


        spesaList.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        loadSpeseAndMembri()

    }

    fun modificaSpesa(view: View){
        val intent = Intent(this, ModificaSpesa::class.java)
        intent.putExtra("docref", gruppo)
        intent.putExtra("spesaref", spesa)
        startActivity(intent)
    }

    private fun loadSpeseAndMembri(){
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
                        UtenteAndSpesa(
                            result.get("first").toString(),
                            result.get("last").toString(),
                            result.reference,
                            prova.get("daPagare").toString().toFloat(),
                            prova.get("pagato").toString().toFloat()
                    ))
                    viewAdapter = SingleSpesaAdapter(data)
                    spesaList.apply {
                        layoutManager = viewManager
                        adapter = viewAdapter
                    }

                }
            }
        }
    }
}

