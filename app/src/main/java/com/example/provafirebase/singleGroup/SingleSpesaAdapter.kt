package com.example.provafirebase.singleGroup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.provafirebase.R
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.synthetic.main.single_spesa_component.view.*

class SingleSpesaAdapter (private var mValues: ArrayList<UtenteAndSpesa>) :
    RecyclerView.Adapter<SingleSpesaAdapter.ViewHolder>() {
    // Provide a reference to the views for each data item
    class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView)

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = mValues.size

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_spesa_component, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mView.apply {
            nome.text = (mValues.get(position).firstName)
            pagato.text=mValues.get(position).pagato.toString()
            da_pagare.text=mValues.get(position).daPagare.toString()
        }

    }





}

class UtenteAndSpesa (var firstName: String, var lastName: String, var id: DocumentReference, var daPagare: Float, var pagato: Float)
