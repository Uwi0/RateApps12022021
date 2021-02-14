package com.kakapo.rateapp.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.kakapo.rateapp.R
import com.kakapo.rateapp.firestore.FireStoreClass
import com.kakapo.rateapp.model.User
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()
        setupBtnSignIn()
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_sign_in_activity)
        val actionBar = supportActionBar

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_24dp)

        }

        toolbar_sign_in_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun validateForm(email: String, password: String): Boolean{
        return when{
            TextUtils.isEmpty(email) ->{
                showErrorSnackBar("please enter an email")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("please enter a password")
                false
            }
            else -> true
        }
    }

    private fun signInRegisteredUser(){
        val email: String = et_email_sign_in.text.toString().trim{it <= ' '}
        val password: String = et_password_sign_in.text.toString().trim{it <= ' '}

        if(validateForm(email, password)){
            showProgressDialog(resources.getString(R.string.please_wait))
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){ task ->
                    hideProgressDialog()
                    if(task.isSuccessful){
                        Log.d("Sign in", "sign in with email success")

                       FireStoreClass().signInUser(this@SignInActivity)
                    }else{
                        Log.e("Sign in", "sign in email: failure")
                        Toast.makeText(
                            baseContext,
                            "Authentication failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun setupBtnSignIn(){
        btn_sign_in.setOnClickListener {
            signInRegisteredUser()
        }
    }

    fun signInSuccess(user: User){
        hideProgressDialog()
        val intent = Intent(this@SignInActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}