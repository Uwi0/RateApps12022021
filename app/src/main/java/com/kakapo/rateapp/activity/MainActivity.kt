package com.kakapo.rateapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.kakapo.rateapp.R
import com.kakapo.rateapp.firestore.FireStoreClass
import com.kakapo.rateapp.model.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBar()
        nav_view.setNavigationItemSelectedListener(this)
        setupBtnSadIcon()
        setupBtnFlatIcon()
        setupBtnSmileIcon()
        FireStoreClass().signInUser(this@MainActivity)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            doubleBackToExit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_my_profile ->{
                val intent = Intent(this@MainActivity, MyProfileActivity::class.java)
                startActivity(intent)
            }

            R.id.nav_print_document ->{
                Toast.makeText(
                        this@MainActivity,
                        "print",
                        Toast.LENGTH_SHORT
                ).show()
            }
            R.id.nav_sign_out ->{
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@MainActivity, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_main_activity)

        toolbar_main_activity.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        toolbar_main_activity.setNavigationOnClickListener {
           toggleDrawer()
        }
    }

    private fun toggleDrawer(){
        if (drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            drawer_layout.openDrawer(GravityCompat.START)
        }
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

    fun updateNavigationUserDetails(user: User){
        val headerView = nav_view.getHeaderView(0)
        val navUserImage: ImageView = headerView.findViewById(R.id.iv_user_image)
        Glide.with(this@MainActivity)
                .load(user.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(navUserImage)
        val navUsername: TextView = headerView.findViewById(R.id.tv_username)
        navUsername.text = user.name
        Log.i("username", user.name)
    }


}