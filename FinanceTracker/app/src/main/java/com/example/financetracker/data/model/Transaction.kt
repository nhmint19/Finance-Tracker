package com.example.financetracker.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

// Create the entity for the database
@Parcelize
@Entity(tableName = "transaction_table")
data class Transaction (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val date: Long,
    val amount: Float,
    val category: String
) : Parcelable