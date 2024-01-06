package com.example.sellhub.home

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
import com.example.sellhub.newitem.Item

class Home : Fragment() {

    companion object {
        fun newInstance() = Home()
    }

    private lateinit var viewModel: HomeViewModel
    private val cardList = mutableListOf<CardData>()
    private val adapter = CardAdapter(cardList)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            }
        })


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        SetCardsData()
    }

    private fun SetCardsData() {
        viewModel.getItems { success, items ->
            if (success) {
                val newCards = mutableListOf<CardData>();
                for (item in items) {
                    newCards.add(CardData(item))
                }
                cardList.clear()
                cardList.addAll(newCards)
                adapter.notifyDataSetChanged()
            }
        }
    }

}