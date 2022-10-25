package com.example.financetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.financetracker.data.FTDatabase
import com.example.financetracker.repository.TransactionRepository
import com.example.financetracker.model.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// A view model to communicate between repository and UI
class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    val readAllTransactions : LiveData<List<Transaction>>
    private val repository: TransactionRepository

    init {
        val transactionDao = FTDatabase.getDatabase(application).transactionDAO()
        repository = TransactionRepository(transactionDao)
        readAllTransactions = repository.readAllTransactions
    }

    fun addTransaction(transaction: Transaction) {
        // applying coroutine scope to run the function in a background thread
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTransaction(transaction)
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTransaction(transaction)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTransaction(transaction)
        }
    }
}