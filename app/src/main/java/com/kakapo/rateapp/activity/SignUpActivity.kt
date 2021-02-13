package com.kakapo.rateapp.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kakapo.rateapp.R
import com.kakapo.rateapp.firestore.FireStoreClass
import com.kakapo.rateapp.model.User
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()
        setupBtnSignUp()
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_sign_up_activity)
        val actionBar = supportActionBar

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_24dp)

        }

        toolbar_sign_up_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun validateForm(name: String, email: String,nip: String, password: String): Boolean{
        return when{
            TextUtils.isEmpty(name) ->{
                showErrorSnackBar("please enter a name")
                false
            }
            TextUtils.isEmpty(email) ->{
                showErrorSnackBar("please enter an email")
                false
            }
            TextUtils.isEmpty(nip) ->{
                showErrorSnackBar("please enter a NIP")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("please enter a password")
                false
            }

            else -> true
        }
    }

    private fun registerUser(){
        val name: String = et_name_sign_up.text.toString().trim{it <= ' '}
        val email: String = et_email_sign_up.text.toString().trim{it <= ' '}
        val nip: String = et_nip_sign_up.text.toString().trim{it <= ' '}
        val password: String = et_password_sign_up.text.toString().trim{it <= ' '}

        if(validateForm(name, email, nip,password)){
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val registeredEmail = firebaseUser.email!!
                        val user = User(
                            id = firebaseUser.uid,
                            name = name,
                            email = registeredEmail,
                            nip = nip.toLong()
                        )

                        FireStoreClass().registerUser(this@SignUpActivity, user)
                    }else{
                        Toast.makeText(
                            this@SignUpActivity,
                            "Registered failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun setupBtnSignUp(){
        btn_sign_up.setOnClickListener {
            registerUser()
        }
    }

    fun userRegisterSuccess(){
        Toast.makeText(
            this@SignUpActivity,
            "Registered success",
            Toast.LENGTH_SHORT
        ).show()
        hideProgressDialog()
        FirebaseAuth.getInstance()
        finish()
    }
}