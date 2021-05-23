package com.example.provafirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.provafirebase.databinding.ActivityLoginScreenBinding.inflate
import com.example.provafirebase.databinding.ActivityMainBinding
import com.example.provafirebase.databinding.ActivityMainBinding.inflate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    // declare the GoogleSignInClient
    lateinit var mGoogleSignInClient: GoogleSignInClient
    // val auth is initialized by lazy
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("191928539014-jcu4ojgc28lkske4gkbigds54q0b1k06.apps.googleusercontent.com")
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso)


    }

    fun openMyNotifications(view: View){
        val intent = Intent(this, NotificationActivity::class.java)
        startActivity(intent)
    }

    fun openMyAccount(view: View){
        val intent = Intent(this, AccountActivity::class.java)
        startActivity(intent)
    }

    fun openMyGroups(view: View){
        val intent = Intent(this, GroupsActivity::class.java)
        startActivity(intent)
    }

    fun logOut(view: View){
            mGoogleSignInClient.signOut().addOnCompleteListener {
                val intent= Intent(this, LoginScreen::class.java)
                startActivity(intent)
                finish()
            }
        }
}