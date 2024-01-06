package com.example.sellhub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sellhub.newitem.Item

data class CardData(
    val item: Item
)

class CardAdapter(private val cardList: List<CardData>) :
    RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

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
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    // ViewHolder class
    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Views in your card layout
        val titleTextView: TextView = itemView.findViewById(R.id.card_header)
        val descriptionTextView: TextView = itemView.findViewById(R.id.card_description)
        val displayNameTextView: TextView = itemView.findViewById(R.id.card_user_name)
    }
}