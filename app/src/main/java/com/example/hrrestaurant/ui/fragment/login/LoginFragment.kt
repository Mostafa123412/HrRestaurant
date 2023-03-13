package com.example.hrrestaurant.ui.fragment.login

import android.content.Intent
import com.example.hrrestaurant.databinding.FragmentLoginBinding
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.hrrestaurant.R
import com.example.hrrestaurant.databinding.FragmentSignUpBinding
import com.example.hrrestaurant.ui.activity.loginActivity.LoginActivityViewModel
import com.example.hrrestaurant.ui.activity.main.MainActivity
import com.example.hrrestaurant.ui.activity.main.TAG
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

//
class LoginFragment : Fragment() {
    companion object {
        private const val TAG = "MAHMOUD"
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private val loginViewModel: LoginActivityViewModel by activityViewModels()
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth = Firebase.auth
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users")
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginBtn.setOnClickListener {
            checkLogin()
        }
        binding.googleImg.setOnClickListener {
            signIn()
        }
        binding.continueAsGuestBtn.setOnClickListener {
            val intent = Intent(requireContext() , MainActivity::class.java)
            startActivity(intent)
        }
    }

    // [START onactivityresult]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                Toast.makeText(
                    requireContext(),
                    "Google sign in failed , Api Error",
                    Toast.LENGTH_LONG
                ).show()

            } catch (internetException: ApiException) {
                Toast.makeText(
                    requireContext(),
                    "Login Failed, No Internet connection",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        lifecycleScope.launch {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            try {
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success")
                            val user = auth.currentUser
                            loginViewModel.changeLoginStatus(true)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.exception)
                            loginViewModel.changeLoginStatus(false)
                        }
                    }
            } catch (internetException: IOException) {
                Toast.makeText(
                    requireContext(),
                    "Login Failed, No Internet Connection",
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "SomeThing went wrong.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    // [END auth_with_google]
    // [END onactivityresult]

    private fun isRegistered(): Boolean = auth.currentUser != null
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    private fun checkLogin() {
        val email = binding.emailEt.text.toString().trim()
        val password = binding.passwordEt.text.toString().trim()
        if (email.isEmpty()) binding.email.error = "Enter email address"
        else if (password.isEmpty()) binding.password.error = "Enter Password"
        else {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val currentUser = auth.currentUser
                            Log.d("Mahmoud", "Current User = $currentUser")
                            if (currentUser != null) {
                                if (auth.currentUser!!.isEmailVerified) {
                                    Log.d("Mahmoud", "Email Verified")
                                    loginViewModel.apply {
                                        changeLoginStatus(true)
                                    }
                                } else {
                                    Log.d("Mahmoud", "Email Not Verified")
                                    Toast.makeText(
                                        requireContext(),
                                        "Login Failed, Please Verify Your Email and try again",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
//                            else {
//                                Toast.makeText(
//                                    requireContext(),
//                                    "Login Failed ${it.exception?.message}",
//                                    Toast.LENGTH_LONG
//                                ).show()
//                            }
                    }.await()
                } catch (e: FirebaseAuthInvalidUserException) {
                    Log.d("Mahmoud", "Exception Email Not Found")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Login Failed, This Account is not Found",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (badlyFormattedEmail: FirebaseAuthInvalidCredentialsException) {
                    withContext((Dispatchers.Main)) {
                        Toast.makeText(
                            requireContext(),
                            "Login Failed, Badly Formatted email",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (internetException: IOException) {
                    Toast.makeText(
                        requireContext(),
                        "Error : No Internet Connection",
                        Toast.LENGTH_LONG
                    ).show()
                } catch (httpException: HttpException) {
                    Toast.makeText(
                        requireContext(),
                        "Error : Error : HTTP Exception",
                        Toast.LENGTH_LONG
                    ).show()
                } catch (reAuthenticate: FirebaseAuthRecentLoginRequiredException) {
                    Toast.makeText(
                        requireContext(),
                        "Error,Please reAuthenticate Your email address",
                        Toast.LENGTH_LONG
                    ).show()

                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        "Error : Something went wrong",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        }
    }
}
