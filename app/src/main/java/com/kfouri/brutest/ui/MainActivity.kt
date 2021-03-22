package com.kfouri.brutest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kfouri.brutest.R
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}