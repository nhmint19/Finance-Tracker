package com.example.financetracker.data.api

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.API
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL

// A view model to fetch api and communicate with main activity
class APIViewModel(application: Application) : AndroidViewModel(application) {
    fun getCurrencyData() : JSONObject? {
        var jsonObject : JSONObject? = null
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // get the jsonObject from the API
                val apiResult = URL(API).readText()
                jsonObject = JSONObject(apiResult).getJSONObject("data")
            } catch (e: Exception) {
                Log.e("DetectException", "$e")
            }
        }
        return jsonObject
    }
}