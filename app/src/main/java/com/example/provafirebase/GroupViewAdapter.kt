package com.example.provafirebase

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.componente_lista_gruppi.view.*

class GroupViewAdapter(private var mValues: ArrayList<DummyList.Group>) :
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
            group_nameTxt.text = mValues[position].groupName
            //holder.itemView.setOnClickListener { listener(mValues[position])}
        }
        holder.itemView.btShowGroup.setOnClickListener(){
            val intent = Intent(holder.mView.context, CreateGroupActivity::class.java)
            intent.putExtra("docref",mValues[position].docRef.path)
            startActivity(holder.mView.context,intent,null)
        }

    }

}