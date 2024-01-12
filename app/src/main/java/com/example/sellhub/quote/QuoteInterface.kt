package com.example.sellhub.quote
import retrofit2.Call
import retrofit2.http.GET
interface QuoteInterface {
    // Get request
    @GET("quotes")
    // function that return a List of QuotesModel Object
    fun getRandomQuotes(): Call<List<Quote>>
}