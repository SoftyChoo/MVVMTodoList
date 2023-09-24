package com.example.mvvmtodolist.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BookmarkViewModel : ViewModel() {

    private val _list: MutableLiveData<List<BookmarkModel>> = MutableLiveData()
    val list: LiveData<List<BookmarkModel>> get() = _list

    fun addBookmarkItem(bookmarkModel: BookmarkModel?) {
        if (bookmarkModel == null) {
            return
        }
        val currentList = list.value.orEmpty().toMutableList() // orEmpty -> list.value가 null일 때 빈 리스트 반환
        currentList.add(bookmarkModel)
        _list.value = currentList
    }

    fun removeBookmarkItem(bookmarkModel: BookmarkModel ,position: Int?) {
        val findPosition = position ?: findIndex(bookmarkModel) //position이 안들어왔을 경우 id 값으로 진행
        if (findPosition == null || findPosition < 0) {
            return
        }

        val currentList = list.value.orEmpty().toMutableList()
        currentList.removeAt(findPosition)
        _list.value = currentList
    }

    fun modifyBookmarkItem(bookmarkModel: BookmarkModel) {
        val findPosition = findIndex(bookmarkModel)
        if (findPosition == null || findPosition <0){
            return
        }
        val currentList = list.value.orEmpty().toMutableList()
        currentList[findPosition] = bookmarkModel
        _list.value = currentList
    }

    fun findIndex(bookmarkModel: BookmarkModel): Int? {
        val currentList = list.value?.toMutableList()
        val findTodoById = currentList?.find { // 수정하고자 하는 todoModel의 id와 currentList의 id를 비교해 같은 id 를 찾음
            it.id == bookmarkModel.id
        }
        return currentList?.indexOf(findTodoById)//indexOf : 찾고자 하는 Array의 index를 반환
    }
}