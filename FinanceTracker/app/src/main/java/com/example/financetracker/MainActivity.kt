package com.example.financetracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // set up drawer from Hamburger button
        var drawer = findViewById<DrawerLayout>(R.id.drawerLayout)
        findViewById<ImageView>(R.id.hamburger).setOnClickListener {
            drawer.openDrawer(GravityCompat.END)
        }

        setSupportActionBar(findViewById(R.id.navbar))
        // navigate to transaction lists fragment
        setupActionBarWithNavController(findNavController(R.id.transactionListFragment))
    }

    // set navigate up action
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.transactionListFragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}