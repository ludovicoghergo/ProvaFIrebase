package com.example.provafirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : AppCompatActivity() {
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var user = "ett6Qi6bXW9zNeBjUwOX"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        viewManager = LinearLayoutManager(this.applicationContext)
        viewAdapter = NotificationActivityAdapter(loadNotifiche())
        supportActionBar?.hide()

        notifiche_lista.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun loadNotifiche(): ArrayList<Notifica> {
        var notifiche = ArrayList<Notifica>()
        var db = FirebaseFirestore.getInstance()
        var usersRef = db.collection("users").document(user)
        usersRef.get().addOnSuccessListener {
            document ->
            var notificheRef = document.get("notifiche") as ArrayList<DocumentReference>
            for (notifica in notificheRef){
                db.document(notifica.path).get().addOnSuccessListener{
                    documentNotifica ->
                    notifiche.add(
                        Notifica(
                            documentNotifica.get("userRef").toString(),
                            documentNotifica.get("msg").toString()
                    ))
                    viewAdapter = NotificationActivityAdapter(notifiche)
                    notifiche_lista.apply {
                        layoutManager = viewManager
                        adapter = viewAdapter
                    }
                }

            }
        }

        return notifiche
    }


}