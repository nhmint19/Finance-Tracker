package com.example.financetracker.repository

import androidx.lifecycle.LiveData
import com.example.financetracker.data.TransactionDAO
import com.example.financetracker.model.Transaction

// A repository class to access to the data resources
class TransactionRepository(private val transactionDao: TransactionDAO) {
    val readAllTransactions: LiveData<List<Transaction>> = transactionDao.readAllTransactions()

    suspend fun addTransaction(transaction: Transaction) {
        transactionDao.addTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }
}

