package com.example.financetracker.repository

import androidx.lifecycle.LiveData
import com.example.financetracker.data.dao.CategoryDAO
import com.example.financetracker.model.Category

// A repository class to access to the data resources
class CategoryRepository(private val categoryDao: CategoryDAO) {
    val readAllCategories: LiveData<List<Category>> = categoryDao.readAllCategories()

    fun addCategory(category: Category) {
        categoryDao.addCategory(category)
    }

    fun updateCategory(category: Category) {
        categoryDao.updateCategory(category)
    }

    fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }
}