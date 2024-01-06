package com.example.sellhub

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Profile
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sellhub.home.Home
import com.example.sellhub.managers.UserManager
import com.example.sellhub.newitem.AddItem
import com.example.sellhub.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private val userManager = UserManager() // Initialize UserManager
    private val homeFragment = Home()
    private val addItemFragment = AddItem()
    private val profileFragment = ProfileFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!userManager.isUserLogged()) {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    replaceFragment(homeFragment)
                    true
                }

                R.id.add -> {
                    replaceFragment(addItemFragment)
                    true
                }

                R.id.profile -> {
                    replaceFragment(profileFragment)
                    true
                }

                else -> false
            }
        }
        replaceFragment(homeFragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}