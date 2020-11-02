package com.brian.views.adapters

import com.brian.base.BaseRecyclerAdapter
import com.brian.databinding.MessagesBinding
import com.brian.internals.Utils
import com.brian.models.AllChatsDataItem
import com.brian.providers.resources.ResourcesProvider


class MyMessagesAdapter(override val layoutId: Int, var resourcesProvider: ResourcesProvider) :
    BaseRecyclerAdapter<MessagesBinding, AllChatsDataItem>() {

    var listener: onViewClick? = null

    override fun bind(holder: ViewHolder, item: AllChatsDataItem, position: Int) {
        holder.binding.item = item

        if (android.text.format.DateFormat.is24HourFormat(resourcesProvider.getContext())) {
            holder.binding.tvTime.setText(Utils.init.get24HourTime(item.lastMessage?.createdAt.toString()))
        } else {
            holder.binding.tvTime.setText(Utils.init.get12HourTime(item.lastMessage?.createdAt.toString()))
        }

        holder.binding.view.setOnClickListener {
            listener?.onChatClick(position)

        }


    }


    interface onViewClick {
        fun onChatClick(position: Int)

    }


}