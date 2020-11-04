package com.brian.views.adapters

import com.brian.base.BaseRecyclerAdapter
import com.brian.databinding.BadgeItemBinding
import com.brian.models.DataBadges


class BadgesAdapter(override val layoutId: Int) :
    BaseRecyclerAdapter<BadgeItemBinding, DataBadges>() {


    override fun bind(holder: ViewHolder, item: DataBadges, position: Int) {
        holder.binding.item = item


    }


}