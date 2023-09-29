package com.example.mvvmtodolist.presentation.todo.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmtodolist.data.TodoRepositoryImpl
import java.util.concurrent.atomic.AtomicLong

class TodoViewModel(
    private val repository: TodoRepository
) : ViewModel() {

    private val _list: MutableLiveData<List<TodoModel>> = MutableLiveData()
    val list: LiveData<List<TodoModel>> get() = _list

    init {
        _list.value = repository.getTestData()
    }

    fun addTodoItem(
        item: TodoModel?
    ) {
        _list.value = repository.addTodoItem(item)
    }

    fun modifyTodoItem(
        item: TodoModel?
    ){
        _list.value = repository.modifyTodoItem(item)
    }


}

class TodoViewModelFactory : ViewModelProvider.Factory {

    private val repository = TodoRepositoryImpl()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            return TodoViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}