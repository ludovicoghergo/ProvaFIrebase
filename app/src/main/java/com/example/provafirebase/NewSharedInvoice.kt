package com.example.provafirebase

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.provafirebase.singleGroup.*
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.example.provafirebase.UtenteSel
import com.example.provafirebase.DbDebitoNew
import kotlinx.android.synthetic.main.activity_groups.*
import kotlinx.android.synthetic.main.conjuctive_debt.*
import kotlin.math.max

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class NewSharedInvoice : DialogFragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var spesa: String? = null
    private lateinit var selUser : UtenteSel
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var recicle: RecyclerView
    private  var members = ArrayList<UtenteSelShare>()
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

    fun getMembers(): ArrayList<UtenteSelShare>{
        val db = FirebaseFirestore.getInstance()

        spesa = arguments?.get("param1").toString()

        val dbSpesaRef = db.document("spese/"+spesa)
        dbSpesaRef.get().addOnSuccessListener {
                documentSpesa ->
            val debiti = documentSpesa.get("debiti") as ArrayList<HashMap<String,Any>>
            for (prova in debiti){
                var utenteRef = db.document((prova.get("refUtente") as DocumentReference).path)
                utenteRef.get().addOnSuccessListener {
                        result ->
                    var nuovo =UtenteSelShare(result.get("first").toString(),utenteRef,0)
                    members.add(nuovo)
                    viewAdapter = ConjuctiveViewAdapter(members)

                    recicle.apply {
                        layoutManager = viewManager
                        adapter = viewAdapter
                    }
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
            val binding = inflater.inflate(R.layout.conjuctive_debt, null)
            viewManager = LinearLayoutManager(this.context)
            getMembers()
            viewAdapter = ConjuctiveViewAdapter(members)
            recicle = binding.findViewById<RecyclerView>(R.id.recUsersDebt)
            recicle.apply {
                layoutManager = viewManager
                adapter = viewAdapter
            }

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(binding)
                // Add action buttons
                .setPositiveButton("confirm",
                    DialogInterface.OnClickListener { dialog, id ->
                        var total = binding.findViewById<TextInputEditText>(R.id.totalDebt).text.toString().toFloat()
                        var listaSpesa = (viewAdapter as ConjuctiveViewAdapter).getLista()
                        val db = FirebaseFirestore.getInstance()
                        val dbSpesaRef = db.document("spese/"+spesa)
                        dbSpesaRef.get().addOnSuccessListener { documentSpesa ->
                            var tot = documentSpesa.get("totale").toString().toFloat() + binding.findViewById<TextInputEditText>(R.id.totalDebt).text.toString().toFloat()
                            val debiti = documentSpesa.get("debiti") as ArrayList<HashMap<String, Any>>
                            for(utente in listaSpesa){
                                for(debt in debiti){
                                        if(debt.get("refUtente")?.equals(utente.UserRef) == true){
                                            val percDebt = (total * utente.perc) / 100
                                            var dapag = debt.get("daPagare").toString().toFloat()+percDebt
                                            val nuovo = DbDebitoNew(dapag ,debt.get("pagato").toString().toFloat() ,
                                                debt.get("refUtente") as DocumentReference
                                            )
                                            dbSpesaRef.update("debiti",FieldValue.arrayRemove(debt))
                                            dbSpesaRef.update("debiti",FieldValue.arrayUnion(nuovo))

                                        }
                                }

                            }
                        }

                    })
                .setNegativeButton("cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }
}
class UtenteSelShare(var name: String,var UserRef: DocumentReference,var perc: Int){
    override fun toString(): String {
        return name
    }
}
