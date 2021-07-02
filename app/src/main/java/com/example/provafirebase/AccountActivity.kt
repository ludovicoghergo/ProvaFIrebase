package com.example.provafirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        supportActionBar?.hide()

        val name: TextView = findViewById(R.id.textView5)
        name.text = SavedPreference.getUsername(this)

        val eMail: TextView = findViewById(R.id.textView7)
        eMail.text = SavedPreference.getEmail(this)

        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Account page"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}