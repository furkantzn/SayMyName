package com.example.saymyname.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.saymyname.CustomViewHolderListener
import com.example.saymyname.R
import com.example.saymyname.model.Word

class WordAdapter (private val words: MutableList<Word>,private val listener:CustomViewHolderListener) :
    RecyclerView.Adapter<WordAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            textView = view.findViewById(R.id.tvWord)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.word_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.textView.text = words[position].name
        viewHolder.itemView.setOnClickListener {
            listener.onWordItemClicked(words[position],position)
        }
    }

    fun removeWordFromList(position: Int) {
        words.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position,words.size)
    }

    override fun getItemCount() = words.size

}