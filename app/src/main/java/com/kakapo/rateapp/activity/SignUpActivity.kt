package com.kakapo.rateapp.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.kakapo.rateapp.R
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

    private fun validateForm(name: String, email: String, password: String): Boolean{
        return when{
            TextUtils.isEmpty(name) ->{
                showErrorSnackBar("please enter a name")
                false
            }
            TextUtils.isEmpty(email) ->{
                showErrorSnackBar("please enter an email")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("pleas enter a password")
                false
            }

            else -> true
        }
    }

    private fun registerUser(){
        val name: String = et_name_sign_up.text.toString().trim{it <= ' '}
        val email: String = et_email_sign_up.text.toString().trim{it <= ' '}
        val password: String = et_password_sign_up.text.toString().trim{it <= ' '}

        if(validateForm(name, email, password)){
            Toast.makeText(
                this@SignUpActivity,
                "Now we can register a new user",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupBtnSignUp(){
        btn_sign_up.setOnClickListener {
            registerUser()
        }
    }
}