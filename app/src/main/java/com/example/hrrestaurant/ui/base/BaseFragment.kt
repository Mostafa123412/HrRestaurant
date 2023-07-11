package com.example.hrrestaurant.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.hrrestaurant.ui.adapter.ItemListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater, container: ViewGroup?, boolean: Boolean) -> VB,
) : Fragment(), ItemListener {
    override fun addItemToCart(id: Int) {
        CoroutineScope(Dispatchers.IO).launch { sharedViewModel.addItemToCart(id) }
    }
    override fun removeItemFromCart(id: Int) {
        lifecycleScope.launch { sharedViewModel.removeItemFromCart(id) }
    }

    // should i call non-suspend function in lifecycle scope?
    override fun incrementItemCount(id: Int) {
        lifecycleScope.launch {
            sharedViewModel.incrementItemCount(id)
        }
    }

    override fun decrementItemCount(id: Int) {
        lifecycleScope.launch {
            sharedViewModel.decrementItemCount(id)
        }
    }

    override fun setItemCountToZero(id: Int) {
        lifecycleScope.launch {
            sharedViewModel.setItemCountToZero(id)
        }
    }
    override fun addItemToFavourite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            sharedViewModel.addItemToFavorite(id)
        }
    }


    private val sharedViewModel: SharedViewModel by activityViewModels()
    val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }
    open val fireStoreDb: FirebaseFirestore by lazy { Firebase.firestore }
    open var currentUserId: String? =
        if (firebaseAuth.currentUser != null) firebaseAuth.uid!! else null


    private var _binding: VB? = null
    val binding: VB
        get() = _binding as VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fireStoreDb
        _binding = bindingInflater.invoke(inflater, container, false)
        if (_binding == null) {
            throw java.lang.IllegalArgumentException("Binding cannot be null")
        }
        return binding.root
    }


    override fun removeItemFromFavourite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            sharedViewModel.removeItemFromFavorite(id)
        }
    }

//    override fun rateItem(id: Int?, rate: Float?): String {
//        var result = ""
//        sharedViewModel.result.observe(viewLifecycleOwner) {
//            result = it
//        }
//        return result
//    }




}