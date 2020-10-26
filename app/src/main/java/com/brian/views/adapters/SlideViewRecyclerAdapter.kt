package com.brian.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brian.R
import com.brian.models.BuzzFeedDataItem
import com.brian.models.BuzzFeedFilesItem
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.layout_slide_view.view.*

class SlideViewRecyclerAdapter(private val images: ArrayList<BuzzFeedFilesItem>) :
    SliderViewAdapter<SlideViewRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(images[position].fileUrl)
            .fitCenter()
            .into(holder.itemView.ivSlideImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val view =
            LayoutInflater.from(parent?.context).inflate(R.layout.layout_slide_view, parent, false)
        return ViewHolder(view)
    }

    override fun getCount(): Int {
        return if (images.isNullOrEmpty()) 0 else images.size
    }
}