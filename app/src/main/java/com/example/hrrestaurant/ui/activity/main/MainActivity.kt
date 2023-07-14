package com.example.hrrestaurant.ui.activity.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.hrrestaurant.R
import com.example.hrrestaurant.databinding.ActivityMainBinding
import com.example.hrrestaurant.ui.activity.loginActivity.LoginActivity
import com.example.hrrestaurant.ui.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            Toast.makeText(
                this,
                "You must accept notification permission to tracking your order details",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val mainActivityViewModel: MainActivityViewModel by viewModels()
    private var areViewsShown = true
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth

    // is used by the navigation drive to manage the behaviour of the navigation button in the upper left corner , this changees behavior depending on the destination level
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Declare the launcher at the top of your Activity/Fragment:
        askNotificationPermission()

        auth = Firebase.auth
        if (!checkUserLogin()) {
            hideLogOutButton()
            hideOrdersHistory()
        } else {
            showLogOutButton()
            showOrderHistoryMenuItem()
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        mainActivityViewModel

        toggle = ActionBarDrawerToggle(
            this, binding.drawerlayout, binding.myToolar, R.string.open, R.string.close
        )
        binding.drawerlayout.addDrawerListener(toggle)
        toggle.syncState()

        setUpNavigationMenu(navController)
        setUpBottomNavMenu(navController)
        setSupportActionBar(binding.myToolar)
        // defining the top level destinations , no back button is shown for these destinations
        appBarConfiguration = setUpAppBarConfiguration()
        // drawer layout to change Burger icon into a back button
        setupActionBarWithNavController(navController, appBarConfiguration)

        mainActivityViewModel.isRoomEmpty.observe(this) {
            if (it) {
                getAppData()
                observeAppData()
            } else {
                showFragmentContainerView()
                binding.cartFab.visibility = View.VISIBLE
            }
        }
        binding.searchTe.setOnClickListener {
            hideViews()
            goToSearchFragment()
        }

        binding.cartFab.setOnClickListener {
            hideViews()
            navController.navigate(R.id.cartFragment)
        }
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            hideViews()
            goToSelectedMenuItem(menuItem)
            true
        }
        binding.mainErrorLayout.refreshButton.setOnClickListener {
            binding.retryingProgressBar.visibility = View.VISIBLE
//            hideErrorLayout()
            Log.d("Repository", "Retrying ............")
            mainActivityViewModel.getDataFromInternet()
        }

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.videoFragment -> {
                    hideSearch()
                    navController.navigate(R.id.videoFragment)
                }
                R.id.favouriteFragment -> {
                    if (checkLoading()) {
                        hideSearch()
                        navController.navigate(R.id.favouriteFragment)
                    } else {
                        showSearch()
                        navController.navigate(R.id.favouriteFragment)
                    }
                }
                R.id.homeFragment -> {
                    if (checkLoading()) {
                        hideSearch()
                        navController.navigate(R.id.favouriteFragment)
                    } else {
                        showSearch()
                        navController.navigate(R.id.homeFragment)
                    }
                }
                else -> showSearch()
            }
            true
        }
    }


    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun showOrderHistoryMenuItem() {
        binding.navigationView.menu.findItem(R.id.logOut).isVisible = true
    }

    private fun hideOrdersHistory() {
        binding.navigationView.menu.findItem(R.id.ordersHistoryFragment).isVisible = false
    }

    private fun setUpAppBarConfiguration(): AppBarConfiguration = AppBarConfiguration(
//            navController.graph
        setOf(
            R.id.homeFragment,
            R.id.favouriteFragment,
            R.id.videoFragment,
        ), binding.drawerlayout
    )

    private fun hideLogOutButton() {
        binding.navigationView.menu.findItem(R.id.logOut).isVisible = false
    }

    private fun showLogOutButton() {
        binding.navigationView.menu.findItem(R.id.logOut).isVisible = true
    }

    private fun checkUserLogin(): Boolean = auth.currentUser != null

    private fun goToSelectedMenuItem(menuItem: MenuItem) {
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
//            R.id.locationFragment -> {
//                hideViews()
//                binding.drawerlayout.close()
//                navController.navigate(R.id.locationFragment)
//            }
            R.id.ordersHistoryFragment -> {
                hideViews()
                binding.drawerlayout.close()
                navController.navigate(R.id.ordersHistoryFragment)
            }
            R.id.logOut -> {
                if (checkUserLogin()) {
                    auth.signOut()
                    val loginActivityIntent = Intent(this, LoginActivity::class.java)
                    startActivity(loginActivityIntent)
                    finish()
                }
            }
            R.id.restaurantPhone -> {
                val checkWhatsappInstalled = checkWhatsappInstalled(packageManager, "com.whatsapp")
                if (checkWhatsappInstalled) {
                    val phoneNumber = "+201008573090"
                    val url = "https://api.whatsapp.com/send?phone=${phoneNumber}"
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(url)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Whatsapp isn't installed", Toast.LENGTH_SHORT).show()
                    intent.data =
                        Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp")
                    startActivity(intent)
                }
            }
        }
    }

    private fun checkWhatsappInstalled(pm: PackageManager, packageName: String): Boolean =
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        } catch (e: java.lang.Exception) {
            false
        }

    private fun goToSearchFragment() = navController.navigate(R.id.searchFragment)

    private fun getAppData() = mainActivityViewModel.getDataFromInternet()

    private fun observeAppData() {
        mainActivityViewModel.status.observe(this) { uiState ->
            when (uiState) {
                is UiState.Error -> {
                    Log.d("Repository", "Error getting data from internet")

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
    }

    private fun showSearch() {
        binding.searchTe.visibility = View.VISIBLE
    }

    private fun hideSearch() {
        binding.searchTe.visibility = View.GONE
    }

    /*
    This method is called whenever the user chooses to navigate Up within your application's activity hierarchy
    from the action bar.
    true if Up navigation completed successfully and this Activity was finished, false otherwise
     */
    override fun onSupportNavigateUp(): Boolean {
        if (checkLoading()) {
        } else showViews()
        return navController.navigateUp(appBarConfiguration)
    }

    override fun onBackPressed() {
        if (checkLoading()) {
        } else showViews()
        Log.d("MAINACTIVITY", "onBackPressed called")

        if (binding.drawerlayout.isOpen) {
            binding.drawerlayout.close()
        } else
            super.onBackPressed()
    }

    private fun checkLoading() =
        binding.mainLoadingLayout.progressBar.isVisible || binding.retryingProgressBar.isVisible
                || binding.mainErrorLayout.refreshButton.isVisible || binding.retryingProgressBar.isVisible

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
        }
        areViewsShown = false
    }

    private fun showViews() {
        binding.apply {
            Log.d("MAINACTIVITY", "showViews called")

            searchTe.visibility = View.VISIBLE
            cartFab.visibility = View.VISIBLE
            bottomNavigation.visibility = View.VISIBLE

        }
        areViewsShown = true
    }

    private fun showLoading() {
        binding.mainLoadingLayout.progressBar.visibility = View.VISIBLE
    }

    private fun hideErrorLayout() {
        binding.mainErrorLayout.errorText.visibility = View.GONE
        binding.mainErrorLayout.refreshButton.visibility = View.GONE
    }

    private fun showErrorLayout(errorMessage: String) {
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
