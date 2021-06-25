package com.example.provafirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_create_group.*
import java.util.ArrayList

class GroupsActivity : AppCompatActivity() {
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var querytext : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)
        querytext = findViewById(R.id.editTextTextPersonName3)
        viewManager = LinearLayoutManager(this.applicationContext)
        viewAdapter = GroupViewAdapter(loadGroups())

        view_user.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    fun newGroup(view: View){
        val intent = Intent(this, GroupsActivity::class.java)
        startActivity(intent)
    }

    fun loadGroups(): ArrayList<DummyList.Utente> {
        val lista = ArrayList<DummyList.Utente>()
        return lista
    }

}