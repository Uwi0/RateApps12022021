package com.kakapo.rateapp.firestore

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kakapo.rateapp.activity.MainActivity
import com.kakapo.rateapp.activity.MyProfileActivity
import com.kakapo.rateapp.activity.SignInActivity
import com.kakapo.rateapp.activity.SignUpActivity
import com.kakapo.rateapp.model.User
import com.kakapo.rateapp.utils.Constants

class FireStoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User){
        mFireStore
            .collection(Constants.USER)
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisterSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "error writing document", e)
            }
    }

    fun loadUserData(activity: Activity) {

        mFireStore.collection(Constants.USER)
                .document(getCurrentUserId())
                .get()
                .addOnSuccessListener { document ->
                    Log.e(activity.javaClass.simpleName, document.toString())

                    val loggedInUser = document.toObject(User::class.java)!!

                    when (activity) {
                        is SignInActivity -> {
                            activity.signInSuccess(loggedInUser)
                        }
                        is MainActivity -> {
                            activity.updateNavigationUserDetails(loggedInUser)
                        }
                        is MyProfileActivity ->{
                            activity.setUserDataInUi(loggedInUser)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    when (activity) {
                        is SignInActivity -> {
                            activity.hideProgressDialog()
                        }
                        is MainActivity -> {
                            activity.hideProgressDialog()
                        }
                    }
                    Log.e(
                            activity.javaClass.simpleName,
                            "Error while getting loggedIn user details",
                            e
                    )
                }
    }

    fun updateUserProfileData(activity: MyProfileActivity, userHashMap: HashMap<String, Any>){
        mFireStore
            .collection(Constants.USER)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                Log.i(
                    activity.javaClass.simpleName,
                    "Profile data updated 'updateUserProfile'"
                )
                Toast.makeText(
                    activity,
                    "Profile updated successfully",
                    Toast.LENGTH_SHORT
                ).show()
                activity.profileUpdateSuccess()
            }.addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while creating a board 'updateUserProfileData'",
                    e
                )
                Toast.makeText(
                    activity,
                    "update profile Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    fun getCurrentUserId(): String{

        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if (currentUser != null){
            currentUserId = currentUser.uid
        }
        return currentUserId
    }
}