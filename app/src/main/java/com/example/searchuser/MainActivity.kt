package com.example.searchuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.searchuser.databinding.ActivityMainBinding
import com.example.searchuser.presentation.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        installSplashScreen()
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.image_navHost_Container)
                as NavHostFragment
        val navController = navHost.findNavController()
        val navGraph = navController.navInflater.inflate(R.navigation.main_nav_graph)
        navController.graph = navGraph
    }
}