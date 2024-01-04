package com.example.sellhub

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sellhub.managers.UserManager


class MainActivity : AppCompatActivity() {

    private val userManager = UserManager() // Initialize UserManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!userManager.isUserLogged()) {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}