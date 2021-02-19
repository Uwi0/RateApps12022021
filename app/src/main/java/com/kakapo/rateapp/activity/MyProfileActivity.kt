package com.kakapo.rateapp.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kakapo.rateapp.R
import com.kakapo.rateapp.firestore.FireStoreClass
import com.kakapo.rateapp.model.User
import com.kakapo.rateapp.utils.Constants
import kotlinx.android.synthetic.main.activity_my_profile.*
import java.io.IOException

class MyProfileActivity : BaseActivity() {

    companion object{
        private const val READ_STORAGE_PERMISSION_CODE = 1
        private const val PICK_IMAGE_REQUEST_CODE = 2
    }

    private var mSelectedImageFileUri: Uri? = null
    private var mProfileImageURL: String = ""
    private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        setupActionBar()

        FireStoreClass().loadUserData(this)
        setupChangeImageFromStorage()
        setupBtnUpdateProfile()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                showImageChooser()
            }else{
                Toast.makeText(
                    this@MyProfileActivity,
                    "Please give Storage permission",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (
            resultCode == Activity.RESULT_OK &&
            requestCode == PICK_IMAGE_REQUEST_CODE &&
            data!!.data != null
        ){
            mSelectedImageFileUri = data.data

            try{
                Glide.with(this@MyProfileActivity)
                    .load(mSelectedImageFileUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(iv_profile_user_image)
            }catch (e: IOException){
                Log.e("Error MyProfileActivity",
                    "try to populate image failure onActivity Result",
                    e
                )
            }
        }
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_my_profile_activity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_color_white_back_24dp)
            actionBar.title = resources.getString(R.string.my_profile)
        }

        toolbar_my_profile_activity.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    private fun setupChangeImageFromStorage(){
        iv_profile_user_image.setOnClickListener {
            if(
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                showImageChooser()
            }else{
                ActivityCompat.requestPermissions(
                    this@MyProfileActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_CODE
                )
            }
        }
    }

    private fun showImageChooser(){
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    private fun getFileExtension(uri: Uri?): String?{
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    private fun uploadUserImage(){
        showProgressDialog(resources.getString(R.string.please_wait))
        if (mSelectedImageFileUri != null){

            val sRef : StorageReference = FirebaseStorage
                .getInstance()
                .reference
                .child(
                    "USER_IMAGE" +
                    System.currentTimeMillis() +
                    "." + getFileExtension(mSelectedImageFileUri)
                )
            sRef.putFile(mSelectedImageFileUri!!)
                .addOnSuccessListener { task ->
                    Log.i(
                        "Firebase Image URL",
                        task.metadata!!.reference!!.downloadUrl.toString()
                    )

                    task.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                        Log.i("Downloadable Image URL", uri.toString())
                        mProfileImageURL = uri.toString()
                        updateUserProfileData()

                    }
                }
                .addOnFailureListener{
                    Toast.makeText(
                        this@MyProfileActivity,
                        "Upload image Failed",
                        Toast.LENGTH_SHORT
                    ).show()

                    hideProgressDialog()
                }
        }
    }

    private fun setupBtnUpdateProfile(){
        btn_update.setOnClickListener{
            if(mSelectedImageFileUri != null){
                uploadUserImage()
            }else{
                showProgressDialog(resources.getString(R.string.please_wait))
                updateUserProfileData()
            }
        }
    }

    private fun updateUserProfileData(){
        val userHashMap = HashMap<String, Any>()

        if(mProfileImageURL.isNotEmpty() && mProfileImageURL != mUserDetails.image){
            userHashMap[Constants.IMAGE] = mProfileImageURL
        }

        if (et_name.text.toString() != mUserDetails.name){
            userHashMap[Constants.NAME] = et_name.text.toString()
        }

        if(et_mobile.text.toString() != mUserDetails.mobile.toString() &&
                et_mobile.text.toString().isNotEmpty() ){
            userHashMap[Constants.MOBILE] = et_mobile.text.toString().toLong()
        }

        FireStoreClass().updateUserProfileData(this, userHashMap)
    }

    fun setUserDataInUi(user: User){

        mUserDetails = user

        Glide.with(this@MyProfileActivity)
                .load(user.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(iv_profile_user_image)

        et_name.setText(user.name)
        et_email.setText(user.email)
        et_nip.setText(user.nip.toString())
        if(user.mobile != 0L){
            et_mobile.setText(user.mobile.toString())
        }
    }

    fun profileUpdateSuccess(){
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }
}