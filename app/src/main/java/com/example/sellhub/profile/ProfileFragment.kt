package com.example.sellhub.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sellhub.CardAdapter
import com.example.sellhub.CardData
import com.example.sellhub.R
import com.example.sellhub.home.HomeViewModel

class ProfileFragment : Fragment() {
    private lateinit var viewModel: ProfileViewModel
    private val cardList = mutableListOf<CardData>()
    private val adapter = CardAdapter(cardList)

    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        setCardsData()
    }

    private fun setCardsData() {
        viewModel.getItems { success, items ->
            if (success) {
                val newCards = mutableListOf<CardData>();
                for (item in items) {
                    newCards.add(CardData(item, null))
                }
                cardList.clear()
                cardList.addAll(newCards)
                adapter.notifyDataSetChanged()
            }
        }
    }
}