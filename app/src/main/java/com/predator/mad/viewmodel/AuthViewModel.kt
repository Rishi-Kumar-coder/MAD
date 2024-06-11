package com.predator.mad.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.predator.mad.Utils
import com.predator.mad.Constants
import com.predator.mad.models.Users
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow


class AuthViewModal : ViewModel() {

    private var firebaseAuthInstance = FirebaseAuth.getInstance()
    private var firebaseStoreInstance = Firebase.firestore

    val _isLoggedIn = MutableStateFlow<Boolean?>(false)
    val isLoggedIn = _isLoggedIn

    val _isLoggedFailed = MutableStateFlow<Boolean?>(false)
    val isLoggedFailed = _isLoggedFailed

    val _isEmailSent = MutableStateFlow<Boolean?>(false)
    val isEmailSent = _isEmailSent

    fun getAuthInstance():FirebaseAuth {
        if (firebaseAuthInstance==null){
            firebaseAuthInstance = FirebaseAuth.getInstance()

        }
        return firebaseAuthInstance
    }

    fun getFireStoreInstance(): FirebaseFirestore {
        if (firebaseStoreInstance==null){
            firebaseStoreInstance = Firebase.firestore

        }
        return firebaseStoreInstance
    }

    fun registerUser(context: Context,email:String,password:String,name:String,phone:String,standard: String,section:String){
        Utils.showProgress(context,"Creating Account...")
        getAuthInstance().createUserWithEmailAndPassword(email,password).addOnSuccessListener {
            val userModel = Users(uid=getUserId(),name=name,phone=phone,email=email,standard=standard, password = password,section=section)

            getFireStoreInstance().collection(Constants.CollectionStudents).document(userModel.uid).set(userModel).addOnSuccessListener{
                Utils.hideProgressDialog()
                Utils.putStudent(context,userModel)
                _isLoggedIn.value = true

            }

        }.addOnFailureListener{
            Utils.hideProgressDialog()
            Utils.showToast(context,it.message.toString())
        }
    }

    fun logout(){
        getAuthInstance().signOut()
    }

    fun loginUser(context: Context,email: String,password: String){
        Utils.showProgress(context,"Signing In...")

        getAuthInstance().signInWithEmailAndPassword(email,password).addOnSuccessListener{
            _isLoggedIn.value = true
            Utils.hideProgressDialog()
        }
    }

    fun sendPasswordResetEmail(context: Context,email: String) {
        Utils.showProgress(context,"Sending Password Reset Email...")
        getAuthInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Utils.hideProgressDialog()
                    Utils.showToast(context,"Email Sent,  Please Check Your Inbox!")
                    _isEmailSent.value = true
                } else {
                    Utils.hideProgressDialog()
                    Utils.showToast(context,"Error Sending Email, Please Retry...")

                }
            }.addOnFailureListener{
                Utils.hideProgressDialog()
                Utils.showToast(context,it.message.toString())

            }
    }


    fun getUserId():String{
        return getAuthInstance().currentUser?.uid.toString()
    }

    fun isUserIn():Boolean{
        if (getAuthInstance().currentUser!=null){
            return true
        }
        else return false
    }








}