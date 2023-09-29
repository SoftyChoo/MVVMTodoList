package com.example.mvvmtodolist.presentation.bookmark

import android.os.Parcelable
import com.example.mvvmtodolist.presentation.todo.home.TodoModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookmarkModel(
    val id: Long,
    val title: String?,
    val description: String?,
    val isBookmark: Boolean = false
) : Parcelable

fun BookmarkModel.toTodoModel(): TodoModel {
    return TodoModel(
        id = id,
        title = title,
        description = description,
        isBookmark = isBookmark
    )
}