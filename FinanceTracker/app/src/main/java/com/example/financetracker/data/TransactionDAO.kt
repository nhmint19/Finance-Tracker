package com.example.financetracker.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.financetracker.model.Transaction

// DAO to interact with the database
@Dao
interface TransactionDAO {
    // Add a new transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTransaction(transaction: Transaction)

    // Show all data
    @Query("SELECT * FROM transaction_table ORDER BY id ASC")
    fun readAllTransactions(): LiveData<List<Transaction>>

    // Update an existing transaction
    @Update
    fun updateTransaction(transaction: Transaction)

    // Delete an existing transaction
    @Delete
    fun deleteTransaction(transaction: Transaction)
}