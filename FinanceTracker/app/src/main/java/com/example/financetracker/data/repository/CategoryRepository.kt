package com.example.financetracker.data.repository

import androidx.lifecycle.LiveData
import com.example.financetracker.data.dao.CategoryDAO
import com.example.financetracker.data.model.Category

// A repository class to access to the data resources
class CategoryRepository(private val categoryDao: CategoryDAO) {
    val readAllCategories: LiveData<List<Category>> = categoryDao.readAllCategories()

    suspend fun addCategory(category: Category) {
        categoryDao.addCategory(category)
    }

    suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(category)
    }

    suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }
}