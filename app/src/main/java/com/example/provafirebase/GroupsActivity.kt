package com.example.provafirebase

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_group.*
import kotlinx.android.synthetic.main.activity_groups.*
import java.util.*

class GroupsActivity : AppCompatActivity() {
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)
        viewManager = LinearLayoutManager(this.applicationContext)
        viewAdapter = GroupViewAdapter(loadGroups())
        welcome_txt.text = "Hi "+SavedPreference.getUsername(this)+"!\n Here's your groups"
        supportActionBar?.hide()

        view_groups.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    fun newGroup(view: View){
        val intent = Intent(this, GroupsActivity::class.java)
        startActivity(intent)
    }

    fun loadGroups(): ArrayList<DummyList.Group> {
        var db = FirebaseFirestore.getInstance()
        var usersRef = db.collection("users")
        var listGroup = ArrayList<DocumentReference>()
        var listNameGroup = ArrayList<DummyList.Group>()

        var email = SavedPreference.getEmail(this)
        var filter = usersRef.whereEqualTo("email",email)
        try {
            filter.get().addOnSuccessListener {
                    result ->
                for(document in result) {
                    if(document.get("groups") != null){
                        listGroup = document.get("groups") as ArrayList<DocumentReference>
                        for (group in listGroup){
                            db.document(group.path).get().addOnSuccessListener {
                                    result ->
                                var nuovo = DummyList.Group(
                                    result.get("name").toString(),
                                    result.reference)
                                listNameGroup.add(nuovo)

                                viewAdapter = GroupViewAdapter(listNameGroup)

                                view_groups.apply {
                                    layoutManager = viewManager
                                    adapter = viewAdapter
                                }
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