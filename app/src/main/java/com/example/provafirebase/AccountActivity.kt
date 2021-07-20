package com.example.provafirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text
import java.util.ArrayList

class AccountActivity : AppCompatActivity() {
    lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("191928539014-jcu4ojgc28lkske4gkbigds54q0b1k06.apps.googleusercontent.com")
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso)
        supportActionBar?.hide()

        val name: TextView = findViewById(R.id.userName)
        name.text = SavedPreference.getUsername(this)

        val eMail: TextView = findViewById(R.id.userEmail)
        eMail.text = SavedPreference.getEmail(this)

        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Account page"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        var db = FirebaseFirestore.getInstance()
        var usersRef = db.collection("users")
        var listGroup = ArrayList<DocumentReference>()
        val grouptxt : TextView = findViewById(R.id.groups_txt)
        val updatetxt : TextView = findViewById(R.id.news_txt)
        var email = SavedPreference.getEmail(this)
        var filter = usersRef.whereEqualTo("email",email)
        try {
            filter.get().addOnSuccessListener {
                    result ->
                for(document in result) {
                    if(document.get("groups") != null ) {
                        listGroup = document.get("groups") as ArrayList<DocumentReference>
                        grouptxt.text = listGroup.size.toString()

                    }
                    if(document.get("notifiche")!= null){
                        var notificheRef = document.get("notifiche") as ArrayList<DocumentReference>
                        updatetxt.text = notificheRef.size.toString()
                    }
                }
            }
        }catch(e: FirebaseException){
            Toast.makeText(getApplicationContext(), "Errore utente", Toast.LENGTH_SHORT).show()
        }

    }


    public fun logOut(view: View){
        mGoogleSignInClient.signOut().addOnCompleteListener {
            val intent= Intent(this, LoginScreen::class.java)
            startActivity(intent)
            finish()
        }
    }

    public fun openMyNotifications(view: View){
        val intent = Intent(this, NotificationActivity::class.java)
        startActivity(intent)
    }

    public fun openMyAccount(view: View){
        val intent = Intent(this, AccountActivity::class.java)
        startActivity(intent)
    }

    public fun openMyGroups(view: View){
        val intent = Intent(this, GroupsActivity::class.java)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}