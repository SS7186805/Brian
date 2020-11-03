package com.brian.views.fragments

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.internals.Utils
import kotlinx.android.synthetic.main.fragment_video_view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein


class VideoViewFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    var isTraining=false
    var categoryId:Int?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_video_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // Utils.init.setupFullScreen(requireActivity())
        requireActivity().getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        var path=arguments?.getString("path")
        isTraining= arguments?.getBoolean(getString(R.string.training_videos))!!
        categoryId= arguments?.getInt(getString(R.string.id),0)
        videoView.setVideoPath(path)
        videoView.setBackgroundColor(Color.WHITE)
        videoView.requestFocus()
        videoView.start()
        videoView.setBackgroundColor(Color.TRANSPARENT)


        videoView.setOnCompletionListener {
            videoView.resume()
            if(isTraining){
                findNavController().navigate(R.id.action_videoViewFragment_to_trainingFragment,
                    bundleOf(getString(R.string.id) to categoryId))

            }else {
                findNavController().navigate(R.id.action_videoViewFragment_to_homeFragment)
            }
        }
    }
}