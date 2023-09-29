package com.example.mvvmtodolist.todo.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.concurrent.atomic.AtomicLong

class TodoViewModel(
    repository: TodoRepository
) : ViewModel() {

    private val _list: MutableLiveData<List<TodoModel>> = MutableLiveData()
    val list: LiveData<List<TodoModel>> get() = _list
    private val idGenerate = repository.getIdGenerate()

    init {
        _list.value = repository.getTestData()
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

    private val todoRemoteDataSource = TodoRemoteDataSource()

    private val repository = TodoRepositoryImpl(todoRemoteDataSource)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            return TodoViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}