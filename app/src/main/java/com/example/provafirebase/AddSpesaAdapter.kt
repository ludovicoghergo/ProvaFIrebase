package com.example.provafirebase

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.synthetic.main.add_spesa_utente.view.*
import kotlinx.android.synthetic.main.componente_utente.view.*
import java.lang.Exception

class AddSpesaAdapter(private var mValues: List<UtenteConSpesa>) :
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
    //@SuppressLint("SetTextI18n")
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
            textView.text = mValues[position].firstName
            holder.itemView.setOnClickListener { listener(mValues[position])}
            userInvoice.addTextChangedListener(textWatcher)

        }


    }

    public fun getLista(): List<UtenteConSpesa> {
        return mValues;
    }

    private fun listener(utenteConSpesa: AddSpesaAdapter.UtenteConSpesa) {
        Log.d("a","${utenteConSpesa.perc}")
    }

    class UtenteConSpesa(var firstName: String, var lastName: String, var id: DocumentReference , var perc: Int)


}


