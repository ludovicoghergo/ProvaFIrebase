package com.example.provafirebase.singleGroup

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.provafirebase.R
import kotlinx.android.synthetic.main.single_group_spesa.view.*

class SingleGroupSpesaAdapter (private var mValues: ArrayList<DbSpesa>, private var groupRef: String) :
    RecyclerView.Adapter<SingleGroupSpesaAdapter.ViewHolder>() {
    // Provide a reference to the views for each data item
    class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView)

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = mValues.size

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_group_spesa, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mView.apply {
            nomeSpesa.text = mValues.get(position).nomeSpesa
            totaleSpesa.text=" = " + mValues.get(position).totale.toString()
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.mView.context, SingleSpesa::class.java)
                intent.putExtra("docRef",groupRef)
                intent.putExtra("spesaRef", mValues.get(position).id)
                ContextCompat.startActivity(holder.mView.context, intent, null)
            }
        }

    }



}