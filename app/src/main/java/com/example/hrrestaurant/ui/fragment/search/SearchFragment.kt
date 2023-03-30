package com.example.hrrestaurant.ui.fragment.search

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.example.hrrestaurant.databinding.FragmentSearchBinding
import com.example.hrrestaurant.ui.adapter.HorizontalAdapter
import com.example.hrrestaurant.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {
    private val searchViewModel: SearchViewModel by viewModels()
    private val searchAdapter: HorizontalAdapter by lazy {
        HorizontalAdapter(this@SearchFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchRv.adapter = searchAdapter

        searchViewModel.foundItems.observe(viewLifecycleOwner) {
            Log.d("Repository", "search fragment recieved $it")
            it.let {
                searchAdapter.setNewData(it)
                searchAdapter.notifyDataSetChanged()
            }
        }

        binding.searchEt.addTextChangedListener { searchText ->
            Log.d("Repository", "NewText $searchText")
            if (searchText!!.isEmpty()) {
                searchAdapter.setNewData(emptyList())
            }else {
                searchViewModel.searchText.value = searchText.toString()
                searchViewModel.getItemsBySearchText()
            }
        }
//        binding.searchEt.doOnTextChanged { text, start, before, count ->
//            Log.d("Repository", "text Changed $text")
//
//        }
    }

}