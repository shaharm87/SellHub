package com.example.sellhub.home

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.sellhub.newitem.Item
import com.google.firebase.firestore.FirebaseFirestore

class HomeViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection("items")

    fun getItems(callback: (Boolean, List<Item>) -> Unit) {
        collectionRef.get()
            .addOnSuccessListener { documents ->
                val items = mutableListOf<Item>()
                for (document in documents) {
                    val data = document.data
                    val id = document.id
                    val displayName = data["displayName"] as? String
                    val title = data["title"] as String
                    val description = data["description"] as String
                    val imageId = data["imageId"] as? String

                    val item = Item(displayName, title, description, imageId)
                    items.add(item)
                }
                callback(true, items)
            }
            .addOnFailureListener { exception ->
            }
    }
}