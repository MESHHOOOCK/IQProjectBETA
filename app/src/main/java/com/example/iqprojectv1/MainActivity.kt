     package com.example.iqprojectv1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.SharedPreferences
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

     class MainActivity : AppCompatActivity() {


         private lateinit var drawerLayout: DrawerLayout
         private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
         private lateinit var navigationView: NavigationView
         private lateinit var viewModel: GameViewModel
         private lateinit var balanceTextView: TextView
         private lateinit var clicksTextView: TextView
         private lateinit var totalEarnedTextView: TextView
         private lateinit var passiveIncomeTextView: TextView
         private lateinit var earnMoneyButton: Button

         private lateinit var sharedPreferences: SharedPreferences

         override fun onCreate(savedInstanceState: Bundle?) {
             super.onCreate(savedInstanceState)
             setContentView(R.layout.activity_main)

             balanceTextView = findViewById(R.id.balanceTextView)
             clicksTextView = findViewById(R.id.clicksTextView)
             totalEarnedTextView = findViewById(R.id.totalEarnedTextView)
             passiveIncomeTextView = findViewById(R.id.passiveIncomeTextView)
             earnMoneyButton = findViewById(R.id.earnMoneyButton)

             viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

             viewModel.balance.observe(this, Observer { balance ->
                 balanceTextView.text = "Balance: $balance₽"
             })

             viewModel.clicks.observe(this, Observer { clicks ->
                 clicksTextView.text = "Clicks: $clicks"
             })

             viewModel.totalEarned.observe(this, Observer { totalEarned ->
                 totalEarnedTextView.text = "Total Earned: $totalEarned₽"
             })

             viewModel.passiveIncome.observe(this, Observer { passiveIncome ->
                 passiveIncomeTextView.text = "Passive Income: $passiveIncome₽ per hour"
             })

             sharedPreferences = getSharedPreferences("gameState", MODE_PRIVATE)
             loadGameState()

             earnMoneyButton.setOnClickListener {
                 viewModel.earnMoney()
             }
         }

         override fun onPause() {
             super.onPause()
             saveGameState()
         }

         private fun loadGameState() {
             viewModel.loadGameState(sharedPreferences)
         }

         private fun saveGameState() {
             viewModel.saveGameState(sharedPreferences)
         }

         override fun onCreateOptionsMenu(menu: Menu?): Boolean {
             menuInflater.inflate(R.menu.menu_main, menu)
             return true
         }

         override fun onOptionsItemSelected(item: MenuItem): Boolean {
             when (item.itemId) {
                 R.id.menu_shop -> openShopPage()
             }
             return super.onOptionsItemSelected(item)
             setupDrawer()
         }

         private fun openShopPage() {
             setContentView(R.layout.activity_shop)
             val buyItemButton: Button = findViewById(R.id.buyItemButton)
             val itemPriceTextView: TextView = findViewById(R.id.itemPriceTextView)
             val itemPassiveIncomeTextView: TextView = findViewById(R.id.itemPassiveIncomeTextView)

             buyItemButton.setOnClickListener {
                 viewModel.buyItem()
             }

             // Set the initial item price and passive income in the shop
             itemPriceTextView.text = "Item Price: ${viewModel.itemPrice}₽"
             itemPassiveIncomeTextView.text =
                 "Item Passive Income: ${viewModel.itemPassiveIncome}₽ per hour"
         }

         override fun onCreate(savedInstanceState: Bundle?) {
             // остальной код

             setupDrawer()
         }

         private fun setupDrawer() {

             drawerLayout = findViewById(R.id.drawerLayout)
             actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close)
             drawerLayout.addDrawerListener(actionBarDrawerToggle)
             supportActionBar?.setDisplayHomeAsUpEnabled(true)

             navigationView = findViewById(R.id.navigationView)
             navigationView.setNavigationItemSelectedListener { menuItem ->
                 when (menuItem.itemId) {
                     R.id.action_shop -> {
                         startActivity(Intent(this, ShopActivity::class.java))
                         true
                     }
                     R.id.action_main -> {
                         startActivity(Intent(this, MainActivity::class.java))
                         true
                     }
                     else -> false
                 }
             }
         }

         override fun onCreateOptionsMenu(menu: Menu): Boolean {
             menuInflater.inflate(R.menu.drawer_menu, menu)
             return true
         }

         override fun onOptionsItemSelected(item: MenuItem): Boolean {
             return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
                 true
             } else {
                 super.onOptionsItemSelected(item)
             }
         }
     }