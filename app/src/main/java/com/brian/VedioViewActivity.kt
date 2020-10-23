package com.brian

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.brian.base.ScopedActivity
import com.brian.views.fragments.HomeFragment
import kotlinx.android.synthetic.main.fragment_video_view.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.x.closestKodein

class VedioViewActivity : ScopedActivity(),KodeinAware {

    override val kodein by closestKodein()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vedio_view)

        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.videoview))
        videoView.requestFocus()
        videoView.start()
        videoView.setOnCompletionListener {
            videoView.resume()
            var intent = Intent(this,HomeFragment::class.java)
            startActivity(intent)
           // findNavController().navigate(R.id.action_videoViewFragment_to_homeFragment)
        }


    }
}