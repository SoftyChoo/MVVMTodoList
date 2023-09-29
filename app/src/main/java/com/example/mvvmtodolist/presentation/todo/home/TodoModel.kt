package com.example.mvvmtodolist.presentation.todo.home

import android.os.Parcelable
import com.example.mvvmtodolist.presentation.bookmark.BookmarkModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class TodoModel(
    val id: Long? = -1,
    val title: String?,
    val description: String?,
    val isBookmark: Boolean = false
) : Parcelable

fun TodoModel.toBookmarkModel(): BookmarkModel {
    return BookmarkModel(
        id = id ?: 0,
        title = title,
        description = description,
        isBookmark = isBookmark
    )
}