package com.example.mvvmtodolist.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmtodolist.bookmark.BookmarkModel
import com.example.mvvmtodolist.bookmark.toTodoModel
import com.example.mvvmtodolist.todo.content.TodoContentType
import com.example.mvvmtodolist.todo.home.TodoModel
import com.example.mvvmtodolist.todo.home.toBookmarkModel

class SharedViewModel : ViewModel() {

    private val _bookmarkState: MutableLiveData<BookmarkState> = MutableLiveData()
    val bookmarkState: LiveData<BookmarkState> get() = _bookmarkState

    private val _todoState: MutableLiveData<TodoState> = MutableLiveData()
    val todoState: LiveData<TodoState> get() = _todoState

    fun updateBookmarkState(item: TodoModel, name: String) {
        when(name){
            TodoContentType.ADD.name -> _bookmarkState.value = BookmarkState.AddBookmark(item.toBookmarkModel())
            TodoContentType.REMOVE.name ->_bookmarkState.value = BookmarkState.RemoveBookmark(item.toBookmarkModel())
            TodoContentType.EDIT.name -> _bookmarkState.value = BookmarkState.ModifyBookmark(item.toBookmarkModel())
        }
    }
    fun updateTodoState(item: BookmarkModel){
        _todoState.value = TodoState.ModifyTodo(item.toTodoModel())
    }

}
sealed interface BookmarkState {
    data class AddBookmark(val bookmarkModel: BookmarkModel) : BookmarkState
    data class RemoveBookmark(val bookmarkModel: BookmarkModel) : BookmarkState
    data class ModifyBookmark(val bookmarkModel: BookmarkModel) : BookmarkState
}
sealed interface TodoState {
    data class ModifyTodo(val todoModel: TodoModel) : TodoState
}

//sealed interface BookmarkState(val bookmarkModel: BookmarkModel){ // refactoring
//    data class AddBookmark() : BookmarkState
//    data class RemoveBookmark(val bookmarkModel: BookmarkModel) : BookmarkState
//    data class ModifyBookmark(val bookmarkModel: BookmarkModel) : BookmarkState
//}

