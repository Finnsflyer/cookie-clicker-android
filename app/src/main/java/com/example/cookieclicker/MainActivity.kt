package com.example.cookieclicker

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.cookieclicker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var cookies = 0L
    private var cookiesPerClick = 1L
    private var cookiesPerSecond = 0L
    private var clickUpgradeCost = 10L
    private var autoUpgradeCost = 25L

    private val handler = Handler(Looper.getMainLooper())
    private val autoClickRunnable = object : Runnable {
        override fun run() {
            if (cookiesPerSecond > 0L) {
                cookies += cookiesPerSecond
                updateUi()
            }
            handler.postDelayed(this, 1000L)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cookieButton.setOnClickListener {
            cookies += cookiesPerClick
            updateUi()
        }

        binding.buyClickUpgradeButton.setOnClickListener {
            if (cookies >= clickUpgradeCost) {
                cookies -= clickUpgradeCost
                cookiesPerClick *= 2
                clickUpgradeCost *= 2
                updateUi()
            }
        }

        binding.buyAutoUpgradeButton.setOnClickListener {
            if (cookies >= autoUpgradeCost) {
                cookies -= autoUpgradeCost
                cookiesPerSecond += 1
                autoUpgradeCost *= 2
                updateUi()
            }
        }

        binding.resetButton.setOnClickListener {
            resetGame()
        }

        updateUi()
        handler.post(autoClickRunnable)
    }

    override fun onDestroy() {
        handler.removeCallbacks(autoClickRunnable)
        super.onDestroy()
    }

    private fun updateUi() {
        binding.cookiesTextView.text = getString(R.string.cookies_format, cookies)
        binding.clickPowerTextView.text = getString(R.string.click_power_format, cookiesPerClick)
        binding.autoClickTextView.text = getString(R.string.auto_click_format, cookiesPerSecond)
        binding.buyClickUpgradeButton.text = getString(R.string.buy_click_upgrade, clickUpgradeCost)
        binding.buyAutoUpgradeButton.text = getString(R.string.buy_auto_upgrade, autoUpgradeCost)
    }

    private fun resetGame() {
        cookies = 0L
        cookiesPerClick = 1L
        cookiesPerSecond = 0L
        clickUpgradeCost = 10L
        autoUpgradeCost = 25L
        updateUi()
    }
}
