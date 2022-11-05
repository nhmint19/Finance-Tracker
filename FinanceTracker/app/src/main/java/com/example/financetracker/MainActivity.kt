package com.example.financetracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.financetracker.data.api.APIViewModel
import com.example.financetracker.data.viewmodel.CurrencyViewModel
import com.google.android.material.navigation.NavigationView

const val API = "https://api.currencyapi.com/v3/latest?apikey=bXhCXWPbFh1ghBuCcmXg7IYE37lJJoZUn2E8Gzzn&currencies=EUR%2CAUD%2CVND"
class MainActivity : AppCompatActivity() {
    private lateinit var currencyVM: CurrencyViewModel
    private lateinit var apiVM: APIViewModel

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

        // get currency api when the app starts
        apiVM = ViewModelProvider(this)[APIViewModel::class.java]
        val data = apiVM.getCurrencyData()

        // update currency data to database
        currencyVM = ViewModelProvider(this)[CurrencyViewModel::class.java]
        currencyVM.updateCurrency(data)
    }
}