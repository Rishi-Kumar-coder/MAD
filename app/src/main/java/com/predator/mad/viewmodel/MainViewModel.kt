package com.predator.mad.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.predator.mad.models.HomeWork


import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.storage
import com.predator.mad.Constants
import com.predator.mad.Utils

import com.predator.mad.models.Notification
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow

class MainViewModel : ViewModel() {

    private var firebaseStoreInstance = Firebase.firestore

    val _imageUriData = MutableStateFlow<List<Uri>?>(null)
    val _imageRecieve = MutableStateFlow<Boolean>(false)
    val _imageUploded = MutableStateFlow<Boolean>(false)
    val _homeWorkUploded = MutableStateFlow<Boolean>(false)
    val _NotificationUploded = MutableStateFlow<Boolean>(false)
    val _homeWorkData = HomeWork()

    var _homeWork : HomeWork = HomeWork()

    val Notificationuploded = _NotificationUploded
    val imageUriData = _imageUriData.value
    val imageRecieve = _imageRecieve

    val imageUploded = _imageUploded
    val homeWorkUploded = _homeWorkUploded

    private val _isHomeWorkUploaded = MutableLiveData<Boolean>(false)
    var isHomeWorkUploaded = _isHomeWorkUploaded
    fun addHomeWork(context: Context ,homeWork: HomeWork){
        Utils.showProgress(context,"Uploading HomeWork...")
        getFireStoreInstance().collection(Constants.CollectionHomeWork).document(homeWork.uid).set(homeWork).addOnSuccessListener{
            _isHomeWorkUploaded.value = true
            Utils.hideProgressDialog()
        }
    }



    fun getFireStoreInstance(): FirebaseFirestore {
        if (firebaseStoreInstance==null){
            firebaseStoreInstance = Firebase.firestore

        }
        return firebaseStoreInstance
    }



    fun getHomeWork(): HomeWork {
        Log.d("rishi2" , "getHomeWork: "+_homeWork.toString())

        return _homeWork
    }








    fun fetchData(standard: String,section:String,date: String):Flow<ArrayList<HomeWork>> = callbackFlow {
        val db = Firebase.firestore.collection("$standard$section")


        db.get()

            .addOnSuccessListener { result ->
                val dataList = ArrayList<HomeWork>()
                for (documents   in result) {
                    val hw = documents.toObject<HomeWork>()
                    if (hw.date == date){
                        dataList.add(hw)
                    }

                }

                trySend(dataList)

            }.addOnFailureListener { exception ->
                // Handle data fetching failure
                Log.w("Firestore", "Error fetching data", exception)
            }
        awaitClose()




    }















}