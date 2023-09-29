package com.example.mvvmtodolist.data

import com.example.mvvmtodolist.presentation.todo.home.TodoModel
import com.example.mvvmtodolist.presentation.todo.home.TodoRepository
import java.util.concurrent.atomic.AtomicLong

class TodoRepositoryImpl( //bridge 역할
    private val idGenerate: AtomicLong = AtomicLong(1L)
) : TodoRepository {

    private val list: ArrayList<TodoModel> = ArrayList()

    override fun getTestData(): List<TodoModel> {
        list = arrayListOf<TodoModel>().apply {
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

    override fun addTodoItem(
        item: TodoModel?
    ): ArrayList<TodoModel> {
        if (item == null) {
            return list
        }

        list.add(
            item.copy(
                id = idGenerate.getAndIncrement()
            )
        )
        return ArrayList<TodoModel>(list)
    }

    override fun modifyTodoItem(
        item: TodoModel?
    ): ArrayList<TodoModel> {
        fun findIndex(item: TodoModel?): Int {
            // 같은 id 를 찾음
            val findTodo = list.find {
                it.id == item?.id
            }
            // 찾은 model 기준으로 index 를 찾음
            return list.indexOf(findTodo)
        }

        if (item == null) {
            return list
        }

        // position 이 null 이면 indexOf 실시
        val findPosition = findIndex(item)
        if (findPosition < 0) {
            return list
        }

        list[findPosition] = item
        return ArrayList<TodoModel>(list)
    }

    override fun removeTodoItem(position: Int?): ArrayList<TodoModel> {
        if (position == null || position < 0) {
            return list
        }

        list.removeAt(position)
        return ArrayList<TodoModel>(list)
    }
}
