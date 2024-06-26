package com.example.sellhub.profile

import androidx.lifecycle.ViewModel
import com.example.sellhub.managers.UserManager
import com.example.sellhub.newitem.Item
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection("items")
    private val userManager = UserManager()

    fun getItems(callback: (Boolean, List<Item>) -> Unit) {
        collectionRef.get()
            .addOnSuccessListener { documents ->
                val items = mutableListOf<Item>()
                for (document in documents) {
                    val data = document.data
                    val id = document.id
                    val displayName = data["displayName"] as? String
                    val userId = data["userId"] as? String
                    val currentUser = userManager.getCurrentUser()
                    if (userId == currentUser?.uid && userId != null) {
                        val title = data["title"] as String
                        val description = data["description"] as String
                        val imageId = data["imageId"] as String?
                        val types = data["types"] as? List<String>


                        val item = Item(displayName, title, description, imageId, types, id,userId)
                        items.add(item)
                    }
                }
                callback(true, items)
            }
            .addOnFailureListener { exception ->
            }
    }

    fun getEmail(): String? {
        return userManager.getUserEmail()
    }

    fun getDisplayName(): String? {
        return userManager.getUserDisplayName()
    }

    fun updateDisplayName(displayName: String, callback: (Boolean, String?) -> Unit) {
        userManager.updateDisplayName(displayName, callback)
    }

    fun logOut() {
        userManager.logOut()
    }
}