package com.example.paging3

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.paging3.connection.QuoteService
import com.example.paging3.data_classes.Result

class QuotePagingSource(
    private val quoteService: QuoteService
) : PagingSource<Int, Result>() {

    override val keyReuseSupported: Boolean = true

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {
            val prevPage = params.key ?: 1 // Current page
            val nextPage = prevPage - 1 // Calculate previous page

            val response = quoteService.getQuotes(nextPage, params.loadSize)
            val quotes = response.body()?.results ?: emptyList()

            // Return LoadResult.Page with previous and next keys flipped for reverse loading
            LoadResult.Page(
                data = quotes,
                prevKey = if (quotes.isEmpty()) null else nextPage + 1,
                nextKey = if (nextPage == 1) null else nextPage - 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        TODO("Not yet implemented")
    }
}
