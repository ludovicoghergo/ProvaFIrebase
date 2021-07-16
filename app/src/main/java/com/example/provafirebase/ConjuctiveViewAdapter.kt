package com.example.provafirebase

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.provafirebase.singleGroup.SingleGroupActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_groups.*
import kotlinx.android.synthetic.main.add_spesa_utente.view.*
import kotlinx.android.synthetic.main.componente_lista_gruppi.view.*
import java.lang.Exception

class ConjuctiveViewAdapter(private var mValues: ArrayList<UtenteSelShare>) :
    RecyclerView.Adapter<ConjuctiveViewAdapter.ViewHolder>() {
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

    public fun getLista():ArrayList<UtenteSelShare>{
        return mValues
    }
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mView.apply {
            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    Log.d("c","b")
                    try {
                        mValues[position].perc =  userInvoice.text.toString().toInt()
                    }catch (e: Exception){
                        Log.d("errore null", "errore null")
                    }



                }
            }
            textView.text = mValues[position].name
            userInvoice.addTextChangedListener(textWatcher)
            //holder.itemView.setOnClickListener { listener(mValues[position])}
        }

    }

}