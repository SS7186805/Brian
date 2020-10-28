package com.brian.views.adapters

import com.brian.base.BaseRecyclerAdapter
import com.brian.databinding.LeaderboardBinding
import com.brian.models.LeaderboardDataItem
import com.brian.providers.resources.ResourcesProvider


class LeaderboardChallengeAdapter(
    override val layoutId: Int,
    var resourcesProvider: ResourcesProvider
) : BaseRecyclerAdapter<LeaderboardBinding, LeaderboardDataItem>() {

    var listener: onViewClick? = null

    override fun bind(holder: ViewHolder, item: LeaderboardDataItem, position: Int) {

        holder.binding.item = item
        holder.binding.tvChallenges.setText("${item.weeklyCompleteChallenge} ${if (item.weeklyCompleteChallenge == 1) "Challenge" else "Challenges"}")


    }


    interface onViewClick {
        fun onAprroveClick()

    }


}