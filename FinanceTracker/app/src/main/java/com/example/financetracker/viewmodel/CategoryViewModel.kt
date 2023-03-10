package com.example.financetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.financetracker.data.FTDatabase
import com.example.financetracker.model.Category
import com.example.financetracker.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// A view model to communicate between repository and UI
class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    val readAllCategories : LiveData<List<Category>>
    private val repository: CategoryRepository

    init {
        val categoryDao = FTDatabase.getDatabase(application).categoryDAO()
        repository = CategoryRepository(categoryDao)
        readAllCategories = repository.readAllCategories
    }

    fun addCategory(category: Category) {
        // applying coroutine scope to run the function in a background thread
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCategory(category)
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCategory(category)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCategory(category)
        }
    }
}