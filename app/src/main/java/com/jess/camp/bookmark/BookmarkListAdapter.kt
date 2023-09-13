package com.jess.camp.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jess.camp.databinding.BookmarkItemBinding

class BookmarkListAdapter : RecyclerView.Adapter<BookmarkListAdapter.ViewHolder>() {

    private val list = ArrayList<BookmarkModel>()

    fun addItems(items: List<BookmarkModel>) {
        list.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BookmarkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    class ViewHolder(
        private val binding: BookmarkItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BookmarkModel) = with(binding) {
            title.text = item.title
        }
    }

}