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
    // ** _읽기 전용 변수 **

    val TodoState : MutableLiveData<TodoState> = MutableLiveData()
    // 단점 -> 이전 값을 재호출 한다.
    // Single Live Data를 사용해야 한다. -> Livedata를 래핑한 것

}

sealed interface BookmarkState{
    data class AddBookmark(val bookmarkModel: BookmarkModel) : BookmarkState
    data class RemoveBookmark(val bookmarkModel: BookmarkModel) : BookmarkState
    data class ModifyBookmark(val bookmarkModel: BookmarkModel) : BookmarkState
}

//sealed interface BookmarkState(val bookmarkModel: BookmarkModel){ // refactoring
//    data class AddBookmark() : BookmarkState
//    data class RemoveBookmark(val bookmarkModel: BookmarkModel) : BookmarkState
//    data class ModifyBookmark(val bookmarkModel: BookmarkModel) : BookmarkState
//}

sealed interface TodoState{
    data class ModifyTodo(val todoModel: TodoModel) : TodoState
}