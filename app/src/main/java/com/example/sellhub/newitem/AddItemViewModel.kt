package com.example.sellhub.newitem

import android.net.Uri
import android.view.View
import androidx.lifecycle.ViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
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

    fun getSelectedChipTexts(chipGroup: ChipGroup): List<String> {
        val selectedChipIds = chipGroup.checkedChipIds
        val selectedChipTexts = mutableListOf<String>()

        for (chipId in selectedChipIds) {
            val chip = chipGroup.findViewById<Chip>(chipId)
            chip?.let {
                selectedChipTexts.add(it.text.toString())
            }
        }

        return selectedChipTexts
    }

    fun validateImage(image: Uri?): Boolean {
        return image != null
    }

    fun validateTypeSelection(selectedTypes: List<Int>?): Boolean {
        if (selectedTypes != null) {
            return selectedTypes.isNotEmpty()
        }
        return false
    }

    fun validateText(text: String?): Boolean {
        return !text.isNullOrBlank()
    }


}