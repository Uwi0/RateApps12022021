package com.kakapo.rateapp.firestore

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
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

    fun sigInUser(activity: SignInActivity){
        mFireStore
            .collection(Constants.USER)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)!!

                activity.signInSuccess(loggedInUser)
            }
            .addOnFailureListener { e ->
                Log.e("signInUser function", "error writing document")
            }
    }

    fun getCurrentUserId(): String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }
}