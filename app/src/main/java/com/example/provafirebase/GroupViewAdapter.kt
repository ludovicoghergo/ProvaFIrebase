package com.example.provafirebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.componente_lista_gruppi.view.*

class GroupViewAdapter(private var mValues: ArrayList<String>) :
    RecyclerView.Adapter<GroupViewAdapter.ViewHolder>() {
    // Provide a reference to the views for each data item
    class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView)

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = mValues.size

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.componente_lista_gruppi, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mView.apply {
            group_nameTxt.text = mValues[position]
            //holder.itemView.setOnClickListener { listener(mValues[position])}
        }

    }

}