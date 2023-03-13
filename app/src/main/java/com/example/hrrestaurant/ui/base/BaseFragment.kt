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
import kotlinx.coroutines.*

abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater, container: ViewGroup?, boolean: Boolean) -> VB
) : Fragment(), ItemListener {

    private val baseViewModel: SharedViewModel by activityViewModels()

    private var _binding: VB? = null
    val binding: VB
        get() = _binding as VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = bindingInflater.invoke(inflater, container, false)
        if (_binding == null) {
            throw  java.lang.IllegalArgumentException("Binding cannot be null")
        }
        return binding.root
    }

    override fun addItemToFavourite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            baseViewModel.addItemToFavorite(id)
        }
    }

    override fun removeItemFromFavourite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            baseViewModel.removeItemFromFavorite(id)
        }
    }

//    override fun rateItem(id: Int?, rate: Float?): String {
//        var result = ""
//        baseViewModel.result.observe(viewLifecycleOwner) {
//            result = it
//        }
//        return result
//    }

    override fun addItemToCart(id: Int) {
        CoroutineScope(Dispatchers.IO).launch { baseViewModel.addItemToCart(id) }
    }

    override fun removeItemFromCart(id: Int) {
        lifecycleScope.launch { baseViewModel.removeItemFromCart(id) }
    }



}