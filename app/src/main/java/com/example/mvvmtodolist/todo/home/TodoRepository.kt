package com.example.mvvmtodolist.todo.home

import java.util.concurrent.atomic.AtomicLong

interface TodoRepository{
    fun getTestData(): List<TodoModel>
    fun getIdGenerate() : AtomicLong
}
class TodoRepositoryImpl( //bridge 역할
    private val todoRemoteDataSource: TodoRemoteDataSource
) : TodoRepository {
    override fun getTestData() : List<TodoModel>  = todoRemoteDataSource.list
    override fun getIdGenerate() : AtomicLong = todoRemoteDataSource.idGenerate

//    private val idGenerate: AtomicLong = AtomicLong(1L)
//    private val list : ArrayList<TodoModel> = ArrayList()
//
//    override fun getTestData() : List<TodoModel> {
//        list =  arrayListOf<TodoModel>().apply {
//            for (i in 0 until 3) {
//                add(
//                    TodoModel(
//                        idGenerate.getAndIncrement(),
//                        "title $i",
//                        "description $i"
//                    )
//                )
//            }
//            return list
//        }
//    }
//    override fun getIdGenerate() : AtomicLong = idGenerate
}

class TodoRemoteDataSource{
    val idGenerate: AtomicLong = AtomicLong(1L)
    var list : ArrayList<TodoModel> = ArrayList()

    init {
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
        }
    }
}