package com.example.provafirebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.add_spesa_utente.view.*
import kotlinx.android.synthetic.main.componente_utente.view.*

class AddSpesaAdapter(private var mValues: List<DummyList.Utente>) :
    RecyclerView.Adapter<AddSpesaAdapter.ViewHolder>() {
    // Provide a reference to the views for each data item
    class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView)

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = mValues.size

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.add_spesa_utente, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mView.apply {
            textView.text = mValues[position].firstName
            //holder.itemView.setOnClickListener { listener(mValues[position])}
        }

    }


}