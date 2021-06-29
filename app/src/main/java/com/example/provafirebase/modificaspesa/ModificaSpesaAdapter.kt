package com.example.provafirebase.modificaspesa

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.provafirebase.AddSpesaAdapter
import com.example.provafirebase.R
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.synthetic.main.add_spesa_utente.view.*
import kotlinx.android.synthetic.main.modifica_spesa_component.view.*
import java.lang.Exception

class ModificaSpesaAdapter (private var mValues: List<UtenteConSpesaModifica>) :
    RecyclerView.Adapter<ModificaSpesaAdapter.ViewHolder>() {
    // Provide a reference to the views for each data item
    class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView)

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = mValues.size

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.modifica_spesa_component, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    //@SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.mView.apply {
            val textWatcherPagato = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    Log.d("c","b")
                    try {
                        mValues[position].pagato=  pagato_m.text.toString().toFloat()
                    }catch (e: Exception){
                        Log.d("errore null", "errore null")
                    }
                    for (utente in mValues){
                        Log.d("lista pagato= ","${utente.pagato}")
                    }


                }
            }
            val textWatcherDaPagare = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    Log.d("c","b")
                    try {
                        mValues[position].daPagare=  da_pagare_m.text.toString().toFloat()
                    }catch (e: Exception){
                        Log.d("errore null", "errore null")
                    }
                    for (utente in mValues){
                        Log.d("lista da pagare = ","${utente.daPagare}")
                    }


                }
            }
            nome_utente_m.text = mValues[position].firstName
            //holder.itemView.setOnClickListener { listener(mValues[position])}
            pagato_m.setText(mValues[position].pagato.toString())
            da_pagare_m.setText(mValues[position].daPagare.toString())
            pagato_m.addTextChangedListener(textWatcherPagato)
            da_pagare_m.addTextChangedListener(textWatcherDaPagare)


        }


    }

    public fun getLista(): List<UtenteConSpesaModifica> {
        return mValues;
    }



    class UtenteConSpesaModifica(var firstName: String, var lastName: String, var id: DocumentReference,
                         var daPagare: Float, var pagato: Float)


}