package com.brian.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brian.R
import com.brian.models.BuzzFeedFilesItem
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.layout_slide_view.view.*

class SlideViewRecyclerAdapter(private val images: ArrayList<BuzzFeedFilesItem>) :
    SliderViewAdapter<SlideViewRecyclerAdapter.ViewHolder>() {

    var listener:onClickEvents? = null

    inner class ViewHolder(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (images[position].fileType.equals("video")) {
            holder.itemView.ivSlideImage.visibility = View.VISIBLE
            holder.itemView.ivVideo.visibility = View.VISIBLE
            holder.itemView.ivAudio.visibility = View.GONE
        } else if (images[position].fileType.equals("audio")) {
            holder.itemView.ivSlideImage.visibility = View.GONE
            holder.itemView.ivVideo.visibility = View.VISIBLE
            holder.itemView.ivAudio.visibility = View.VISIBLE


        } else {
            holder.itemView.ivSlideImage.visibility = View.VISIBLE
            holder.itemView.ivVideo.visibility = View.GONE
            holder.itemView.ivAudio.visibility = View.GONE
        }
        Glide.with(holder.itemView)
            .load(images[position].fileUrl)
            .fitCenter()
            .into(holder.itemView.ivSlideImage)

        holder.itemView.setOnClickListener {
            if (images[position].fileType.equals("video")) {
                listener?.onVideoClick(position, images[position].fileUrl!!)

            } else if (images[position].fileType.equals("audio")) {
                listener?.onAudioClick(position, images[position].fileUrl!!)

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val view =
            LayoutInflater.from(parent?.context)
                .inflate(R.layout.layout_slide_view, parent, false)
        return ViewHolder(view)
    }

    override fun getCount(): Int {
        return if (images.isNullOrEmpty()) 0 else images.size
    }

    interface onClickEvents {
        fun onVideoClick(position: Int,url:String)
        fun onAudioClick(position: Int,url: String)

    }

}