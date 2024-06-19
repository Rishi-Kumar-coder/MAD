package com.predator.mad.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.predator.mad.models.Notification


import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.predator.mad.Constants
import com.predator.mad.Utils
import com.predator.mad.modal.Attendence
import com.predator.mad.models.ChatUser
import com.predator.mad.models.HomeWork
import com.predator.mad.models.Messege
import com.predator.mad.models.Users

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
    val _AdminName = MutableStateFlow<String>("")
    val AdminName = _AdminName.value

    val _Messeges = MutableStateFlow<ArrayList<Messege>>(ArrayList())

    var _homeWork: HomeWork = HomeWork()

    val Notificationuploded = _NotificationUploded
    val imageUriData = _imageUriData.value
    val imageRecieve = _imageRecieve

    val imageUploded = _imageUploded
    val homeWorkUploded = _homeWorkUploded

    private val _isHomeWorkUploaded = MutableLiveData<Boolean>(false)
    var isHomeWorkUploaded = _isHomeWorkUploaded
    fun addHomeWork(context: Context, homeWork: HomeWork) {
        Utils.showProgress(context, "Uploading HomeWork...")
        getFireStoreInstance().collection(Constants.CollectionHomeWork).document(homeWork.uid)
            .set(homeWork).addOnSuccessListener {
                _isHomeWorkUploaded.value = true
                Utils.hideProgressDialog()
            }
    }


    fun getFireStoreInstance(): FirebaseFirestore {
        if (firebaseStoreInstance == null) {
            firebaseStoreInstance = Firebase.firestore

        }
        return firebaseStoreInstance
    }


    fun fetchData(standard: String, section: String, date: String): Flow<ArrayList<HomeWork>> =
        callbackFlow {
            val db = Firebase.firestore.collection("$standard$section")


            db.get()

                .addOnSuccessListener { result ->
                    val dataList = ArrayList<HomeWork>()
                    for (documents in result) {
                        val hw = documents.toObject<HomeWork>()
                        if (hw.date == date) {
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

    fun fetchAttendence(uid: String, month: String, year: String): Flow<ArrayList<Attendence>> =
        callbackFlow {
            val db = Firebase.firestore.collection(Constants.CollectionStudents).document(uid)
                .collection(Constants.Attendence).whereEqualTo("month", month)
                .whereEqualTo("year", year)

            db.get()

                .addOnSuccessListener { result ->
                    val dataList = ArrayList<Attendence>()
                    for (documents in result) {
                        val hw = documents.toObject<Attendence>()
                        dataList.add(hw)

                    }

                    trySend(dataList)

                }.addOnFailureListener { exception ->
                    // Handle data fetching failure
                    Log.w("Firestore", "Error fetching data", exception)
                }
            awaitClose()


        }

    @SuppressLint("SuspiciousIndentation")
    fun fetchNotificatioon(
        standard: String,
        section: String,
        date: String
    ): Flow<ArrayList<Notification>> = callbackFlow {
        val db = Firebase.firestore.collection(Constants.CollectionNotification)
            .whereEqualTo("standard", standard).whereEqualTo("section", section)



        db.get()

            .addOnSuccessListener { result ->
                val dataList = ArrayList<Notification>()
                for (documents in result) {
                    val nt = documents.toObject<Notification>()

                    dataList.add(nt)


                }

                trySend(dataList)

            }.addOnFailureListener { exception ->
                // Handle data fetching failure
                Log.w("Firestore", "Error fetching data", exception)
            }
        awaitClose()


    }

    fun fetchUser(uid: String) {
        FirebaseFirestore.getInstance().collection(Constants.CollectionAdminUser).document(uid).get().addOnSuccessListener {
            val user = it.toObject<Users>()
            _AdminName.value = user!!.name.toString()

        }
    }

    fun fetchMesseges(uid: String){
        getFireStoreInstance().collection(Constants.CollectionMessege).document(uid).get().addOnSuccessListener {
            val messeges = it.toObject<ArrayList<Messege>>()
            _Messeges.value = messeges!!
        }
    }


    @SuppressLint("SuspiciousIndentation")
    fun fetchChatList(uid:String): Flow<ArrayList<ChatUser>> = callbackFlow {
        val id = FirebaseAuth.getInstance().currentUser!!.uid.toString()

        val db =getFireStoreInstance().collection(Constants.CollectionStudents).document(id).collection(Constants.CollectionMessege)



        db.get().addOnSuccessListener{
            var dataList = ArrayList<ChatUser>()
            dataList = it.toObjects(ChatUser::class.java) as ArrayList<ChatUser>
            trySend(dataList)
        }


        awaitClose()


    }






}