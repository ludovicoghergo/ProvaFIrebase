package com.example.provafirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_group.*
import kotlinx.android.synthetic.main.activity_groups.*
import java.util.ArrayList

class GroupsActivity : AppCompatActivity() {
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)
        viewManager = LinearLayoutManager(this.applicationContext)
        viewAdapter = GroupViewAdapter(loadGroups())

        view_groups.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    fun newGroup(view: View){
        val intent = Intent(this, GroupsActivity::class.java)
        startActivity(intent)
    }

    fun loadGroups(): ArrayList<String> {
        var db = FirebaseFirestore.getInstance()
        var usersRef = db.collection("users")
        var listGroup = ArrayList<DocumentReference>()
        var listNameGroup = ArrayList<String>()


        var filter = usersRef.whereEqualTo("email","mattia@pippo.it")
        try {
            var pippo = filter.get()
            filter.get().addOnSuccessListener {
                    result ->
                for(document in result) {
                    var pippo = document.get("groups")
                    listGroup = document.get("groups") as ArrayList<DocumentReference>
                    for (group in listGroup){
                        db.document(group.path).get().addOnSuccessListener {
                            result ->
                            var pippo = result.get("name")
                            listNameGroup.add(result.get("name").toString())

                            viewAdapter = GroupViewAdapter(listNameGroup)

                            view_groups.apply {
                                layoutManager = viewManager
                                adapter = viewAdapter
                            }
                        }

                    }

                }
            }
        } catch(e: FirebaseException){
            Toast.makeText(getApplicationContext(), "Errore utente", Toast.LENGTH_SHORT).show();
        }
        return listNameGroup
    }

    fun openCreateGroups(view: View){
        val intent = Intent(this, CreateGroupActivity::class.java)
        startActivity(intent)
    }

}