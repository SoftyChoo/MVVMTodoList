package com.example.mvvmtodolist.presentation.todo.home

import android.icu.text.Transliterator.Position
import java.util.concurrent.atomic.AtomicLong

interface TodoRepository {
    fun getTestData(): List<TodoModel>
    fun addTodoItem(item:TodoModel?): List<TodoModel>
    fun modifyTodoItem(item:TodoModel?): List<TodoModel>
    fun removeTodoItem(position: Int?): List<TodoModel>
}
