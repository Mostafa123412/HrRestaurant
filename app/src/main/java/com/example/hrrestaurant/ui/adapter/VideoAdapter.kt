package com.example.hrrestaurant.ui.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrrestaurant.data.dataSources.remoteDataSource.VideoItem
import com.example.hrrestaurant.data.dataSources.remoteDataSource.VideoSubscriber
import com.example.hrrestaurant.databinding.VideoItemBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class VideoAdapter(
    private val options: FirebaseRecyclerOptions<VideoItem?>,
    private val subscriber: VideoSubscriber,
) :
    FirebaseRecyclerAdapter<VideoItem?, VideoAdapter.ViewPagerViewHolder>(options) {


    inner class ViewPagerViewHolder(val binding: VideoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setVideoData(videoItem: VideoItem) {
            binding.apply {
                videoView.setOnTouchListener { view, motionEvent ->
                    if (motionEvent.action == MotionEvent.ACTION_UP) { //which indicates that the user has released their touch on the VideoView.
                        if (videoView.isPlaying)
                            videoView.pause()
                        else
                            videoView.start()
                    }
                    /***
                     * Note that we return true from the OnTouchListener,
                     * which indicates that we have consumed the touch event
                     * and no other touch listeners should be called after this
                     */
                    true
                }
                videoTitle.text = videoItem.mealTitle
                textVideoDescription.text = videoItem.mealDescription ?: ""
                videoView.setVideoPath(videoItem.url)
                videoView.setOnPreparedListener { mediaPlayer ->
                    videoProgressBar.visibility = View.GONE
                    mediaPlayer.start()

                    val videoRatio = mediaPlayer.videoWidth / (mediaPlayer.videoHeight).toFloat()
                    val screenRatio = videoView.width / videoView.height.toFloat()
                    val scale = videoRatio / screenRatio
                    if (scale >= 1f) videoView.scaleX = scale else videoView.scaleY = 1f / scale

                }
                videoView.setOnCompletionListener { mp ->
                    mp.start()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        return ViewPagerViewHolder(
            VideoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int, model: VideoItem) {
        holder.setVideoData(model)
    }


}