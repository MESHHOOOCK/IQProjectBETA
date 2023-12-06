package com.example.iqprojectv1

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class ShopActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView
    private lateinit var viewModel: GameViewModel
    private lateinit var itemPriceTextView: TextView
    private lateinit var itemPassiveIncomeTextView: TextView
    private lateinit var buyItemButton: Button

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        itemPriceTextView = findViewById(R.id.itemPriceTextView)
        itemPassiveIncomeTextView = findViewById(R.id.itemPassiveIncomeTextView)
        buyItemButton = findViewById(R.id.buyItemButton)

        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        viewModel.itemPrice.observe(this, Observer { itemPrice ->
            itemPriceTextView.text = "Item Price: $itemPrice₽"
        })

        viewModel.itemPassiveIncome.observe(this, Observer { itemPassiveIncome ->
            itemPassiveIncomeTextView.text = "Item Passive Income: $itemPassiveIncome₽ per hour"
        })

        sharedPreferences = getSharedPreferences("gameState", MODE_PRIVATE)

        buyItemButton.setOnClickListener {
            viewModel.buyItem()
        }
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
    }
}