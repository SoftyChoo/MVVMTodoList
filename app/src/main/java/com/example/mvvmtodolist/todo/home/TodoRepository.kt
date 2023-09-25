package com.example.mvvmtodolist.todo.home

import java.util.concurrent.atomic.AtomicLong

interface TodoRepository{
    fun getTestData(): List<TodoModel>
    fun getIdGenerate() : AtomicLong
}
class TodoRepositoryImpl : TodoRepository {
    private val idGenerate: AtomicLong = AtomicLong(1L)
    private val list : ArrayList<TodoModel> = ArrayList()

    override fun getTestData() : List<TodoModel> {
        list =  arrayListOf<TodoModel>().apply {
            for (i in 0 until 3) {
                add(
                    TodoModel(
                        idGenerate.getAndIncrement(),
                        "title $i",
                        "description $i"
                    )
                )
            }
            return list
        }
    }

    override fun getIdGenerate() : AtomicLong = idGenerate
}