package com.example.financetracker.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.financetracker.data.FTDatabase
import com.example.financetracker.data.api.CurrencyAPI
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

    // update the currency based on JSON object obtained from api
    fun updateCurrencyFromAPI() {
        viewModelScope.launch(Dispatchers.IO) {
            // get the currency data
            val jsonObject = CurrencyAPI.getCurrencyData()

            // update the data to the local database
            if (jsonObject != null) {
                for (code in jsonObject.keys()) {
                    // Get the code and value of the data
                    val value = jsonObject.getDouble(code)
                    val currency = Currency(code, value)

                    // Check if the currency code has existed in the database
                    if (repository.hasCurrency(code)) {
                        // if yes, update the currency
                        repository.updateCurrency(currency)
                    } else {
                        // else, add the currency
                        repository.addCurrency(currency)
                    }
                }
            }
        }
    }
}