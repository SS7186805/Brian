package com.brian.views.adapters

import com.brian.base.BaseRecyclerAdapter
import com.brian.databinding.TeamsBinding
import com.brian.models.DataItemMyTeam
import com.brian.providers.resources.ResourcesProvider


class MyTeamsAdapter(override val layoutId: Int,var resourcesProvider: ResourcesProvider) :
    BaseRecyclerAdapter<TeamsBinding, DataItemMyTeam>() {

    var listener: onClickEvents? = null

    override fun bind(holder: ViewHolder, item: DataItemMyTeam, position: Int) {
        holder.binding.item = item

        holder.binding.iv11players.setText("${item.totalMember}")

        holder.itemView.setOnClickListener {
            listener?.onTeamClick(position)
        }


    }


    interface onClickEvents {
        fun onTeamClick(position: Int)

    }


}