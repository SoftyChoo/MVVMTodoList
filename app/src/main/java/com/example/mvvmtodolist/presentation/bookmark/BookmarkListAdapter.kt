package com.example.mvvmtodolist.presentation.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmtodolist.databinding.BookmarkItemBinding
import com.example.mvvmtodolist.presentation.todo.home.TodoModel

class BookmarkListAdapter(
    private val onBookmarkChecked: (Int, BookmarkModel) -> Unit
) : ListAdapter<BookmarkModel, BookmarkListAdapter.ViewHolder>(

    object : DiffUtil.ItemCallback<BookmarkModel>() {
        override fun areItemsTheSame(
            oldItem: BookmarkModel,
            newItem: BookmarkModel
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BookmarkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onBookmarkChecked
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
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

            // 북마크 클릭
            bookmark.setOnClickListener {
                // 현재 바인딩된 아이템과 checked 된 값 비교 후 전달
                if (item.isBookmark != bookmark.isChecked) {
                    onBookmarkChecked(
                        adapterPosition,
                        item.copy(
                            isBookmark = bookmark.isChecked
                        )
                    )
                }
            }
        }
    }

}