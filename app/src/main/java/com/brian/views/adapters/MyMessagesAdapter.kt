package com.brian.views.adapters

import com.brian.base.BaseRecyclerAdapter
import com.brian.databinding.MessagesBinding
import com.brian.models.AllChatsDataItem
import com.brian.providers.resources.ResourcesProvider


class MyMessagesAdapter(override val layoutId: Int, var resourcesProvider: ResourcesProvider) :
    BaseRecyclerAdapter<MessagesBinding, AllChatsDataItem>() {

    var listener: onViewClick? = null

    override fun bind(holder: ViewHolder, item: AllChatsDataItem, position: Int) {
        holder.binding.item = item

        holder.binding.view.setOnClickListener {
            listener?.onChatClick()

        }


    }


    interface onViewClick {
        fun onChatClick()

    }


}