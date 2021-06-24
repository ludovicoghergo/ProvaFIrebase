package com.example.provafirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.android.synthetic.main.activity_create_group.*
import java.lang.Exception
import java.util.ArrayList

class CreateGroupActivity : AppCompatActivity() {
    private val mColumnCount = 1
    private val enableStaggeredGrid = false
    private val staggeredGridOrientation = StaggeredGridLayoutManager.VERTICAL
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var querytext : EditText
    private var listaMemb =  ArrayList<DummyList.Utente>()
    private var listaRef =  ArrayList<DocumentReference>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)
        querytext = findViewById(R.id.editTextTextPersonName3)

        if (mColumnCount <= 1) {
            viewManager = LinearLayoutManager(this.applicationContext)
            } else {
            if (enableStaggeredGrid) {
                viewManager = StaggeredGridLayoutManager(mColumnCount, staggeredGridOrientation)
                } else {
                viewManager = GridLayoutManager(this, mColumnCount)
                } }

        viewAdapter = UserViewAdapter(loadUsers(""))

        view_user.apply {
            layoutManager = viewManager
                    adapter = viewAdapter
        }

    }

    fun loadUsers(s: String):ArrayList<DummyList.Utente>{
        val lista = ArrayList<DummyList.Utente>()
        return lista
    }

    fun addPerson(view: View){
        var db = FirebaseFirestore.getInstance()
        var usersRef = db.collection("users")
        var groupRef = db.collection("groups")

        Log.d("out", "$querytext.text")
        var filter = usersRef.whereEqualTo("email",querytext.text.toString())
        try {
            filter.get().addOnSuccessListener {
                    result ->
                for(document in result) {
                    var docRef = document.reference
                    var nuovo = DummyList.Utente(
                        document.data.get("first").toString(),
                        document.data.get("last").toString(),
                        document.id.toString()
                    )
                    listaMemb.add(nuovo)
                    listaRef.add(docRef)
                }

                viewAdapter = UserViewAdapter(listaMemb)

                view_user.apply {
                    layoutManager = viewManager
                    adapter = viewAdapter
                }
            }
         } catch(e:FirebaseException){
            Toast.makeText(getApplicationContext(), "Errore utente", Toast.LENGTH_SHORT).show();
         }

    }
    fun createGroup(view: View){
        //crea il gruppo e cambia activity
        var listaMembri = ArrayList<HashMap<String,HashMap<String,Any>>>()
        var db = FirebaseFirestore.getInstance()


        val membroHash = hashMapOf(
            "spese" to hashMapOf<String,DocumentReference>(),
            "user" to SavedPreference.REFERENCE
        )

        val campoMembro = hashMapOf(
            "membro0" to membroHash
        )
        listaMembri.add(campoMembro)


        for(i in 0..listaMemb.size-1){
            val membroHash = hashMapOf(
                "spese" to hashMapOf<String,DocumentReference>(),
                "user" to listaRef.get(i)
            )

            val campoMembro = hashMapOf(
                "membro"+i+1 to membroHash
            )
            listaMembri.add(campoMembro)
        }

        val data = hashMapOf(
            "membri" to listaMembri,
            "name" to "NOME TESTO GRUPPO"
        )

        db.collection("groups")
            .add(data)
            .addOnSuccessListener { documentReference -> Log.d("SUCCESS", "DocumentSnapshot added with ID: " + documentReference.id) }
            .addOnFailureListener { e -> Log.w("ERROR", "Error adding document", e) }
    }
}
