package com.example.mvvmtodolist.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmtodolist.databinding.BookmarkItemBinding
import com.example.mvvmtodolist.todo.home.TodoModel

class BookmarkListAdapter(
    private val onBookmarkChecked: (Int, BookmarkModel) -> Unit
) : ListAdapter<BookmarkModel, BookmarkListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<BookmarkModel>() {
        override fun areItemsTheSame(
            oldItem: BookmarkModel, newItem: BookmarkModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: BookmarkModel,
            newItem: BookmarkModel
        ): Boolean {
            return oldItem == newItem
        }
    }
) {
    fun addItems(items: List<BookmarkModel>) {
        submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BookmarkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onBookmarkChecked
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) // ListAdapter의 메소드 getItem
        holder.bind(item)
    }

    class ViewHolder(
        private val binding: BookmarkItemBinding,
        private val onBookmarkChecked: (Int, BookmarkModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BookmarkModel) = with(binding) {
            title.text = item.title
            description.text = item.description
            bookmark.isChecked = item.isBookmark

            bookmark.setOnCheckedChangeListener { buttonView, isChecked ->
                if (item.isBookmark != isChecked) {
                    onBookmarkChecked(
                        adapterPosition,
                        item.copy(
                            isBookmark = isChecked
                        )
                    )
                }
            }
        }
    }

}