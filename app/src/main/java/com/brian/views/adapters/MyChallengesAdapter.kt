package com.brian.views.adapters

import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import com.brian.base.BaseRecyclerAdapter
import com.brian.base.Prefs
import com.brian.databinding.MyChallengesItemBinding
import com.brian.models.DataItemMyChalleneges
import com.brian.providers.resources.ResourcesProvider


class MyChallengesAdapter(override val layoutId: Int, var resourcesProvider: ResourcesProvider) :
    BaseRecyclerAdapter<MyChallengesItemBinding, DataItemMyChalleneges>() {

    var listener: onViewClick? = null

    override fun bind(holder: ViewHolder, item: DataItemMyChalleneges, position: Int) {

        holder.binding.item = item

        Log.e("UserId", Prefs.init().userInfo?.id.toString())

        if (Prefs.init().userInfo?.id == item.challengeToUserId) {
            if (item.isAccepted == 1 && item.isApproved == 0) {
                holder.binding.bWaitingApproval.visibility = VISIBLE
            } else if (item.isAccepted == 0 && item.isApproved == 0) {
                holder.binding.lAcceptReject.visibility = VISIBLE
                holder.binding.accept.visibility = VISIBLE
                holder.binding.approve.visibility = GONE
            } else if (item.isAccepted == 1 && item.isApproved == 1) {
                holder.binding.check.visibility = VISIBLE
            } else if (item.isAccepted == 1 && item.isApproved == 2) {
                holder.binding.cancel.visibility = VISIBLE
            }

        }

        if (Prefs.init().userInfo?.id != item.challengeToUserId) {
            if (item.isAccepted == 1 && item.isApproved == 0) {
                holder.binding.lAcceptReject.visibility = VISIBLE
                holder.binding.accept.visibility = GONE
                holder.binding.approve.visibility = VISIBLE
            } else if (item.isAccepted == 1 && item.isApproved == 1) {
                holder.binding.check.visibility = VISIBLE
            } else if (item.isAccepted == 1 && item.isApproved == 2) {
                holder.binding.cancel.visibility = VISIBLE
            } else {
                holder.binding.cancelChallenge.visibility = VISIBLE

            }

        }

        holder.binding.cardVideoImage.setOnClickListener {

            listener?.onVideoClick(position, item.fileName.toString())

        }

        holder.binding.approve.setOnClickListener {

            listener?.onApproveRejectChallenge(position, 1)

        }

        holder.binding.reject.setOnClickListener {

            listener?.onApproveRejectChallenge(position, 2)

        }


        holder.binding.cancelChallenge.setOnClickListener {
            listener?.onCancelChallenge(position)
        }


    }


    interface onViewClick {
        fun onCancelChallenge(position: Int)
        fun onApproveRejectChallenge(position: Int, status: Int)
        fun onVideoClick(position: Int, url: String)

    }


}