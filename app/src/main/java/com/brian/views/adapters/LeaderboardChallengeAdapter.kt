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

        if (position == 0) {
            holder.binding.tvRank.setText("1")
        } else if (position == 1) {
            holder.binding.tvRank.setText("2")

        } else if (position == 2) {
            holder.binding.tvRank.setText("3")

        }

        if (item.userType?.contains("Players")!!) {

            holder.binding.tvChallenges.setText("${item.weeklySituation?.toInt()}% Situations")


        } else {
            holder.binding.tvChallenges.setText("${item.weeklyCompleteChallenge} ${if (item.weeklyCompleteChallenge == 1) "Challenge" else "Challenges"}")

        }


    }


    interface onViewClick {
        fun onAprroveClick()

    }


}