package com.predator.mad

import android.R
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import com.predator.mad.models.Users
//import com.gtappdevelopers.kotlingfgproject.ViewPagerAdapter
import com.predator.mad.databinding.ProgressDialogBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

object Utils {

    private var alert : AlertDialog? = null

    fun showProgress(context: Context, message: String) {

        val progress = ProgressDialogBinding.inflate(LayoutInflater.from(context))

        progress.messageTextView.text = message
        alert = AlertDialog.Builder(context).setView(progress.root).setCancelable(true).create()

        alert!!.show()

    }

    fun showToast(context: Context,messege: String){
        Toast.makeText(context,messege,Toast.LENGTH_LONG).show()
    }

    fun checkEmpty(view: TextView):Boolean{
        return (view.text=="")
    }

    fun setError(view: TextView,messege:String){
        view.setError(messege)
    }

    fun isValidEmail(email: String): Boolean {
        val emailPattern = Pattern.compile(
            "[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*"
        )
        val matcher = emailPattern.matcher(email)
        return matcher.matches()
    }

    fun hideProgressDialog(){
        alert?.dismiss()

    }

    fun getCurrentDate(): String {
        val today = LocalDate.now()
        return today.toString()
    }

    fun getCurrentTime(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")  // Change format as needed
        return current.format(formatter)
    }

    fun getCurrentMonth(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MMMM")  // Change format as needed
        return current.format(formatter)
    }

    fun getCurrentYear(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("YY")  // Change format as needed
        return current.format(formatter)
    }

    fun setListAdapter(context: Context,list:List<String>,view:AutoCompleteTextView){
        val adapter
                = ArrayAdapter(context,
            R.layout.simple_list_item_1, list)
        view.setAdapter(adapter)

        view.setOnFocusChangeListener { v, hasFocus ->
            run {
                if (hasFocus) view.showDropDown()
            }
        }

    }

    fun openMultiImagePicker(activity: Activity, requestCode: Int) {
        val intent = Intent.createChooser(
            Intent(Intent.ACTION_GET_CONTENT).setType("image/*").putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true),
            "Select Images"
        )
        // Check if there's an activity to handle the intent
        if (intent.resolveActivity(activity.packageManager) != null) {
            activity.startActivityForResult(intent, requestCode)
        }
    }

//    fun setPagerAdapter(context:Context,viewPager: ViewPager,list:List<Uri>) {
//
//        val adapter = ViewPagerAdapter(context,list)
//        viewPager.adapter = adapter
//
//
//    }


    fun putUser(context: Context,users: Users){
        val sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name",users.name)
        editor.putString("email",users.email)
        editor.putString("phone",users.phone)
        editor.putString("uid",users.uid)
        editor.putString("section",users.section)
        editor.putString("standard",users.standard)
        editor.apply()


    }

    fun putStudent(context: Context,users: Users){
        val sharedPreferences = context.getSharedPreferences("student", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name",users.name)
        editor.putString("email",users.email)
        editor.putString("phone",users.phone)
        editor.putString("uid",users.uid)
        editor.putString("section",users.section)
        editor.putString("standard",users.standard)
        editor.apply()


    }

    fun generateUniqueTimeBasedId(): String {
        val currentTime = System.currentTimeMillis()
        return "$currentTime"
    }
    fun clearStudent(context: Context){
        val sharedPreferences = context.getSharedPreferences("student", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name","")
        editor.putString("email","")
        editor.putString("phone","")
        editor.putString("uid","")
        editor.putString("section","")
        editor.putString("standard","")
        editor.apply()


    }

    fun getUser(context: Context) : Users{
        val sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE)

        val name = sharedPreferences.getString("name","")
        val email = sharedPreferences.getString("email","")
        val phone = sharedPreferences.getString("phone","")
        val uid = sharedPreferences.getString("uid","")
        val section = sharedPreferences.getString("section","")
        val standard = sharedPreferences.getString("standard","")
        val user = Users(uid=uid!!,name=name,email=email,phone=phone,section=section, standard = standard)
        return user
    }

    fun getStudent(context: Context) : Users{
        val sharedPreferences = context.getSharedPreferences("student",Context.MODE_PRIVATE)

        val name = sharedPreferences.getString("name","")
        val email = sharedPreferences.getString("email","")
        val phone = sharedPreferences.getString("phone","")
        val uid = sharedPreferences.getString("uid","")
        val section = sharedPreferences.getString("section","")
        val standard = sharedPreferences.getString("standard","")
        val user = Users(uid=uid!!,name=name,email=email,phone=phone,section=section, standard = standard)
        return user
    }



}