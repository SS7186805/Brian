package com.brian.views.adapters

import com.brian.base.BaseRecyclerAdapter
import com.brian.databinding.TeamPlayersItemBinding
import com.brian.databinding.TeamsBinding
import com.brian.models.MyTeamMembersItem
import com.brian.providers.resources.ResourcesProvider


class MyTeamMembersAdapter(override val layoutId: Int, var resourcesProvider: ResourcesProvider) :
    BaseRecyclerAdapter<TeamPlayersItemBinding, MyTeamMembersItem>() {

    var listener: onClickEvents? = null

    override fun bind(holder: ViewHolder, item: MyTeamMembersItem, position: Int) {
        holder.binding.item = item

        holder.itemView.setOnClickListener {
            listener?.onPlayerClick(position)
        }


    }


    interface onClickEvents {
        fun onPlayerClick(position: Int)

    }


}