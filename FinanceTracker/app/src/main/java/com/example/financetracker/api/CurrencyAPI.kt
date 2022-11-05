package com.example.financetracker.api
import android.util.Log
import org.json.JSONObject
import java.net.URL

// constant API
private const val API = "https://api.exchangerate.host/latest?base=USD&symbols=USD,EUR,AUD,VND"
const val DEFAULT_CURRENCY = "USD"
const val DEFAULT_VALUE = "1"

// The API object that can fetch currency api
// the sample api data
// {"rates":{"AUD":1.54829,"EUR":1.002049,"USD":1,"VND":24872.328244}}

object CurrencyAPI {
    fun getCurrencyData() : JSONObject? {
        try {
            // get the jsonObject from the API
            val apiResult = URL(API).readText()
            Log.i("CurrencyAPI", apiResult)
            return JSONObject(apiResult).getJSONObject("rates")
        } catch (e: Exception) {
            Log.i("CurrencyAPI", "Failure!")
            Log.e("DetectException", "$e")
        }
        return null
    }
}