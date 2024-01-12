package com.example.sellhub.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.sellhub.CardAdapter
import com.example.sellhub.CardData
import com.example.sellhub.R
import com.example.sellhub.managers.CacheManager
import com.example.sellhub.newitem.Item
import com.example.sellhub.quote.Quote
import com.example.sellhub.quote.QuoteService

class Home : Fragment() {

    companion object {
        fun newInstance() = Home()
    }
    var position:Int=0
    public lateinit var quoteslist:List<Quote>

    private lateinit var viewModel: HomeViewModel
    private lateinit var cacheManager: CacheManager<Item>
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val cardList = mutableListOf<CardData>()
    private val adapter = CardAdapter(cardList)
    private val cacheKey = "items_list"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        // Initialize views
        var tv_quotes: TextView= view.findViewById(R.id.tv_quotes)
        var tv_author: TextView = view.findViewById(R.id.tv_author)
        var btn_next: ImageView = view.findViewById(R.id.btn_next)
        var btn_previous: ImageView= view.findViewById(R.id.btn_previous)

        // Make API call to get random quotes
        QuoteService().getRandomQuotes(){listquote->
            if (listquote != null) {
                quoteslist=listquote
                tv_quotes.text=quoteslist.get(0).text
                tv_author.text="~ "+quoteslist.get(0).author
            }
        }
        // Button click listeners
        btn_next.setOnClickListener {
            nextQuote(tv_quotes,tv_author)
        }
        btn_previous.setOnClickListener {
            previousQuote(tv_quotes,tv_author)
        }

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        cacheManager = CacheManager(requireContext())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            setCardsData()
        }

        saveCurrentToCache()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getItemsFromCache()
    }

    private fun setCardsData() {
        viewModel.getItems { success, items ->
            if (success) {
                viewModel.createCardList(items, cardList, adapter)
                saveCurrentToCache()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun saveCurrentToCache() {
        if (cardList.isNotEmpty()) {
            cacheManager.saveList(cacheKey, cardList.map { it.item })
        }
    }

    private fun getItemsFromCache() {
        var items = cacheManager.getList(cacheKey, Item::class.java)
        if (items.isEmpty()) {
            setCardsData()
            return
        } else {
            viewModel.createCardList(items, cardList, adapter)
        }
    }

    // Display the next quote
    fun nextQuote(tv_quotes:TextView,tv_author:TextView){
        if(position<quoteslist.size){
            if (position == quoteslist.size - 1) {
                position = 0
            } else {
                position++;
            }
            tv_quotes.text=quoteslist.get(position).text
            tv_author.text="~ "+quoteslist.get(position).author
        }
    }

    // Display the previous quote
    fun previousQuote(tv_quotes:TextView,tv_author:TextView){
        if(position<quoteslist.size){
            if (position == 0) {
                position = quoteslist.size - 1
            } else {
                position--;
            }
            tv_quotes.text=quoteslist.get(position).text
            tv_author.text="~ "+quoteslist.get(position).author
        }
    }
}