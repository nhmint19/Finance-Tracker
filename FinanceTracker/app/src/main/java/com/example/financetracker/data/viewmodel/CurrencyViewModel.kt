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
import org.json.JSONObject

// A view model to communicate between repository and UI
class CurrencyViewModel(application: Application) : AndroidViewModel(application) {
    val readCurrency: LiveData<List<Currency>>
    private val repository: CurrencyRepository

    init {
        val currencyDao = FTDatabase.getDatabase(application).currencyDAO()
        repository = CurrencyRepository(currencyDao)
        readCurrency = repository.readCurrency
    }

    private fun hasCurrency(code: String) : Boolean {
        var result = false
        viewModelScope.launch(Dispatchers.IO) {
            repository.hasCurrency(code)
            result = true
        }
        return result
    }

    // update the currency based on JSON object obtained from api
    fun updateCurrency(jsonObject: JSONObject?) {
        // update the data to the local database
        if (jsonObject != null) {
            // check if the default code has been in the database
            if (!repository.hasCurrency("USD")) {
                // if not existed, add it
                repository.addCurrency(Currency("USD", 1.0))
            }

            for (key in jsonObject.keys()) {
                val data = jsonObject.getJSONObject(key)
                // Get the code and value of the data
                val code = data.getString("code")
                val value = data.getDouble("value")
                val currency = Currency(code, value)

                // Check if the currency code has existed in the database
                if (hasCurrency(code)) {
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