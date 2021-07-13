package com.example.provafirebase

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.provafirebase.singleGroup.*
import com.example.provafirebase.singleGroup.DbDebito
import com.example.provafirebase.singleGroup.DbSpesa
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_single_group.*
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class NewDebtFragment : DialogFragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var spesa: String? = null

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment invoicesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            invoicesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun getMembers(): ArrayList<String>{
        val db = FirebaseFirestore.getInstance()
        val members = ArrayList<String>()
        spesa = arguments?.get("param1").toString()

        val dbSpesaRef = db.document("spese/"+spesa)
        dbSpesaRef.get().addOnSuccessListener {
                documentSpesa ->
            val debiti = documentSpesa.get("debiti") as ArrayList<HashMap<String,Any>>
            for (prova in debiti){
                val utenteRef = db.document((prova.get("refUtente") as DocumentReference).path)
                utenteRef.get().addOnSuccessListener {
                        result ->
                    members.add(result.get("first").toString())
                }
            }
        }

        return members
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            val items = getMembers()
            val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
            val binding = inflater.inflate(R.layout.new_debt, null)
            binding.findViewById<AutoCompleteTextView>(R.id.menu_debt).setAdapter(adapter)

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(binding)
                // Add action buttons
                .setPositiveButton("confirm",
                    DialogInterface.OnClickListener { dialog, id ->
                        // sign in the user ...
                        Toast.makeText(this.context, "ciao", Toast.LENGTH_SHORT).show()
                    })
                .setNegativeButton("cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }
}