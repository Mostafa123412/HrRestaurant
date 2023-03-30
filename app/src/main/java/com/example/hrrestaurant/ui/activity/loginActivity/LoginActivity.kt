package com.example.hrrestaurant.ui.activity.loginActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.hrrestaurant.R
import com.example.hrrestaurant.databinding.ActivityLoginBinding
import com.example.hrrestaurant.ui.activity.main.MainActivity
import com.example.hrrestaurant.ui.fragment.login.LoginFragment
import com.example.hrrestaurant.ui.util.Constants.USERID
import com.example.hrrestaurant.ui.util.NetworkResponse
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var navController: NavController
    private val loginViewModel: LoginActivityViewModel by viewModels()

    //cannot retrieve list of all users , instead add every user to firebase db and retrieve them easily
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDB: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        auth = Firebase.auth
        firebaseDB = FirebaseFirestore.getInstance()
        installSplashScreen().apply {
            Log.d("Mahmoud", "logged In state in splashScreen ${checkLoggedInState()}")
            if (checkLoggedInState()) {
                createUserIfNotExists(auth.currentUser!!.uid, firebaseDB)
                moveToMainActivity()
            }
        }
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.findNavController()
        loginViewModel.loggedInStatus.observe(this) { status ->
            if (status) {
                binding.progressBar.visibility = View.VISIBLE
                Log.d("Mahmoud", "logged In state in viewModel ${status}")
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        createUserIfNotExists(auth.currentUser!!.uid, firebaseDB)
                    }
                }
                moveToMainActivity()
            }
        }
        binding.loginBtn.setOnClickListener {
            navController.navigate(R.id.loginFragment)
        }
        binding.signUpBtn.setOnClickListener {
            navController.navigate(R.id.signUpFragment)
        }
    }

    private fun checkLoggedInState(): Boolean {
        return if (auth.currentUser == null) {
            loginViewModel.changeLoginStatus(false)
            false
        } else if (!auth.currentUser!!.isEmailVerified) {
            loginViewModel.changeLoginStatus(false)
            false
        } else {
            loginViewModel.changeLoginStatus(true)
//            loginViewModel.userLoggedIn(auth.currentUser!!.uid , auth.currentUser!!.email!! )
            true
        }
    }

    fun createUserIfNotExists(userId: String, fireStoreDB: FirebaseFirestore) {
        val usersCollection = fireStoreDB.collection("Users")

        usersCollection.document(userId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    // User already exists, do nothing
                } else {
                    // User does not exist, create a new document
                    usersCollection.document(userId).set(
                        mapOf(
                            "userPoints" to 0,
                            // add additional fields as needed
                        )
                    )
                }
            } else {
                // Handle errors
            }
        }
    }


    // [START onactivityresult]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val loginFragmemt = supportFragmentManager.findFragmentById(R.id.loginFragment)
        loginFragmemt?.onActivityResult(requestCode, resultCode, data)
    }


    private fun moveToMainActivity() {
        Log.d("Mahmoud", "Moving to Main Activity ...")
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        binding.progressBar.visibility = View.GONE
        finish()
    }
}