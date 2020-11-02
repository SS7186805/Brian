package com.brian.views.adapters

import android.view.View.GONE
import android.view.View.VISIBLE
import com.brian.R
import com.brian.base.BaseRecyclerAdapter
import com.brian.databinding.MyFriendsItemBinding
import com.brian.models.MyFriendsDataItem
import com.brian.providers.resources.ResourcesProvider


class MyFriendsAdapter(override val layoutId: Int, var context: ResourcesProvider) :
    BaseRecyclerAdapter<MyFriendsItemBinding, MyFriendsDataItem>() {

    var listener: onViewClick? = null
    var rowIndex = 0

    override fun bind(holder: ViewHolder, item: MyFriendsDataItem, position: Int) {

        holder.binding.item = item

        if (item.isAccepted!!.contains(context.getString(R.string.No))
        ) {
            holder.binding.btnAccept.visibility = VISIBLE
            holder.binding.btnReject.visibility = VISIBLE
        } else {
            holder.binding.btnAccept.visibility = GONE
            holder.binding.btnReject.visibility = GONE
        }


        holder.binding.friendImage.setOnClickListener {
            listener?.viewUserProfile(position)
        }


        holder.binding.btnAccept.setOnClickListener {
            listener?.acceptRequest(position)
        }

        holder.binding.btnReject.setOnClickListener {
            listener?.rejectRequest(position)
        }


        holder.binding.friendImage.setOnClickListener {
            listener?.viewUserProfile(position)
        }
        holder.itemView.setOnClickListener {
            listener?.startChat(position)
        }



    }

    interface onViewClick {
        fun viewUserProfile(position: Int)
        fun startChat(position: Int)
        fun acceptRequest(position: Int)
        fun rejectRequest(position: Int)

    }


}