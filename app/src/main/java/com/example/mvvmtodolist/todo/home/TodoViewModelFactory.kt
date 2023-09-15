package com.example.mvvmtodolist.todo.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException
import java.util.concurrent.atomic.AtomicLong

class TodoViewModelFactory(private val idGenerate: AtomicLong) : ViewModelProvider.Factory {

    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TodoViewModel::class.java)){
            return TodoViewModel(idGenerate) as T
        }
        throw IllegalArgumentException("Not Found ViewModel class.")
    }
}
// provider로 뷰모델을 생성하는 과정에서 Factory 필요
// 뷰모델에 주입
