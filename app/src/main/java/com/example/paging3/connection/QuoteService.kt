package com.example.paging3.connection

import com.example.paging3.data_classes.QuotesData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuoteService {
    @GET("/quotes")
    suspend fun getQuotes(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<QuotesData>
}
