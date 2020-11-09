package com.brian.views.adapters

import android.os.Handler
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import com.brian.base.BaseRecyclerAdapter
import com.brian.base.Prefs
import com.brian.databinding.MyChallengesItemBinding
import com.brian.internals.Utils
import com.brian.models.DataItemMyChalleneges
import com.brian.providers.resources.ResourcesProvider


class MyChallengesAdapter(override val layoutId: Int, var resourcesProvider: ResourcesProvider) :
    BaseRecyclerAdapter<MyChallengesItemBinding, DataItemMyChalleneges>() {

    var listener: onViewClick? = null

    override fun bind(holder: ViewHolder, item: DataItemMyChalleneges, position: Int) {

        holder.binding.item = item

        Log.e("timeCreateddd", "F${item.createdAt.toString()}")

        if (Prefs.init().userInfo?.id == item.challengeToUserId) {
            if (item.isAccepted == 1 && item.isApproved == 0) {
                holder.binding.bWaitingApproval.visibility = VISIBLE
                holder.binding.lAcceptReject.visibility = GONE
                holder.binding.cancelChallenge.visibility = GONE
                holder.binding.check.visibility = GONE
                holder.binding.cancel.visibility = GONE
                holder.binding.cardVideoImage.visibility = VISIBLE


            } else if (item.isAccepted == 0 && item.isApproved == 0) {
                /*holder.binding.lAcceptReject.visibility = VISIBLE
                holder.binding.accept.visibility = VISIBLE
                holder.binding.approve.visibility = GONE
                holder.binding.cancel.visibility = GONE
                holder.binding.cancelChallenge.visibility = GONE*/

            } else if (item.isAccepted == 1 && item.isApproved == 1) {
                holder.binding.check.visibility = VISIBLE
                holder.binding.cancel.visibility = GONE
                holder.binding.lAcceptReject.visibility = GONE
                holder.binding.cancelChallenge.visibility = GONE
                holder.binding.bWaitingApproval.visibility = GONE
                holder.binding.cardVideoImage.visibility = VISIBLE


            } else if (item.isAccepted == 1 && item.isApproved == 2) {
                holder.binding.cancel.visibility = VISIBLE
                holder.binding.check.visibility = GONE
                holder.binding.cancelChallenge.visibility = GONE
                holder.binding.lAcceptReject.visibility = GONE
                holder.binding.cardVideoImage.visibility = VISIBLE
                holder.binding.bWaitingApproval.visibility = GONE


            }

        }

        Log.e("OWnID", Prefs.init().userInfo?.id.toString())
        if (Prefs.init().userInfo?.id != item.challengeToUserId) {
            if (item.isAccepted == 1 && item.isApproved == 0) {
                holder.binding.lAcceptReject.visibility = VISIBLE
                holder.binding.accept.visibility = GONE
                holder.binding.approve.visibility = VISIBLE
                holder.binding.cardVideoImage.visibility = VISIBLE
                holder.binding.cancelChallenge.visibility = GONE
                holder.binding.bWaitingApproval.visibility = GONE
                holder.binding.cancel.visibility = GONE
                holder.binding.check.visibility = GONE

            } else if (item.isAccepted == 1 && item.isApproved == 1) {
                holder.binding.check.visibility = VISIBLE
                holder.binding.cancel.visibility = GONE
                holder.binding.cancelChallenge.visibility = GONE
                holder.binding.cardVideoImage.visibility = VISIBLE
                holder.binding.lAcceptReject.visibility = GONE
                holder.binding.bWaitingApproval.visibility = GONE

            } else if (item.isAccepted == 1 && item.isApproved == 2) {
                holder.binding.cancel.visibility = VISIBLE
                holder.binding.check.visibility = GONE
                holder.binding.cardVideoImage.visibility = VISIBLE
                holder.binding.cancelChallenge.visibility = GONE
                holder.binding.lAcceptReject.visibility = GONE
                holder.binding.bWaitingApproval.visibility = GONE

            } else {
                holder.binding.cancel.visibility = GONE
                holder.binding.cardVideoImage.visibility = GONE
                holder.binding.check.visibility = GONE
                holder.binding.cancelChallenge.visibility = VISIBLE
                holder.binding.lAcceptReject.visibility = GONE
                holder.binding.accept.visibility = GONE
                holder.binding.approve.visibility = GONE
                holder.binding.bWaitingApproval.visibility = GONE


            }
        }

   /*     if (android.text.format.DateFormat.is24HourFormat(resourcesProvider.getContext())) {
            holder.binding.time.setText(Utils.init.get24HourTimeChallenge(item.challenge?.createdAt.toString()))
        } else {
            holder.binding.time.setText(Utils.init.get12HourTimeChallenge(item.challenge?.createdAt.toString()))
        }
*/
        holder.binding.cardVideoImage.setOnClickListener {
            holder.binding.cardVideoImage.isEnabled = false
            Handler().postDelayed({

                holder.binding.cardVideoImage.isEnabled = true

            }, 500)

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

    override fun getItemViewType(position: Int): Int {
        return position
    }


    interface onViewClick {
        fun onCancelChallenge(position: Int)
        fun onApproveRejectChallenge(position: Int, status: Int)
        fun onVideoClick(position: Int, url: String)

    }


}