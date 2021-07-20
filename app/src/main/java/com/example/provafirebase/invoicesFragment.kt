package com.example.provafirebase

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.provafirebase.modificaspesa.ModificaSpesa
import com.example.provafirebase.singleGroup.SingleSpesaAdapter
import com.example.provafirebase.singleGroup.UtenteAndSpesa
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_invoices.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [invoicesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class invoicesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var viewAdapter: SingleSpesaAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var gruppo = ""
    private var spesa = ""
    private var data = ArrayList<UtenteAndSpesa>()
    private lateinit var recSpesa: RecyclerView

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        viewManager= LinearLayoutManager(this.activity?.applicationContext)
        viewAdapter = SingleSpesaAdapter(data)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view_new =  inflater.inflate(R.layout.fragment_invoices, container, false)
        recSpesa = view_new.findViewById<RecyclerView>(R.id.recycleSpesa)
        recSpesa.layoutManager = viewManager
        recSpesa.adapter = viewAdapter

        val newPayment = view_new.findViewById<TextView>(R.id.newPay_txt)
        val newDebt = view_new.findViewById<TextView>(R.id.newDebt_txt)
        val newSharedInvoice = view_new.findViewById<TextView>(R.id.sharedInvoice_txt)

        newDebt.setOnClickListener(){
            spesa = arguments?.get("param1").toString()
            if(spesa == "null"){
                Toast.makeText(context, "You should select a invoice first!", Toast.LENGTH_SHORT).show();
            }else{
                val newFragment = NewDebtFragment()
                newFragment.arguments = this.arguments
                getFragmentManager()?.let { newFragment.show(it, "change") }
            }

        }

        newPayment.setOnClickListener(){
            spesa = arguments?.get("param1").toString()
            if(spesa == "null"){
                Toast.makeText(context, "You should select a invoice first!", Toast.LENGTH_SHORT).show();
            }else{
                val newFragment = NewPaymentFragment()
                newFragment.arguments = this.arguments
                getFragmentManager()?.let { newFragment.show(it, "change") }
            }

        }

        newSharedInvoice.setOnClickListener(){
            spesa = arguments?.get("param1").toString()
            if(spesa == "null"){
                Toast.makeText(context, "You should select a invoice first!", Toast.LENGTH_SHORT).show();
            }else{
                val newFragment = NewSharedInvoice()
                newFragment.arguments = this.arguments
                getFragmentManager()?.let { newFragment.show(it, "change") }
            }

        }

        if(arguments != null) {
            spesa = this.param1.toString()
            loadSpeseAndMembri()
        }
        return view_new
    }



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



   fun updateInvoice(){
       val db = FirebaseFirestore.getInstance()
       val dbSpesaRef = db.document("spese/"+spesa)

       dbSpesaRef.get().addOnSuccessListener {
               documentSpesa ->
           var debiti = documentSpesa.get("debiti") as ArrayList<HashMap<String,Any>>
           for (debt in debiti){
           }
       }

   }


    private fun loadSpeseAndMembri(){
        var db = FirebaseFirestore.getInstance()
        var membriRef = ArrayList<DocumentReference>()
        var dbSpesaRef = db.document("spese/"+spesa)
        dbSpesaRef.get().addOnSuccessListener {
                documentSpesa ->
            var debiti = documentSpesa.get("debiti") as ArrayList<HashMap<String,Any>>
            for (prova in debiti){
                var utenteRef = db.document((prova.get("refUtente") as DocumentReference).path)
                utenteRef.get().addOnSuccessListener {
                        result ->
                    data.add(
                        UtenteAndSpesa(
                            result.get("first").toString(),
                            result.get("last").toString(),
                            result.reference,
                            prova.get("daPagare").toString().toFloat(),
                            prova.get("pagato").toString().toFloat()
                        ))
                    viewAdapter = SingleSpesaAdapter(data)
                    recSpesa.apply {
                        layoutManager = viewManager
                        adapter = viewAdapter
                    }
                    viewAdapter.notifyDataSetChanged()

                }
            }
        }
    }

}