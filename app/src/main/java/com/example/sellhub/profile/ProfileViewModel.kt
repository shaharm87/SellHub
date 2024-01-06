package com.example.sellhub.profile

import androidx.lifecycle.ViewModel
import com.example.sellhub.newitem.Item
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection("items")

    fun getItems(callback: (Boolean, List<Item>) -> Unit) {
        collectionRef.get()
            .addOnSuccessListener { documents ->
                val items = mutableListOf<Item>()
                for (document in documents) {
                    val data = document.data
                    val id = document.id
                    val displayName = data["displayName"] as String
                    val title = data["title"] as String
                    val description = data["description"] as String

                    val item = Item(displayName, title, description)
                    items.add(item)
                }
                callback(true, items)
            }
            .addOnFailureListener { exception ->
            }
    }
}