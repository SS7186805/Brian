package com.brian.views.adapters

import android.content.Context
import android.util.Log
import android.view.View.*
import androidx.core.content.ContextCompat
import com.brian.R
import com.brian.base.BaseRecyclerAdapter
import com.brian.databinding.*
import com.brian.models.BuzzFeedDataItem
import com.brian.models.TrainingVideosDataItem
import com.brian.views.NavigationItem



class BuzzFeedAdapter (override val layoutId: Int) : BaseRecyclerAdapter<BuzzFeedItemBinding, BuzzFeedDataItem>(){

    var listener:onClickEvents?=null

    override fun bind(holder: ViewHolder, item: BuzzFeedDataItem, position: Int) {
        holder.binding.item=item

        holder.itemView.setOnClickListener{
            listener?.onVideoClick(position)
        }


    }


    interface onClickEvents{
       fun onVideoClick(position: Int)

    }




}