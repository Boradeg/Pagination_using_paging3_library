package com.example.paging3.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paging3.adapter.QuoteAdapter
import com.example.paging3.QuotePagingSource
import com.example.paging3.connection.QuoteService
import com.example.paging3.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(loadState: LoadState) {
            // Implement UI logic for the header based on loadState if needed
        }

        companion object {
            fun create(parent: ViewGroup): HeaderViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_header, parent, false)
                return HeaderViewHolder(view)
            }
        }
    }

    // ViewHolder for Footer
    class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(loadState: LoadState) {
            // Implement UI logic for the footer based on loadState if needed
        }

        companion object {
            fun create(parent: ViewGroup): FooterViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_footer, parent, false)
                return FooterViewHolder(view)
            }
        }
    }

    // HeaderAdapter
    class HeaderAdapter : LoadStateAdapter<HeaderViewHolder>() {
        override fun onBindViewHolder(holder: HeaderViewHolder, loadState: LoadState) {
            holder.bind(loadState)
        }

        override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): HeaderViewHolder {
            return HeaderViewHolder.create(parent)
        }
    }

    // FooterAdapter
    class FooterAdapter : LoadStateAdapter<FooterViewHolder>() {
        override fun onBindViewHolder(holder: FooterViewHolder, loadState: LoadState) {
            holder.bind(loadState)
        }

        override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): FooterViewHolder {
            return FooterViewHolder.create(parent)
        }
    }
    private lateinit var quoteAdapter: QuoteAdapter
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        quoteAdapter = QuoteAdapter()

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = quoteAdapter.withLoadStateHeaderAndFooter(
                header = HeaderAdapter(),
                footer = FooterAdapter()
            )
        }

        val quoteService = Retrofit.Builder()
            .baseUrl("https://api.quotable.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuoteService::class.java)

        val pagingSourceFactory = { QuotePagingSource(quoteService) }

        val pagingConfig = PagingConfig(
            pageSize = 2,
            prefetchDistance = 1,
            enablePlaceholders = false
        )

        val quotes = Pager(
            config = pagingConfig,
            pagingSourceFactory = pagingSourceFactory
        ).liveData

        quotes.observe(this) { pagingData ->
            quoteAdapter.submitData(lifecycle, pagingData)
        }


        quoteAdapter.addLoadStateListener { loadState ->
            // Show ProgressBar when loading data
            progressBar.visibility = if (loadState.refresh is LoadState.Loading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}
