package com.example.hrrestaurant.ui.activity.main

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.hrrestaurant.R
import com.example.hrrestaurant.databinding.ActivityMainBinding
import com.example.hrrestaurant.ui.util.UiState
import dagger.hilt.android.AndroidEntryPoint

const val TAG = "MainActivity"


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val mainActivityViewModel: MainActivityViewModel by viewModels()
    private var areViewsShown = true
    private lateinit var toggle: ActionBarDrawerToggle
    private var darkMode = false
    private lateinit var sharedPreferences: SharedPreferences

    // is used by the navigation drive to manage the behaviour of the navigation button in the upper left corner , this changees behavior depending on the destination level
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        mainActivityViewModel
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerlayout,
            binding.myToolar,
            R.string.open,
            R.string.close
        )
        binding.drawerlayout.addDrawerListener(toggle)
        toggle.syncState()
        setUpNavigationMenu(navController)
        setUpBottomNavMenu(navController)
        setSupportActionBar(binding.myToolar)
//        toggle.setToolbarNavigationClickListener {
//            toggle.isDrawerIndicatorEnabled = false
//        }
        // defining the top level destinations , no back button is shown for these destinations
        appBarConfiguration = AppBarConfiguration(
//            navController.graph
            setOf(
                R.id.homeFragment,
                R.id.favouriteFragment,
            ), binding.drawerlayout
        )
        // drawer layout to change Burger icon into a back button
        setupActionBarWithNavController(navController, appBarConfiguration)
        setUpNavigationMenu(navController)
        mainActivityViewModel.isRoomEmpty.observe(this) {
            if (it) {
                mainActivityViewModel.getDataFromInternet()
                mainActivityViewModel.status.observe(this) { uiState ->
                    when (uiState) {
                        is UiState.Error -> {
                            hideLoading()
                            hideFragmentContainerView()
                            hideSearch()
                            showErrorLayout(uiState.errorMessage)
                        }
                        is UiState.Loading -> {
                            Log.d("Repository", "Loading ............")
                            hideErrorLayout()
                            hideSearch()
                            hideFragmentContainerView()
                            showLoading()
                        }
                        is UiState.Success -> {
                            Log.d("Repository", "Successful  ............")
                            hideErrorLayout()
                            hideLoading()
                            showSearch()
                            showFragmentContainerView()
                        }
                    }
                }
            } else showFragmentContainerView()
        }
        binding.searchTe.setOnClickListener {
            hideViews()
            navController.navigate(R.id.searchFragment)
            supportFragmentManager.primaryNavigationFragment
            val currentFragment = navHostFragment.childFragmentManager.primaryNavigationFragment
            Toast.makeText(this, "CurrentFragment: $currentFragment", Toast.LENGTH_SHORT).show()
        }
        binding.darkMode.setOnClickListener {
            binding.lightMode.visibility = View.VISIBLE
            binding.darkMode.visibility = View.GONE
            sharedPreferences = getSharedPreferences("MODE", MODE_PRIVATE)
            var editor = sharedPreferences.edit()
            editor.putBoolean("night", true)
            editor.apply()
            darkMode = sharedPreferences.getBoolean("night", false)
            if (darkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
        binding.lightMode.setOnClickListener {
            sharedPreferences = getSharedPreferences("MODE", MODE_PRIVATE)
            var editor = sharedPreferences.edit()
            editor.putBoolean("night", false)
            editor.apply()
            darkMode = sharedPreferences.getBoolean("night", true)
            if (!darkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            binding.darkMode.visibility = View.VISIBLE
            binding.lightMode.visibility = View.GONE

        }
        binding.cartFab.setOnClickListener {
            hideViews()
            navController.navigate(R.id.cartFragment)
        }
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            hideViews()
            when (menuItem.itemId) {
                R.id.aboutDeveloperFragment -> {
                    hideViews()
                    binding.drawerlayout.close()
                    navController.navigate(R.id.aboutDeveloperFragment)
                }
                R.id.aboutRestaurantFragment -> {
                    hideViews()
                    binding.drawerlayout.close()
                    navController.navigate(R.id.aboutRestaurantFragment)
                }
                R.id.youtubeFragment -> {
                    hideViews()
                    binding.drawerlayout.close()
                    navController.navigate(R.id.youtubeFragment)
                }
                R.id.locationFragment -> {
                    hideViews()
                    binding.drawerlayout.close()
                    navController.navigate(R.id.locationFragment)
                }
                R.id.ordersHistoryFragment -> {
                    hideViews()
                    binding.drawerlayout.close()
                    navController.navigate(R.id.ordersHistoryFragment)
                }
            }
            true
        }

//        mainActivityViewModel._status.observe(this) { isRoomEmpty ->
//            Log.d("Repository", "is Room Empty ?: ${isRoomEmpty.isNullOrEmpty()}")
//            if (isRoomEmpty?.isEmpty()) {
//                showSearch()
//                showFragmentContainerView()
//            } else {
//                hideFragmentContainerView()
//                hideSearch()
//            }
//
//        }
        binding.mainErrorLayout.refreshButton.setOnClickListener {
            binding.retryingProgressBar.visibility = View.VISIBLE
//            hideErrorLayout()
            Log.d("Repository", "Retrying ............")
            mainActivityViewModel.getDataFromInternet()
        }

    }

    private fun showSearch() {
        binding.searchTe.visibility = View.VISIBLE
        binding.darkMode.visibility = View.VISIBLE
        binding.lightMode.visibility = View.VISIBLE

    }

    private fun hideSearch() {
        binding.searchTe.visibility = View.INVISIBLE
        binding.darkMode.visibility = View.INVISIBLE
        binding.lightMode.visibility = View.INVISIBLE
    }


    /*
    This method is called whenever the user chooses to navigate Up within your application's activity hierarchy from the action bar.
    true if Up navigation completed successfully and this Activity was finished, false otherwise
     */
    override fun onSupportNavigateUp(): Boolean {
        showViews()
        return navController.navigateUp(appBarConfiguration)
    }

    override fun onBackPressed() {
        showViews()
        if (binding.drawerlayout.isOpen) {
            binding.drawerlayout.close()
        } else
            super.onBackPressed()
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainerView, fragment, null)
            .commit()
    }

    private fun hideViews() {
        binding.apply {
            searchTe.visibility = View.GONE
            cartFab.visibility = View.GONE
            bottomNavigation.visibility = View.GONE
            lightMode.visibility = View.GONE
            darkMode.visibility = View.GONE
        }
        areViewsShown = false
    }

    private fun showViews() {
        binding.apply {
            searchTe.visibility = View.VISIBLE
            cartFab.visibility = View.VISIBLE
            bottomNavigation.visibility = View.VISIBLE
            if (this@MainActivity.darkMode) darkMode.visibility = View.VISIBLE
            else lightMode.visibility = View.VISIBLE

        }
        areViewsShown = true
    }

    private fun showLoading() {
        binding.mainLoadingLayout.progressBar.visibility = View.VISIBLE
    }

    private fun hideErrorLayout() {
        binding.mainErrorLayout.errorImg.visibility = View.GONE
        binding.mainErrorLayout.errorText.visibility = View.GONE
        binding.mainErrorLayout.refreshButton.visibility = View.GONE
    }

    private fun showErrorLayout(errorMessage: String) {
        binding.mainErrorLayout.errorImg.visibility = View.VISIBLE
        binding.mainErrorLayout.errorText.visibility = View.VISIBLE
        binding.mainErrorLayout.refreshButton.visibility = View.VISIBLE
        binding.mainErrorLayout.errorText.text = errorMessage
    }

    private fun hideLoading() {
        binding.retryingProgressBar.visibility = View.GONE
        binding.mainLoadingLayout.progressBar.visibility = View.GONE
    }

    private fun hideFragmentContainerView() {
        binding.fragmentContainerView.visibility = View.INVISIBLE
    }

    private fun showFragmentContainerView() {
        binding.fragmentContainerView.visibility = View.VISIBLE
    }


    private fun setUpNavigationMenu(navController: NavController) =
        binding.navigationView.setupWithNavController(navController)

    private fun setUpBottomNavMenu(navController: NavController) =
        binding.bottomNavigation.setupWithNavController(navController)


}