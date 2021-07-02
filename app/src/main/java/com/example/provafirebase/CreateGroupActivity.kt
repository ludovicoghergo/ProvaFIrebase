package com.example.provafirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.google.firebase.firestore.FieldValue
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
    private lateinit var querytext: EditText
    private lateinit var groupName: EditText
    private var listaMemb = ArrayList<DummyList.Utente>()
    private var listaRef = ArrayList<DocumentReference>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)
        querytext = findViewById(R.id.editTextTextPersonName3)
        groupName = findViewById(R.id.editTextTextPersonName)

        supportActionBar?.hide()

        if (mColumnCount <= 1) {
            viewManager = LinearLayoutManager(this.applicationContext)
        } else {
            if (enableStaggeredGrid) {
                viewManager = StaggeredGridLayoutManager(mColumnCount, staggeredGridOrientation)
            } else {
                viewManager = GridLayoutManager(this, mColumnCount)
            }
        }

        viewAdapter = UserViewAdapter(loadUsers())

        view_user.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

    }

    fun loadUsers(): ArrayList<DummyList.Utente> {
        val lista = ArrayList<DummyList.Utente>()
        return lista
    }

    fun addPerson(view: View) {
        var db = FirebaseFirestore.getInstance()
        var usersRef = db.collection("users")
        var groupRef = db.collection("groups")

        Log.d("out", "$querytext.text")
        var filter = usersRef.whereEqualTo("email", querytext.text.toString())
        try {
            filter.get().addOnSuccessListener { result ->
                for (document in result) {
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
        } catch (e: FirebaseException) {
            Toast.makeText(getApplicationContext(), "Errore utente", Toast.LENGTH_SHORT).show();
        }

    }

    fun createGroup(view: View) {
        //crea il gruppo e cambia activity
        var ListMemb = ArrayList<DocumentReference>()
        var ListSpese = ArrayList<DocumentReference>()
        var db = FirebaseFirestore.getInstance()
        var filter = db.collection("users").whereEqualTo("email", SavedPreference.getEmail(this))

        filter.get().addOnSuccessListener { result ->
            val membroHash = result.documents.get(0).reference

            var user0 = db.document(membroHash.path)

            ListMemb.add(membroHash)

            for (i in 0..listaRef.size - 1) {
                val membroHash = listaRef.get(i)
                ListMemb.add(membroHash)
            }
            val data = hashMapOf(
                "membri" to ListMemb,
                "spese" to ListSpese,
                "name" to groupName.text.toString()
            )

            db.collection("groups")
                .add(data)
                .addOnSuccessListener { documentReference ->
                    user0.update("groups", FieldValue.arrayUnion(documentReference))
                    for (i in 0..listaRef.size - 1) {
                        var user = db.document(listaRef.get(i).path)
                        user.update("groups", FieldValue.arrayUnion(documentReference))
                        var notifica = Notifica(listaRef.get(i).path, "sei stato aggiunto al gruppo "+groupName.text.toString())
                        db.collection("notifiche").add(notifica).addOnSuccessListener {
                            documentNotificaRef ->
                            user.update("notifiche", FieldValue.arrayUnion(documentNotificaRef))
                        }
                    }
                }
                .addOnFailureListener { e -> Log.w("ERROR", "Error adding document", e) }

        }



    }

}
