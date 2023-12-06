package com.example.iqprojectv1

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Timer
import java.util.TimerTask

class GameViewModel : ViewModel() {


    private val _balance = MutableLiveData<Int>()
    val balance: LiveData<Int> = _balance

    private val _clicks = MutableLiveData<Int>()
    val clicks: LiveData<Int> = _clicks

    private val _totalEarned = MutableLiveData<Int>()
    val totalEarned: LiveData<Int> = _totalEarned

    private val _passiveIncome = MutableLiveData<Int>()
    val passiveIncome: LiveData<Int> = _passiveIncome

    var itemPrice = 10
    var itemPassiveIncome = 1

    init {
        _balance.value = 0
        _clicks.value = 0
        _totalEarned.value = 0
        _passiveIncome.value = 0

        // Start passive income timer
        val passiveIncomeTimer = Timer()
        passiveIncomeTimer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                incrementBalance(_passiveIncome.value ?: 0)
            }
        }, 0, 3600000) // Every hour
    }

    fun earnMoney() {
        incrementBalance(1)
        _clicks.value = (_clicks.value ?: 0) + 1
        _totalEarned.value = (_totalEarned.value ?: 0) + 1
    }

    fun buyItem() {
        val currentBalance = _balance.value ?: 0
        if (currentBalance >= itemPrice) {
            val newBalance = currentBalance - itemPrice
            _balance.value = newBalance
            _passiveIncome.value = (_passiveIncome.value ?: 0) + itemPassiveIncome

            // Increase item price and passive income by 20%
            itemPrice = (itemPrice * 1.2).toInt()
            itemPassiveIncome = (itemPassiveIncome * 1.2).toInt()
        }
    }

    fun loadGameState(sharedPreferences: SharedPreferences) {
        _balance.value = sharedPreferences.getInt("balance", 0)
        _clicks.value = sharedPreferences.getInt("clicks", 0)
        _totalEarned.value = sharedPreferences.getInt("totalEarned", 0)
        _passiveIncome.value = sharedPreferences.getInt("passiveIncome", 0)
    }

    fun saveGameState(sharedPreferences: SharedPreferences) {
        val editor = sharedPreferences.edit()
        editor.putInt("balance", _balance.value ?: 0)
        editor.putInt("clicks", _clicks.value ?: 0)
        editor.putInt("totalEarned", _totalEarned.value ?: 0)
        editor.putInt("passiveIncome", _passiveIncome.value ?: 0)
        editor.apply()
    }

    private fun incrementBalance(increment: Int) {
        val currentBalance = _balance.value ?: 0
        val newBalance = currentBalance + increment
        _balance.value = newBalance
        _totalEarned.value = (_totalEarned.value ?: 0) + increment
    }
}