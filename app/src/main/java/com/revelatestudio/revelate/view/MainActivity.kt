package com.revelatestudio.revelate.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.revelatestudio.revelate.R
import com.revelatestudio.revelate.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        setupBottomNavigation()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupBottomNavigation() {
        val navController = findNavController(R.id.fragment)
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}