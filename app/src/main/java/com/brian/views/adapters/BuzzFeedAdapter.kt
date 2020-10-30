package com.brian.views.adapters

import com.brian.base.BaseRecyclerAdapter
import com.brian.databinding.BuzzFeedItemBinding
import com.brian.models.BuzzFeedDataItem


class BuzzFeedAdapter(override val layoutId: Int) :
    BaseRecyclerAdapter<BuzzFeedItemBinding, BuzzFeedDataItem>() {

    var listener: onClickEvents? = null

    override fun bind(holder: ViewHolder, item: BuzzFeedDataItem, position: Int) {
        holder.binding.item = item

        holder.itemView.setOnClickListener {
            listener?.onVideoClick(position)
        }


    }


    interface onClickEvents {
        fun onVideoClick(position: Int)

    }


}