package com.example.provafirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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

        viewAdapter = UserViewAdapter(loadUsers(""))

        view_user.apply {
            layoutManager = viewManager
                    adapter = viewAdapter
        }

    }

    fun loadUsers(s: String):ArrayList<DummyList.Utente>{
        val lista = ArrayList<DummyList.Utente>()
        var db = FirebaseFirestore.getInstance()
        var usersRef = db.collection("users")
        var querytext = s.toString()
        var filter = usersRef.whereGreaterThanOrEqualTo("first",querytext)
        filter.get().addOnSuccessListener {
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
        Log.d("out", "$lista")
        return lista

    }
}
