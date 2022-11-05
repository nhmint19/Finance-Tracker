package com.example.financetracker.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.financetracker.data.model.Currency

@Dao
interface CurrencyDAO {
    // Add a new currency
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCurrency(currency: Currency)

    // Show all data
    @Query("SELECT * FROM currency_table")
    fun readCurrency(): LiveData<List<Currency>>

    // Update an existing currency
    @Update
    fun updateCurrency(currency: Currency)

    // Check if a currency has existed
    @Query("SELECT COUNT(*) FROM currency_table WHERE code = :code")
    fun hasCurrency(code: String) : Int
}