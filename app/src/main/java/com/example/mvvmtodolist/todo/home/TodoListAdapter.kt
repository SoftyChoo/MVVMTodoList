package com.example.mvvmtodolist.todo.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmtodolist.databinding.TodoItemBinding

class TodoListAdapter(
    private val onClickItem: (Int, TodoModel) -> Unit,
    private val onBookmarkChecked: (Int, TodoModel) -> Unit
) : ListAdapter<TodoModel, TodoListAdapter.ViewHolder>(// ListAdapter : List를 관리하기 쉽게 DiffUtil을 비동기처리해주는 기능이 들어간 Adapter
    object : DiffUtil.ItemCallback<TodoModel>() {
        // DiffUtil : list 관리를 쉽게해주기위해 만든 알고리듬
        // DiffUtil을 통해 데이터가 변경되었는지 찾음
        // + Adapter의 List와 새로 변경된 List를 비교해서 변경이 있으면 RecyclerView를 새로고침

        override fun areItemsTheSame( //areItemsTheSame item속성이 같은지 다른지 여부 판단
            oldItem: TodoModel,
            newItem: TodoModel
        ): Boolean {
            return oldItem.id == newItem.id // id를 통해 판단
        }

        override fun areContentsTheSame(
            oldItem: TodoModel,
            newItem: TodoModel
        ): Boolean {
            return oldItem == newItem
        }
        // ->> ** areItemsTheSame, areContentsTheSame DiffUtil을 판단해 데이터를 갱신해주는 역할을 하는 함수 **
    }
) {
    //ListAdapter는 기존에 사용하고 있던 리스트를 재활용하지 못한다.
    //그 이유는 기존에 들어왔던 ArrayList와 새로 들어온 ArrayList를 다르게 인식하기 때문에
    //submit(list)를 할 경우에 새로운 ArrayList의 인스턴스를 생성해서 넣어줘야한다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {// item 생성
        return ViewHolder(
            TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClickItem,
            onBookmarkChecked
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { // binding 해줌
        val item = getItem(position) // ListAdapter의 메소드 getItem
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

            // itemClick
            container.setOnClickListener {
                onClickItem(
                    adapterPosition,
                    item
                )
            }
            // 북마크 클릭
            bookmark.setOnCheckedChangeListener { _, isChecked ->
                // 현재 바인딩된 아이템과 checked 된 값 비교 후 전달
                if (item.isBookmark != isChecked) { // 둘이 상태가 다를 경우 상태를 isChecked로 바꾼 item을 copy해와 전송
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