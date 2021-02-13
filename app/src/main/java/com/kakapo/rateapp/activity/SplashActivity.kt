package com.kakapo.rateapp.activity

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.kakapo.rateapp.R
import com.kakapo.rateapp.firestore.FireStoreClass
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val typeFace: Typeface = Typeface.createFromAsset(assets, "carbon bl.ttf")
        tv_app_name.typeface = typeFace

        Handler().postDelayed({

            val currentUserID = FireStoreClass().getCurrentUserId()
            if (currentUserID.isNotEmpty()){
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this@SplashActivity, IntroActivity::class.java)
                startActivity(intent)
            }

            finish()

        }, 2500)
    }
}