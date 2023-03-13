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
import com.example.hrrestaurant.data.dataSources.remote.User
import com.example.hrrestaurant.databinding.FragmentSignUpBinding
import com.example.hrrestaurant.ui.activity.loginActivity.LoginActivityViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

//
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var auth: FirebaseAuth
    val loginViewModel: LoginActivityViewModel by activityViewModels()
    private lateinit var userEmail: String
    private lateinit var userPassword: String
    private var emailPattern = Regex("[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+")
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        user = User()
        binding.nameEt.addTextChangedListener { text ->
            if (text.toString().isEmpty()) {
                binding.name.error = "Invalid Name."
            } else loginViewModel.addUserName(text.toString())
        }
        binding.emailEt.addTextChangedListener { text ->
            if (text.toString().isEmpty()) {
                binding.email.error = "Invalid Email Address."
//            } else if (userEmail.trim().matches(emailPattern)) {
//                Toast.makeText(requireContext(),"Correct email",Toast.LENGTH_LONG).show()
            } else userEmail = text.toString()
        }
        binding.primaryPhoneNumberEt.addTextChangedListener { text ->
            if (text.toString().isEmpty()) {
                binding.password.error = "Invalid Phone Number."
            } else loginViewModel.addPrimaryPhoneNumber("$text".toInt())
        }
        binding.secondaryPhoneNumberEt.addTextChangedListener { text ->
            if (text.toString().isEmpty()) {
                binding.password.error = "Optional , used when making order"
                user.userSecondaryPhoneNumber = -1
            } else loginViewModel.addSecondaryPhoneNumber("$text".toInt())
        }
        binding.passwordEt.addTextChangedListener { text ->
            if (text.toString().isEmpty()) {
                binding.password.error = "Invalid Password."
            } else userPassword = text.toString()
        }
        binding.userLocationEt.addTextChangedListener { text ->
            if (text.toString().isEmpty()) {
                binding.userLocation.error = "Invalid Location."
            } else loginViewModel.addLocation(text.toString())
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
                            binding.userLocationEt.setText("")
                            binding.primaryPhoneNumberEt.setText("")
                            binding.nameEt.setText("")
                        }
//                            else {
//                                Log.d("Mahmoud", "Email Verified Failed to sent")
//                                Toast.makeText(
//                                    requireContext(),
//                                    "Error : ${it.exception?.message}",
//                                    Toast.LENGTH_LONG
//                                ).show()
//                            }
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