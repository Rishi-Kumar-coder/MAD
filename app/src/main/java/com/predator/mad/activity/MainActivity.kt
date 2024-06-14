package com.predator.mad.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.predator.mad.fragment.ChatsFragment
import com.predator.mad.fragment.HomeFragment
import com.predator.mad.fragment.NotificationFragment
import com.predator.mad.fragment.ProfileFragment
import com.predator.mad.R

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        val btm = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        loadFragment(HomeFragment())
        btm.setOnItemSelectedListener {
            when(it.itemId){
                R.id.homeFragment->{
                    loadFragment(HomeFragment())
                    true

                }
                R.id.notificationFragment->{
                    loadFragment(NotificationFragment())
                    true
                }
                R.id.chatsFragment->{
                    loadFragment(ChatsFragment())
                    true
                }
                R.id.studentsFragment->{
                    loadFragment(ProfileFragment())
                    true
                }

                else -> false
            }
        }
    }

    fun loadFragment(fragment:Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }
}