package com.brian.views.adapters

import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import com.brian.R
import com.brian.base.BaseRecyclerAdapter
import com.brian.databinding.MyFriendsItemBinding
import com.brian.models.MyFriendsDataItem
import com.brian.providers.resources.ResourcesProvider


class SelectFriendsAdapter(override val layoutId: Int, var context: ResourcesProvider) :
    BaseRecyclerAdapter<MyFriendsItemBinding, MyFriendsDataItem>() {

    var listener: onViewClick? = null
    var listenerChat: onChatClickListener? = null
    var rowIndex = 0
    var selectedUsers = ArrayList<MyFriendsDataItem>()

    override fun bind(holder: ViewHolder, item: MyFriendsDataItem, position: Int) {

        holder.binding.item = item



        Log.e("ItemSlectedd", item.isSelected.toString())
        for (user in selectedUsers) {

            Log.e("Selectusers", selectedUsers.size.toString())
            if (user.id == item.id) {
                item.isSelected = !item.isSelected
                holder.binding.clView.isSelected = item.isSelected
                selectedUsers.remove(user)
                break
//                notifyDataSetChanged()

            }
        }


        holder.binding.clView.isSelected = item.isSelected


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


        holder.itemView.setOnClickListener {
            if (listenerChat != null) {
                listenerChat?.onChatClick(position)

            } else {
                item.isSelected = !item.isSelected
                holder.binding.clView.isSelected = item.isSelected
                notifyDataSetChanged()
            }

        }


    }

    interface onViewClick {
        fun viewUserProfile(position: Int)
        fun acceptRequest(position: Int)
        fun rejectRequest(position: Int)

    }


    interface onChatClickListener {
        fun onChatClick(position: Int)

    }

}