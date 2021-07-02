package com.example.provafirebase

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.provafirebase.singleGroup.SingleGroupActivity
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.synthetic.main.componente_lista_gruppi.view.*
import kotlinx.android.synthetic.main.notification_component.view.*

class NotificationActivityAdapter (private var mValues: ArrayList<Notifica>) :
    RecyclerView.Adapter<NotificationActivityAdapter.ViewHolder>() {
    // Provide a reference to the views for each data item
    class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView)

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = mValues.size

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_component, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mView.apply {
            notifica.text = mValues[position].msg
            //holder.itemView.setOnClickListener { listener(mValues[position])}
        }


    }

}

class Notifica (var userRef: String, var msg: String)