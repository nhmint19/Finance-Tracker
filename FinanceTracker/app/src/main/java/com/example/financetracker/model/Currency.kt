package com.example.financetracker.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "currency_table")
data class Currency(
    @PrimaryKey
    val code: String,
    val value: Double
) : Parcelable