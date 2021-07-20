package com.example.provafirebase

import android.R.attr.data
import android.content.Intent
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

    public fun swap(datas: ArrayList<DummyList.Group>) {
        mValues.clear()
        mValues.addAll(datas)
        notifyDataSetChanged()
    }
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mView.apply {
            group_nameTxt.text = mValues[position].groupName
            //holder.itemView.setOnClickListener { listener(mValues[position])}
        }
        holder.itemView.btShowGroup.setOnClickListener() {
            val intent = Intent(holder.mView.context, SingleGroupActivity::class.java)
            intent.putExtra("docref", mValues[position].docRef.path)
            startActivity(holder.mView.context, intent, null)
        }
        holder.itemView.del_icon.setOnClickListener() {
            MaterialAlertDialogBuilder(holder.mView.context)
                .setTitle("Delete Group")
                .setMessage("Once deleted a group it cannot be recovered. \nAre you sure?")
                .setNeutralButton("Cancel") { dialog, which ->
                    // Respond to neutral button press
                }
                .setPositiveButton("Yes") { dialog, which ->
                    var db = FirebaseFirestore.getInstance()
                    var email = SavedPreference.getEmail(holder.mView.context)
                    var filter = db.collection("users").whereEqualTo("email", email)
                    var invoices = ArrayList<DocumentReference>()

                    db.document(mValues[position].docRef.path).get().addOnSuccessListener {
                        result ->
                        for(invoice in result["spese"] as ArrayList<DocumentReference>){
                            db.document(invoice.path).delete()
                        }

                        db.document(mValues[position].docRef.path).delete().addOnSuccessListener {
                            filter.get().addOnSuccessListener { result ->
                                for (document in result) {
                                    document.reference.update(
                                        "groups",
                                        FieldValue.arrayRemove(mValues[position].docRef)
                                    )
                                    Toast.makeText(
                                        holder.mView.context,
                                        "Group Has been deleted!",
                                        Toast.LENGTH_SHORT
                                    ).show()


                                }

                            }

                        }

                    }



                }
                .show()
        }

    }

}