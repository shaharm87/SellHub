package com.example.sellhub.quote
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class QuoteService {
    // fetch the Quotes from the API 
    fun getRandomQuotes(callback: (List<Quote>?) -> Unit) {

        // Create Retrofit instance 
        val retrofit = Retrofit.Builder()
            .baseUrl("https://type.fit/api/")
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create ApiService interface 
        val api = retrofit.create(QuoteInterface::class.java)

        // Make an API call to get random quotes 
        api.getRandomQuotes().enqueue(object : Callback<List<Quote>> {

            // Callback method on successful API response 
            override fun onResponse(
                call: Call<List<Quote>>,
                response: Response<List<Quote>>
            ) {
                if (response.isSuccessful) {
                    val quoteslist: List<Quote>? = response.body()
                    callback(quoteslist)
                } else {
                    callback(null)
                }
            }

            // Callback method on API call failure 
            override fun onFailure(call: Call<List<Quote>>, t: Throwable) {
                callback(null)
            }
        })
    }
} 