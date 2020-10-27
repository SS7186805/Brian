package com.brian.views.adapters

import com.brian.base.BaseRecyclerAdapter
import com.brian.databinding.ChallengeTypeBinding
import com.brian.models.ChallengeTypeDataItem
import com.brian.providers.resources.ResourcesProvider


class ChallengeTypeAdapter(override val layoutId: Int,val resourcesProvider: ResourcesProvider) :
    BaseRecyclerAdapter<ChallengeTypeBinding, ChallengeTypeDataItem>() {

    var listerner:onClickView?=null


    override fun bind(holder: ViewHolder, item: ChallengeTypeDataItem, position: Int) {
        holder.binding.item = item


        holder.itemView.setOnClickListener{
            listerner?.onClickItem(position)
        }

    }

    interface onClickView{
        fun onClickItem(position: Int)
    }


}