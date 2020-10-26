package com.brian.views.adapters

import android.content.Context
import android.util.Log
import android.view.View.*
import androidx.core.content.ContextCompat
import com.brian.R
import com.brian.base.BaseRecyclerAdapter
import com.brian.databinding.BadgeItemBinding
import com.brian.databinding.MyChallengesItemBinding
import com.brian.databinding.NavigationItemBinding
import com.brian.databinding.TrainingVideosItemBinding
import com.brian.models.TrainingVideosDataItem
import com.brian.views.NavigationItem



class TrainingVideosAdapter (override val layoutId: Int) : BaseRecyclerAdapter<TrainingVideosItemBinding, TrainingVideosDataItem>(){

    var listener:onClickEvents?=null

    override fun bind(holder: ViewHolder, item: TrainingVideosDataItem, position: Int) {
        holder.binding.item=item


        holder.itemView.setOnClickListener{
            listener?.onVideoClick(position)
        }


    }


    interface onClickEvents{
       fun onVideoClick(position: Int)

    }




}