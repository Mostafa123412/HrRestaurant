package com.example.hrrestaurant.ui.fragment.video

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.hrrestaurant.data.dataSources.remoteDataSource.VideoItem
import com.example.hrrestaurant.data.dataSources.remoteDataSource.VideoSubscriber
import com.example.hrrestaurant.databinding.FragmentVideoBinding
import com.example.hrrestaurant.ui.adapter.VideoAdapter
import com.example.hrrestaurant.ui.base.BaseFragment
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoFragment : BaseFragment<FragmentVideoBinding>(FragmentVideoBinding::inflate) , VideoSubscriber {

    private lateinit var videoAdapter:VideoAdapter
    private lateinit var firebaseDatabaseRef: DatabaseReference


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val video3 = "android.resource://" + requireContext().packageName + "/" + R.raw.sea2
        firebaseDatabaseRef = Firebase.database.getReference("videos")
        val options =
            FirebaseRecyclerOptions.Builder<VideoItem>()
                .setQuery(firebaseDatabaseRef, VideoItem::class.java)
                .build()

        videoAdapter = VideoAdapter(options, this)
        binding.videoViewPager.adapter = videoAdapter

    }
    override fun onStart() {
        super.onStart()
        videoAdapter.startListening()
    }

    override fun onStop() {
        videoAdapter.stopListening()
        super.onStop()
    }

}