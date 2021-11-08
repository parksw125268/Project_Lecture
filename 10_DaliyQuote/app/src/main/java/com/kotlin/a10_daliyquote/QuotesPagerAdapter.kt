package com.kotlin.a10_daliyquote

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuotesPagerAdapter(
    private val quotes : List<Quote>,
    private val isNameRevealed :  Boolean
) : RecyclerView.Adapter<QuotesPagerAdapter.QuoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        return QuoteViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quote, parent, false))
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.bind(quotes[position],isNameRevealed)
    }

    override fun getItemCount(): Int {
        return quotes.size
    }
    class QuoteViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        private val quoteTextView : TextView = itemView.findViewById(R.id.quoteTextView)
        private val nameTextView : TextView = itemView.findViewById(R.id.nameTextView)
        fun bind(quote:Quote, isNameRevealed: Boolean){
            quoteTextView.text = quote.quote

            if(isNameRevealed){
                nameTextView.text = quote.name
                //recyclerView라서 아랠 처리를 안해주면 어떨때는 안보일수도 있음.
                nameTextView.visibility = View.VISIBLE
            }else{
                nameTextView.visibility = View.GONE
            }
        }
    }
}