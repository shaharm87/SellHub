package com.example.sellhub.home

import androidx.lifecycle.ViewModel
import com.example.sellhub.CardAdapter
import com.example.sellhub.CardData
import com.example.sellhub.newitem.Item
import com.example.sellhub.services.StorageService
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
                    val types = data["types"] as? List<String>


                    val item = Item(displayName, title, description, imageId, types)
                    items.add(item)
                }
                callback(true, items)
            }
            .addOnFailureListener { exception ->
            }
    }

    fun createCardList(items: List<Item>, cardList: MutableList<CardData>, adapter: CardAdapter) {
        cardList.clear()
        for (item in items) {
            val cardData = CardData(item, null)
            cardList.add(cardData)
            adapter.notifyItemChanged(cardList.indexOf(cardData))
            if (item.imageId != null) {
                StorageService.downloadImage(cardData.item.imageId.toString()) { success, imageBit ->
                    if (success) {
                        cardData.image = imageBit
                        adapter.notifyItemChanged(cardList.indexOf(cardData))
                    }
                }
            }
        }
    }
}