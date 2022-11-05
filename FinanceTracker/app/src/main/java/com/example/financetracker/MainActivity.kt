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
import com.example.financetracker.viewmodel.CurrencyViewModel
import com.google.android.material.navigation.NavigationView

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

        // update currency data to database at the start of the app
        currencyVM = ViewModelProvider(this)[CurrencyViewModel::class.java]
        currencyVM.updateCurrencyFromAPI()
    }
}