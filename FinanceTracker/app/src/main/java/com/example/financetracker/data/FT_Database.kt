package com.example.financetracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.financetracker.model.Transaction

@Database(entities = [Transaction::class], version = 1, exportSchema = false)
abstract class FTDatabase: RoomDatabase() {
    abstract fun transactionDAO() : TransactionDAO

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
                    "transaction_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}