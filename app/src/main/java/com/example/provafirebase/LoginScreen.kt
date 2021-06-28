package com.example.provafirebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.provafirebase.singleGroup.SingleGroupActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore


class LoginScreen : AppCompatActivity() {
    lateinit var mGoogleSignInClient: GoogleSignInClient
    var Req_Code:Int=123
    var firebaseAuth= FirebaseAuth.getInstance()
    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("191928539014-jcu4ojgc28lkske4gkbigds54q0b1k06.apps.googleusercontent.com")
            .requestEmail()
            .build()
        // getting the value of gso inside the GoogleSigninClient
        mGoogleSignInClient= GoogleSignIn.getClient(this, gso)
        // initialize the firebaseAuth variable
        firebaseAuth = FirebaseAuth.getInstance()


    }
    //skipLogin
    fun skipLogin(view: View){
        SavedPreference.setEmail(this, "adolfo.celi22@gmail.com")
        SavedPreference.setUsername(this, "Adolfo Celi")
        val intent = Intent(this, SingleGroupActivity::class.java)
        startActivity(intent)
    }

    fun loadCreateGroup(view: View){
        SavedPreference.setEmail(this, "Mattia@email.it")
        SavedPreference.setUsername(this, "Mattia Zito")
        val intent = Intent(this, CreateGroupActivity::class.java)
        startActivity(intent)
    }

    // signInGoogle() function
     fun  signInGoogle(AndroidView: View){

        val signInIntent:Intent=mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)
    }
    // onActivityResult() function : this is where we provide the task and data for the Google Account
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==Req_Code){
            val task:Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }
    // handleResult() function -  this is where we update the UI after Google signin takes place
    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? =completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    // UpdateUI() function - this is where we specify what UI updation are needed after google signin has taken place.
    private fun UpdateUI(account: GoogleSignInAccount){
        val credential= GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task->
            if(task.isSuccessful) {
                val user: MutableMap<String, Any> = HashMap()
                user["first"] = account.displayName.toString()
                user["last"] = account.familyName.toString()
                user["email"] = account.email.toString()

                db.collection("users")
                        .add(user)
                        .addOnSuccessListener {}
                        .addOnFailureListener { e -> Log.w("ERROR", "Error adding document", e) }

                SavedPreference.setEmail(this, account.email.toString())
                SavedPreference.setUsername(this, account.displayName.toString())
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }


    override fun onStart() {
        super.onStart()
        if(GoogleSignIn.getLastSignedInAccount(this)!=null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


}