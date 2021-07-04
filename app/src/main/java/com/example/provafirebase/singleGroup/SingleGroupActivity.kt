package com.example.provafirebase.singleGroup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.provafirebase.*
import com.example.provafirebase.DummyList.db
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import kotlinx.android.synthetic.main.activity_create_group.*
import kotlinx.android.synthetic.main.activity_single_group.*
import java.lang.reflect.Field

class SingleGroupActivity : AppCompatActivity() {
    private lateinit var viewAdapterUtenti: SingleGroupUtenteAdapter
    private lateinit var viewManagerUtenti: RecyclerView.LayoutManager
    private lateinit var viewAdapterSpese: SingleGroupSpesaAdapter
    private lateinit var viewManagerSpese: RecyclerView.LayoutManager
    private var gruppo = ""


    var membriData = ArrayList<Utente>()
    var speseData = ArrayList<DbSpesa>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_group)
        val actionbar = supportActionBar
        actionbar!!.title = "Nome Gruppo"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
        gruppo = intent.getStringExtra("docref").toString()

        viewManagerUtenti = LinearLayoutManager(this.applicationContext)
        viewManagerSpese = LinearLayoutManager(this.applicationContext)

        viewAdapterSpese = SingleGroupSpesaAdapter(speseData, gruppo)
        viewAdapterUtenti = SingleGroupUtenteAdapter(membriData)

        lista_membri.apply {
            layoutManager = viewManagerUtenti
            adapter = viewAdapterUtenti
        }

        lista_spese.apply {
            layoutManager = viewManagerSpese
            adapter = viewAdapterSpese
        }

        loadSpeseAndMembri()


    }

    fun leaveGroup(view: View){
        var groupRef = db.document(gruppo)
        var usersRef = db.collection("users")
        var email = SavedPreference.getEmail(this)
        //elimino utente dal gruppo
        usersRef.whereEqualTo("email",email).get().addOnSuccessListener {
            document ->
            for (doc in document){
                groupRef.update("membri", FieldValue.arrayRemove(doc.reference))
                //elimino gruppo dall'utente
                db.document(doc.reference.path).update("groups", FieldValue.arrayRemove(groupRef))
                //elimino le spese
                groupRef.get().addOnSuccessListener {
                    doc2 ->
                    var spese = doc2.get("spese") as ArrayList<DocumentReference>
                    for (spesa in spese){
                        db.document(spesa.path).get().addOnSuccessListener {
                            docSpesa ->
                            var debiti = docSpesa.get("debiti") as ArrayList<HashMap<String, Any>>
                            for (debito in debiti){
                                if ((debito.get("refUtente") as DocumentReference).path.equals(doc.reference.path)){
                                    db.document(spesa.path).update("debiti", FieldValue.arrayRemove(debito))
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    fun rimuoviUtente(view: View){
        var groupRef = db.document(gruppo)
        var usersRef = db.collection("users")
        //email dell'utente da eliminare
        var email = ""
        //elimino utente dal gruppo
        usersRef.whereEqualTo("email",email).get().addOnSuccessListener {
                document ->
            for (doc in document){
                groupRef.update("membri", FieldValue.arrayRemove(doc.reference))
                //elimino gruppo dall'utente
                db.document(doc.reference.path).update("groups", FieldValue.arrayRemove(groupRef))
                //elimino le spese
                groupRef.get().addOnSuccessListener {
                        doc2 ->
                    var spese = doc2.get("spese") as ArrayList<DocumentReference>
                    for (spesa in spese){
                        db.document(spesa.path).get().addOnSuccessListener {
                                docSpesa ->
                            var debiti = docSpesa.get("debiti") as ArrayList<HashMap<String, Any>>
                            for (debito in debiti){
                                if ((debito.get("refUtente") as DocumentReference).path.equals(doc.reference.path)){
                                    db.document(spesa.path).update("debiti", FieldValue.arrayRemove(debito))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun aggiungiUtente(view: View){
        //email dell'utente da aggiungere
        var email = ""
        var groupRef = db.document(gruppo)
        var usersRef = db.collection("users")
        //elimino utente dal gruppo
        usersRef.whereEqualTo("email",email).get().addOnSuccessListener {
                document ->
            for (doc in document){
                groupRef.update("membri", FieldValue.arrayUnion(doc.reference))
                db.document(doc.reference.path).update("groups", FieldValue.arrayUnion(groupRef))
            }

        }
    }

    fun aggiungiSpesa(view: View){
        val intent = Intent(this, AddSpesa::class.java)
        intent.putExtra("docref", gruppo)
        startActivity(intent)
    }

    private fun loadSpeseAndMembri(){
        var groupRef = db.document(gruppo)

        var membriRef = ArrayList<DocumentReference>()
        var speseRef = ArrayList<DocumentReference>()
        val actionbar = supportActionBar
        groupRef.get().addOnSuccessListener {
            document ->

            actionbar!!.title = document.get("name").toString()
            membriRef = document.get("membri") as ArrayList<DocumentReference>
            for(membroRef in membriRef){
                var dbMembroRef = db.document(membroRef.path)
                dbMembroRef.get().addOnSuccessListener {
                    documentMembro ->
                    membriData.add(Utente(
                        documentMembro.get("first").toString(),
                        documentMembro.get("last").toString(),
                        documentMembro.reference
                    ))
                    Log.d("membri","${membriData.toString()}")
                    viewAdapterUtenti = SingleGroupUtenteAdapter(membriData)
                    lista_membri.apply {
                        layoutManager = viewManagerUtenti
                        adapter = viewAdapterUtenti
                    }

                }
            }

            speseRef = document.get("spese") as ArrayList<DocumentReference>
            for (spesaRef in speseRef){
                var dbSpesaRef = db.document(spesaRef.path)
                dbSpesaRef.get().addOnSuccessListener {
                    documentSpesa ->
                    var debiti = ArrayList<DbDebito>()
                    debiti = documentSpesa.get("debiti") as ArrayList<DbDebito>
                    speseData.add(DbSpesa(
                        documentSpesa.get("nomeSpesa").toString(),
                        documentSpesa.get("totale").toString().toFloat(),
                        debiti,
                        documentSpesa.id
                    ))
                    viewAdapterSpese = SingleGroupSpesaAdapter(speseData, gruppo)
                    lista_spese.apply {
                        layoutManager = viewManagerSpese
                        adapter = viewAdapterSpese
                    }
                }
            }


        }

    }
}
class DbSpesa(var nomeSpesa: String, var totale: Float, var debiti: ArrayList<DbDebito>, var id: String)
class DbDebito(var daPagare: Float, var pagato: Float, var refUtente: DocumentReference)
class Utente(var firstName: String, var lastName: String, var id: DocumentReference )