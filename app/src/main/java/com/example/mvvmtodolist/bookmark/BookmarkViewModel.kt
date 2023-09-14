package com.example.mvvmtodolist.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BookmarkViewModel : ViewModel() {

    private val _list : MutableLiveData<List<BookmarkModel>> = MutableLiveData()
    val list : LiveData<List<BookmarkModel>> get() = _list
    // getter메소드를 사용하지 않으면
    // 외부에서 list에 대한 변경 사항을 감지하지 못하고 _list를 통해 데이터를 변경해야 한다.
    // 이것은 LiveData의 핵심 장점 중 하나인 데이터 변경 감지 및 생명주기 관리 기능을 사용하지 않는 것과 같다.



}