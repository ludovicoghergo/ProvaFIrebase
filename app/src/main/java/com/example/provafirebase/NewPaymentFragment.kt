package com.example.provafirebase

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.provafirebase.singleGroup.*
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.example.provafirebase.UtenteSel
import com.example.provafirebase.DbDebitoNew
import kotlinx.android.synthetic.main.activity_single_group.*
import kotlin.math.max

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class NewPaymentFragment : DialogFragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var spesa: String? = null
    private lateinit var selUser : UtenteSel

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

    fun updateMember(usRef: DocumentReference,payment:Float){
        val db = FirebaseFirestore.getInstance()
        spesa = arguments?.get("param1").toString()
        val dbSpesaRef = db.document("spese/"+spesa)

        dbSpesaRef.get().addOnSuccessListener {
                documentSpesa ->
            val debiti = documentSpesa.get("debiti") as ArrayList<HashMap<String,Any>>
            for (prova in debiti){
                val utenteRef = (prova.get("refUtente") as DocumentReference)
                    if(utenteRef.equals(usRef)){
                        var paid = prova.get("pagato").toString().toFloat()
                        var tobepaid = prova.get("daPagare").toString().toFloat()
                        tobepaid = max(tobepaid - payment,0.toFloat())
                        paid = paid + payment
                        val nuovo = DbDebitoNew(tobepaid, paid,utenteRef)
                        dbSpesaRef.update("debiti", FieldValue.arrayRemove(prova))
                        dbSpesaRef.update("debiti", FieldValue.arrayUnion(nuovo))
                    }

            }
        }

    }

    fun getMembers(): ArrayList<UtenteSel>{
        val db = FirebaseFirestore.getInstance()
        val members = ArrayList<UtenteSel>()
        spesa = arguments?.get("param1").toString()

        val dbSpesaRef = db.document("spese/"+spesa)
        dbSpesaRef.get().addOnSuccessListener {
                documentSpesa ->
            val debiti = documentSpesa.get("debiti") as ArrayList<HashMap<String,Any>>
            for (prova in debiti){
                val utenteRef = db.document((prova.get("refUtente") as DocumentReference).path)
                utenteRef.get().addOnSuccessListener {
                        result ->
                    members.add(
                        UtenteSel(
                            result.get("first").toString(),
                            utenteRef
                        )
                    )
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
            val binding = inflater.inflate(R.layout.new_payment, null)
            binding.findViewById<AutoCompleteTextView>(R.id.menu_payment).setAdapter(adapter)
            var selector = binding.findViewById<AutoCompleteTextView>(R.id.menu_payment)

            selector.setOnItemClickListener { parent, view, position, id ->
                selUser = adapter.getItem(position)!!
            }
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(binding)
                // Add action buttons
                .setPositiveButton("confirm",
                    DialogInterface.OnClickListener { dialog, id ->
                        // sign in the user ...
                        var inputPayment = binding.findViewById<TextInputEditText>(R.id.new_payment)
                        var debt = inputPayment.text.toString().toFloat()
                        updateMember(selUser.UserRef,debt)
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

