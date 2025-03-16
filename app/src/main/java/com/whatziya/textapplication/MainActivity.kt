package com.whatziya.textapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.whatziya.textapplication.databinding.ActivityMainBinding
import com.whatziya.textapplication.events.NavGraphEvent
import com.whatziya.textapplication.fragments.factory.ViewModelFactory
import com.whatziya.textapplication.preferences.PreferenceProvider
import com.whatziya.textapplication.preferences.SharedPreferencesHelper
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: SharedViewModel by viewModels {
        ViewModelFactory(
            PreferenceProvider(
                SharedPreferencesHelper.provideSharedPreferences(
                    applicationContext
                )
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        viewModel.setNavGraphEvent()
        setupObservers(navHostFragment)
    }

    private fun setupObservers(navHostFragment: NavHostFragment) {
        viewModel.observeNavGraphEvent().onEach { event ->
            when (event) {
                NavGraphEvent.Login -> {
                    val loginGraph =
                        navHostFragment.navController.navInflater.inflate(R.navigation.login_in_nav_graph)
                    loginGraph.setStartDestination(R.id.signInFragment)
                    defaultNavHostTrue(navHostFragment)
                    navHostFragment.navController.graph = loginGraph
                }
                NavGraphEvent.Main -> {
                    val mainGraph =
                        navHostFragment.navController.navInflater.inflate(R.navigation.main_nav_graph)
                    mainGraph.setStartDestination(R.id.mainFragment)
                    defaultNavHostTrue(navHostFragment)
                    navHostFragment.navController.graph = mainGraph
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun defaultNavHostTrue(navHostFragment: NavHostFragment) {
        supportFragmentManager.beginTransaction().setPrimaryNavigationFragment(navHostFragment)
            .commit()
    }
}