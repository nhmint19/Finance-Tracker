package com.example.financetracker.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.financetracker.data.model.Category
import com.example.financetracker.data.model.Transaction

// DAO to interact with the database
@Dao
interface CategoryDAO {
    // Add a new transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCategory(category: Category)

    // Show all data
    @Query("SELECT * FROM category_table ORDER BY id DESC")
    fun readAllCategories(): LiveData<List<Category>>

    // Update an existing transaction
    @Update
    fun updateCategory(category: Category)

    // Delete an existing transaction
    @Delete
    fun deleteCategory(category: Category)
}