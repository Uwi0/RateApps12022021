package com.kakapo.rateapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.kakapo.rateapp.R
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupBtnSignUp()
    }

    private fun setupBtnSignUp(){
        btn_sign_up_intro.setOnClickListener {
            val intent = Intent(this@IntroActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}