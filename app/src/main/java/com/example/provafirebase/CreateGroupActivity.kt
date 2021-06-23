package com.example.provafirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_group.*
import java.util.ArrayList

class CreateGroupActivity : AppCompatActivity() {
    private val mColumnCount = 1
    private val enableStaggeredGrid = false
    private val staggeredGridOrientation = StaggeredGridLayoutManager.VERTICAL
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)
        if (mColumnCount <= 1) {
            viewManager = LinearLayoutManager(this.applicationContext)
            } else {
            if (enableStaggeredGrid) {
                viewManager = StaggeredGridLayoutManager(mColumnCount, staggeredGridOrientation)
                } else {
                viewManager = GridLayoutManager(this, mColumnCount)
                } }

        viewAdapter = UserViewAdapter(loadUsers())

        view_user.apply {
            layoutManager = viewManager
                    adapter = viewAdapter
        }

    }

    fun loadUsers():ArrayList<DummyList.Utente>{
        val lista = ArrayList<DummyList.Utente>()
        var db = FirebaseFirestore.getInstance()
        db.collection("users").get().addOnSuccessListener {
                result ->
            for(document in result) {
                var nuovo = DummyList.Utente(
                    document.data.get("first").toString(),
                    document.data.get("last").toString(),
                    document.id.toString()
                )
                lista.add(nuovo)
            }
            viewAdapter.notifyDataSetChanged()
        }
       //
        return lista

    }
    fun addPerson(view: View){
        //ricerca della persona nel database
        //se non esiste toast con errore
        //se esiste aggiunta alla lista
    }
    fun createGroup(view: View){
        //crea il gruppo e cambia activity
    }
}
