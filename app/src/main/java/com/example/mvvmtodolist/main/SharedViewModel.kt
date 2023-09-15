package com.example.mvvmtodolist.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmtodolist.bookmark.BookmarkModel
import com.example.mvvmtodolist.todo.home.TodoModel
import com.example.mvvmtodolist.todo.home.toBookmarkModel

class SharedViewModel : ViewModel() {

//    val modifyTodoItem: MutableLiveData<TodoModel> = MutableLiveData()
//    val modifyBookmarkItem: MutableLiveData<BookmarkModel> = MutableLiveData()
//    val addBookmarkItem : MutableLiveData<BookmarkModel> = MutableLiveData()
//    val removeBookmarkItem: MutableLiveData<BookmarkModel> = MutableLiveData()

    //bookmarkitem 에 타입을 설정해서 -sealed interface
    val bookmarkState : MutableLiveData<BookmarkState> = MutableLiveData()

    val TodoState : MutableLiveData<TodoState> = MutableLiveData()
}

sealed interface BookmarkState{
    data class AddBookmark(val bookmarkModel: BookmarkModel) : BookmarkState
    data class RemoveBookmark(val bookmarkModel: BookmarkModel) : BookmarkState
    data class ModifyBookmark(val bookmarkModel: BookmarkModel) : BookmarkState
}

sealed interface TodoState{
    data class ModifyTodo(val todoModel: TodoModel) : TodoState
}