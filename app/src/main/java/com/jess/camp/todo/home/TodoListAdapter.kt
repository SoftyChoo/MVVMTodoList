package com.jess.camp.todo.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jess.camp.databinding.TodoItemBinding

class TodoListAdapter(
    private val onClickItem: (Int, TodoModel) -> Unit
) : RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {

    private val list = ArrayList<TodoModel>()

    fun addItem(todoModel: TodoModel?) {
        todoModel?.let {
            list.add(todoModel)
            notifyItemChanged(list.size - 1)
        }
    }

    fun addItems(items: List<TodoModel>) {
        list.addAll(items)
        notifyDataSetChanged()
    }

    fun modifyItem(
        position: Int?,
        todoModel: TodoModel?
    ) {
        if (position == null || todoModel == null) {
            return
        }
        list[position] = todoModel
        notifyItemChanged(position)
    }

    fun removeItem(
        position: Int?
    ) {
        if (position == null) {
            return
        }
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClickItem
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    class ViewHolder(
        private val binding: TodoItemBinding,
        private val onClickItem: (Int, TodoModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TodoModel) = with(binding) {
            title.text = item.title
            description.text = item.description

            container.setOnClickListener {
                onClickItem(
                    adapterPosition,
                    item
                )
            }
        }
    }

}