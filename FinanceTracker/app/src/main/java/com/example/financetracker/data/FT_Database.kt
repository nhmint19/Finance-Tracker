package com.example.financetracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.financetracker.data.dao.CategoryDAO
import com.example.financetracker.data.dao.CurrencyDAO
import com.example.financetracker.data.dao.TransactionDAO
import com.example.financetracker.data.model.Category
import com.example.financetracker.data.model.Currency
import com.example.financetracker.data.model.Transaction

@Database(entities = [Transaction::class, Category::class, Currency::class], version = 1, exportSchema = false)
abstract class FTDatabase: RoomDatabase() {
    abstract fun transactionDAO() : TransactionDAO
    abstract fun categoryDAO(): CategoryDAO
    abstract fun currencyDAO(): CurrencyDAO

    // only one instance of db (Single Pattern)
    companion object {
        @Volatile
        private var INSTANCE: FTDatabase? = null

        fun getDatabase(context: Context) : FTDatabase {
            val tempInstance = INSTANCE
            // if database exists
            if (tempInstance != null)
                return tempInstance
            // if database does not exist
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FTDatabase::class.java,
                    "finance_tracker_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}