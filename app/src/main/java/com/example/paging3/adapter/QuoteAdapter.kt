package com.example.paging3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.paging3.R
import com.example.paging3.data_classes.Result

class QuoteAdapter : PagingDataAdapter<Result, QuoteAdapter.QuoteViewHolder>(QuoteComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)
        return QuoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val quote = getItem(position)
        quote?.let { holder.bind(it) }
    }



    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(quote: Result) {
            itemView.findViewById<TextView>(R.id.textViewQuote).text = quote.content
            itemView.findViewById<TextView>(R.id.textViewAuthor).text = "- ${quote.author}"
        }
    }

    class QuoteComparator : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.content == newItem.content
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }
}

