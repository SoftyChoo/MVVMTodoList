package com.example.mvvmtodolist.todo.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.concurrent.atomic.AtomicLong

class TodoViewModel(
    private val idGenerate: AtomicLong
) : ViewModel() {

    private val _list: MutableLiveData<List<TodoModel>> = MutableLiveData()
    val list: LiveData<List<TodoModel>> get() = _list

    init {
        _list.value = arrayListOf<TodoModel>().apply {
            for (i in 0 until 3) {
                add(
                    TodoModel(
                        idGenerate.getAndIncrement(),
                        "title $i",
                        "description $i"
                    )
                )
            }
        }
    }

    /**
     * TodoModel 아이템을 추가합니다.
     */
    fun addTodoItem(
        item: TodoModel?
    ) {
        if (item == null) {
            return
        }

        val currentList = list.value.orEmpty().toMutableList()
        _list.value = currentList.apply {
            add(
                item.copy(
                    id = idGenerate.getAndIncrement()
                )
            )
        }
    }

    fun modifyTodoItem(
        item: TodoModel?
    ) {

        fun findIndex(item: TodoModel?): Int {
            val currentList = list.value.orEmpty().toMutableList()
            // 같은 id 를 찾음
            val findTodo = currentList.find {
                it.id == item?.id
            }

            // 찾은 model 기준으로 index 를 찾음
            return currentList.indexOf(findTodo)
        }

        if (item == null) {
            return
        }

        // position 이 null 이면 indexOf 실시
        val findPosition = findIndex(item)
        if (findPosition < 0) {
            return
        }

        val currentList = list.value.orEmpty().toMutableList()
        currentList[findPosition] = item
        _list.value = currentList
    }

    fun removeTodoItem(position: Int?) {
        if (position == null || position < 0) {
            return
        }

        val currentList = list.value.orEmpty().toMutableList()
        currentList.removeAt(position)
        _list.value = currentList
    }
}

class TodoViewModelFactory : ViewModelProvider.Factory {

    // id 를 부여할 값
    private val idGenerate = AtomicLong(1L)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            return TodoViewModel(idGenerate) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}