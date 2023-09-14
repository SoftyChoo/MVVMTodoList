package com.example.mvvmtodolist.bookmark

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookmarkModel(
    val id: Int,
    val title: String,
    val description: String,
    val bookmark: Boolean
) : Parcelable