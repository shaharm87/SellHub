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
import com.example.sellhub.services.StorageService

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
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        setCardsData()
    }

    private fun setCardsData() {
        viewModel.getItems { success, items ->
            if (success) {
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
    }
}