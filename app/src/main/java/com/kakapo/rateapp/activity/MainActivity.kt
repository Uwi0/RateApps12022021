package com.kakapo.rateapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.kakapo.rateapp.R
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBtnSadIcon()
        setupBtnFlatIcon()
        setupBtnSmileIcon()
    }

    private fun setupBtnSadIcon(){
        ib_icon_sad.setOnClickListener {
            Toast.makeText(
                    this@MainActivity,
                    "Sad Face",
                    Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun setupBtnFlatIcon(){
        ib_icon_flat.setOnClickListener{
            Toast.makeText(
                    this@MainActivity,
                    "Flat Face",
                    Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun setupBtnSmileIcon(){
        ib_icon_smile.setOnClickListener {
            Toast.makeText(
                    this@MainActivity,
                    "Smile face",
                    Toast.LENGTH_LONG
            ).show()
        }
    }
}