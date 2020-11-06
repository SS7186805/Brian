package com.brian.views.adapters

import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import com.brian.base.BaseRecyclerAdapter
import com.brian.base.Prefs
import com.brian.databinding.MyChallengesItemBinding
import com.brian.internals.Utils
import com.brian.models.DataItemMyChalleneges
import com.brian.providers.resources.ResourcesProvider


class MyChallengesRequestsAdapter(
    override val layoutId: Int,
    var resourcesProvider: ResourcesProvider
) :
    BaseRecyclerAdapter<MyChallengesItemBinding, DataItemMyChalleneges>() {

    var listener: onViewClick? = null

    override fun bind(holder: ViewHolder, item: DataItemMyChalleneges, position: Int) {

        holder.binding.item = item

        Log.e("UserId", Prefs.init().userInfo?.id.toString())


        holder.binding.lAcceptReject.visibility = VISIBLE
        holder.binding.cardVideoImage.visibility= GONE
        holder.binding.accept.visibility = VISIBLE
        holder.binding.approve.visibility = GONE

        if (android.text.format.DateFormat.is24HourFormat(resourcesProvider.getContext())) {
            holder.binding.time.setText(Utils.init.get24HourTimeChallenge(item.createdAt.toString()))
        } else {
            holder.binding.time.setText(Utils.init.get12HourTimeChallenge(item.createdAt.toString()))
        }


        holder.binding.accept.setOnClickListener {
            listener?.onRequestAcceptClick(position)

        }

        holder.binding.reject.setOnClickListener {
            listener?.onRequestRejectClick(position)

        }


    }


    interface onViewClick {
        fun onRequestAcceptClick(position: Int)
        fun onRequestRejectClick(position: Int)

    }


}