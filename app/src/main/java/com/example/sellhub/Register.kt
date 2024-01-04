package com.example.sellhub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.sellhub.managers.UserManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.lang.Exception

class Register : AppCompatActivity() {
    private val userManager = UserManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun onButtonClick(view: View) {
        try {
            val email = findViewById<TextInputEditText>(R.id.reg_email).text.toString()
            val password = findViewById<TextInputEditText>(R.id.reg_password).text.toString()
            userManager.registerUser(email, password) { isSuccess, errorMessage ->
                if (isSuccess) {
                    Toast.makeText(this, "User registered successfully!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Registration failed: $errorMessage", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        } catch (e: Exception) {
            Log.d("Tag", e.message.toString());
        }
    }
}