package com.example.financetracker.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.financetracker.data.FTDatabase
import com.example.financetracker.data.model.Currency
import com.example.financetracker.data.repository.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// A view model to communicate between repository and UI
class CurrencyViewModel(application: Application) : AndroidViewModel(application) {
    val readCurrency: LiveData<List<Currency>>
    private val repository: CurrencyRepository

    init {
        val currencyDao = FTDatabase.getDatabase(application).currencyDAO()
        repository = CurrencyRepository(currencyDao)
        readCurrency = repository.readCurrency
    }

    fun addCurrency(currency: Currency) {
        // applying coroutine scope to run the function in a background thread
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCurrency(currency)
        }
    }

    fun hasCurrency(code: String) : Boolean {
        var result = false
        viewModelScope.launch(Dispatchers.IO) {
            repository.hasCurrency(code)
            result = true
        }
        return result
    }

    fun updateCurrency(currency: Currency) {
        viewModelScope.launch(Dispatchers.IO){
            repository.updateCurrency(currency)
        }
    }
}