package com.brian.views.adapters

import android.util.Log
import android.view.View
import com.brian.base.BaseRecyclerAdapter
import com.brian.databinding.BuzzFeedItemBinding
import com.brian.models.BuzzFeedDataItem


class BuzzFeedAdapter(override val layoutId: Int) :
    BaseRecyclerAdapter<BuzzFeedItemBinding, BuzzFeedDataItem>() {

    var listener: onClickEvents? = null

    override fun bind(holder: ViewHolder, item: BuzzFeedDataItem, position: Int) {
        holder.binding.item = item

        Log.e("buzzFiless", item.buzzFeedFiles.toString())

        if (item.buzzFeedFiles!![0].fileType.equals("video")) {
            holder.binding.ivImage.visibility = View.VISIBLE
            holder.binding.ivVideo.visibility = View.VISIBLE
            holder.binding.ivAudio.visibility = View.GONE
        } else if (item.buzzFeedFiles!![0].fileType.equals("audio")) {
            holder.binding.ivAudio.visibility = View.VISIBLE
            holder.binding.ivVideo.visibility = View.VISIBLE
            holder.binding.ivImage.visibility = View.GONE


        } else {
            holder.binding.ivImage.visibility = View.VISIBLE
            holder.binding.ivVideo.visibility = View.GONE
            holder.binding.ivAudio.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            listener?.onVideoClick(position)
        }


    }


    interface onClickEvents {
        fun onVideoClick(position: Int)

    }


}