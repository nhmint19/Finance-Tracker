package com.example.financetracker.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "category_table")
data class Category (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val colorCode: String
) : Parcelable