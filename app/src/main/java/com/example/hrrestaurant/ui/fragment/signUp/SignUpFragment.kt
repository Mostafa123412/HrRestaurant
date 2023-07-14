package com.example.hrrestaurant.ui.fragment.signUp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.hrrestaurant.databinding.FragmentSignUpBinding
import com.example.hrrestaurant.ui.activity.loginActivity.LoginActivityViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var auth: FirebaseAuth
    val loginViewModel: LoginActivityViewModel by activityViewModels()
    private lateinit var userEmail: String
    private lateinit var userPassword: String
    private lateinit var fireStoreDb: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fireStoreDb = Firebase.firestore
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        binding.emailEt.addTextChangedListener { text ->
            if (text.toString().isEmpty()) {
                binding.email.error = "Invalid Email Address."
//            } else if (userEmail.trim().matches(emailPattern)) {
//                Toast.makeText(requireContext(),"Correct email",Toast.LENGTH_LONG).show()
            } else userEmail = text.toString()
        }
        binding.passwordEt.addTextChangedListener { text ->
            if (text.toString().isEmpty()) {
                binding.password.error = "Invalid Password."
            } else userPassword = text.toString()
        }

        binding.signUpBtn.setOnClickListener {
            registerUser()
        }

    }

    private fun registerUser() {
        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            binding.password.error = "Invalid Email address or password"
            Log.d("LoginActivity", "user Data $userEmail - $userPassword")
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(userEmail, userPassword).await()
                    Log.d("Mahmoud", "Email Created and current user = ${auth.currentUser}")
                    auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.d("Mahmoud", "Email Verified Sent.")
                            Toast.makeText(
                                requireContext(), "Please verify your email address Then Login",
                                Toast.LENGTH_LONG
                            ).show()
                            binding.emailEt.setText("")
                            binding.passwordEt.setText("")
                        }
                            else {
                                Log.d("Firebase", "Email Verified Failed to sent")
                                Toast.makeText(
                                    requireContext(),
                                    "Error : ${it.exception?.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    }?.await()
                } catch (badlyFormattedEmail: FirebaseAuthInvalidCredentialsException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Register Failed, Badly Formatted email",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (emailAlreadyExists: FirebaseAuthUserCollisionException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Register Failed , Email Already Exists",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (weakPassword: FirebaseAuthWeakPasswordException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Register Failed,Enter Strong Password",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (internetException: IOException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Error : No Internet Connection",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (httpException: HttpException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Error : Error : HTTP Exception",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.d("Firebase", "error ${e.message}}")
                        Toast.makeText(
                            requireContext(),
                            e.message,
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            }
        }
    }

}
