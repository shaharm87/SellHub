package com.example.sellhub

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sellhub.newitem.Item
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.firestore.FirebaseFirestore

data class CardData(
    var item: Item,
    var image: Bitmap?
)

class CardAdapter(private val cardList: List<CardData>) :
    RecyclerView.Adapter<CardAdapter.CardViewHolder>() {
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("items")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentItem = cardList[position]

        holder.titleTextView.text = currentItem.item.title
        holder.descriptionTextView.text = currentItem.item.description
        holder.displayNameTextView.text = currentItem.item.displayName

        holder.chipGroup.removeAllViews()
        currentItem.item.types?.forEach { type ->
            val chip = Chip(holder.chipGroup.context)
            chip.text = type
            chip.isClickable = false
            chip.isCheckable = false
            setChipIcon(type, chip)
            holder.chipGroup.addView(chip)
        }
        if (currentItem.image != null)
            holder.displayImage.setImageBitmap(currentItem.image)

        // Button click listeners
        holder.deletePostButton.setOnClickListener {
            onDeletePost(currentItem.item.id, position)
        }
        holder.editPostButton.setOnClickListener {
            onEditPost(currentItem.item.id, position)
        }

    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    // ViewHolder class
    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.card_header)
        val descriptionTextView: TextView = itemView.findViewById(R.id.card_description)
        val displayNameTextView: TextView = itemView.findViewById(R.id.card_user_name)
        val displayImage: ImageView = itemView.findViewById(R.id.card_image)
        val chipGroup: ChipGroup = itemView.findViewById(R.id.card_chip_group)
        val deletePostButton: View = itemView.findViewById<View>(R.id.delete_post_button)
        val editPostButton: View = itemView.findViewById<View>(R.id.edit_post_button)
    }

    fun setChipIcon(type: String, chip: Chip) {
        if (type == "Electronics")
            chip.setChipIconResource(R.drawable.electronic)
        if (type == "Games")
            chip.setChipIconResource(R.drawable.games)
        if (type == "Clothing")
            chip.setChipIconResource(R.drawable.clothing)
        if (type == "Jewellery")
            chip.setChipIconResource(R.drawable.diamond)
        if (type == "Furniture")
            chip.setChipIconResource(R.drawable.bed)
    }
    fun onEditPost(itemId: String?, position: Int) {
        val documentRef = itemId?.let { collectionRef.document(it) }
        val updates = hashMapOf<String, Any>(
            "fieldName" to "newValue",
            // Add more fields as needed
        )

//        documentRef?.update(updates)?.addOnSuccessListener {
//            (cardList as MutableList)[position] = itemAfterUpdate
//            notifyItemChanged(position)
//            notifyItemRangeChanged(position, cardList.size)
//                // Update successful
//            }?.addOnFailureListener { e ->
//                // Handle the error
//            }
    }

    private fun onDeletePost(itemId: String?, position: Int) {
        val documentRef = itemId?.let { collectionRef.document(it) }
        documentRef?.delete()?.addOnSuccessListener {
            // Deletion successful
            (cardList as MutableList).removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cardList.size)
        }?.addOnFailureListener { e ->
            // Handle the error
        }
    }
}