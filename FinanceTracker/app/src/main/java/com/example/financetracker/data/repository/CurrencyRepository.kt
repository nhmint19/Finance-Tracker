package com.example.financetracker.data.repository

import androidx.lifecycle.LiveData
import com.example.financetracker.data.dao.CurrencyDAO
import com.example.financetracker.data.model.Currency

// A repository class to access to the data resources
class CurrencyRepository(private val currencyDao: CurrencyDAO) {
    val readCurrency: LiveData<List<Currency>> = currencyDao.readCurrency()

    fun addCurrency(currency: Currency) {
        currencyDao.addCurrency(currency)
    }

    fun hasCurrency(code: String) : Boolean {
        return currencyDao.hasCurrency(code) == 1
    }

    fun updateCurrency(currency: Currency) {
        return currencyDao.updateCurrency(currency)
    }
}