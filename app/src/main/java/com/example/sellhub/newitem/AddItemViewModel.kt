package com.example.sellhub.newitem

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class AddItemViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection("items")

    fun uploadItemToFirestore(item: Item, callback: (Boolean, String?) -> Unit) {
        collectionRef.add(item)
            .addOnSuccessListener {
                callback(true, null);
            }
            .addOnFailureListener { e ->
                callback(false, e.message);
            }
    }
}