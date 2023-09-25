package com.example.mvvmtodolist.todo.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmtodolist.databinding.TodoItemBinding

class TodoListAdapter(
    private val onClickItem: (Int, TodoModel) -> Unit,
    private val onBookmarkChecked: (Int, TodoModel) -> Unit
) : ListAdapter<TodoModel, TodoListAdapter.ViewHolder>(

    object : DiffUtil.ItemCallback<TodoModel>() {
        override fun areItemsTheSame(
            oldItem: TodoModel,
            newItem: TodoModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: TodoModel,
            newItem: TodoModel
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClickItem,
            onBookmarkChecked
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(
        private val binding: TodoItemBinding,
        private val onClickItem: (Int, TodoModel) -> Unit,
        private val onBookmarkChecked: (Int, TodoModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TodoModel) = with(binding) {
            title.text = item.title
            description.text = item.description
            bookmark.isChecked = item.isBookmark

            // 아이템 클릭
            container.setOnClickListener {
                onClickItem(
                    adapterPosition,
                    item
                )
            }

            // 북마크 클릭
            bookmark.setOnClickListener {
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