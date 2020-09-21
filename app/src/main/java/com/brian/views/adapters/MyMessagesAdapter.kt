package com.brian.views.adapters

import com.brian.base.BaseRecyclerAdapter
import com.brian.databinding.MessagesBinding


class MyMessagesAdapter(override val layoutId: Int) :
    BaseRecyclerAdapter<MessagesBinding, MessageData>() {

    var listener: onViewClick? = null

    override fun bind(holder: ViewHolder, item: MessageData, position: Int) {

        holder.binding.view.setOnClickListener{
            listener?.onAprroveClick()

        }



    }


    interface onViewClick {
        fun onAprroveClick()

    }


}