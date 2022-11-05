package com.example.financetracker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.financetracker.data.model.Currency
import com.example.financetracker.data.viewmodel.CurrencyViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL


class MainActivity : AppCompatActivity() {
    private lateinit var currencyVM: CurrencyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // set up toolbar
        val toolbar = findViewById<Toolbar>(R.id.navbar)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navController = findNavController(R.id.navHostFragment)
        // top-level destinations are the screen in toolbar (transaction list and categories list)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.transactionListsFragment,
            R.id.categoryListsFragment,
            R.id.exportFragment,
            R.id.currencyFragment
        ), drawerLayout)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        // set up menu navigation
        val navigationView : NavigationView = findViewById(R.id.nav)
        NavigationUI.setupWithNavController(navigationView, navController)

        currencyVM = ViewModelProvider(this)[CurrencyViewModel::class.java]

        // get currency api
        getCurrencyData()
    }

    private fun getCurrencyData() {
        val API = "https://api.currencyapi.com/v3/latest?apikey=bXhCXWPbFh1ghBuCcmXg7IYE37lJJoZUn2E8Gzzn&currencies=EUR%2CAUD%2CVND"
        GlobalScope.launch(Dispatchers.IO) {
            try {
                // get the jsonObject from the API
                val apiResult = URL(API).readText()
                val jsonObject = JSONObject(apiResult).getJSONObject("data")

                // check if the default code has been in the database
                if (!currencyVM.hasCurrency("USD")) {
                    // if not existed, add it
                    currencyVM.addCurrency(Currency("USD", 1.0))
                }

                for (key in jsonObject.keys()) {
                    val data = jsonObject.getJSONObject(key)
                    // Get the code and value of the data
                    val code = data.getString("code")
                    val value = data.getDouble("value")
                    val currency = Currency(code, value)

                    // Check if the currency code has existed in the database
                    if (currencyVM.hasCurrency(code)) {
                        // if yes, update the currency
                        currencyVM.updateCurrency(currency)
                    }
                    else {
                        // else, add the currency
                        currencyVM.addCurrency(currency)
                    }
                }
            } catch (e: Exception) {
                Log.e("DetectException", "$e")
            }
        }
    }

}