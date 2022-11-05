package com.example.financetracker.repository

import androidx.lifecycle.LiveData
import com.example.financetracker.data.dao.TransactionDAO
import com.example.financetracker.model.Transaction

// A repository class to access to the data resources
class TransactionRepository(private val transactionDao: TransactionDAO) {
    val readAllTransactions: LiveData<List<Transaction>> = transactionDao.readAllTransactions()

    fun addTransaction(transaction: Transaction) {
        transactionDao.addTransaction(transaction)
    }

    fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }

    fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }
}

