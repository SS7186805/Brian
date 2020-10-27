package com.brian.views.adapters

import android.view.View.VISIBLE
import com.brian.R
import com.brian.base.BaseRecyclerAdapter
import com.brian.databinding.UsersItemBinding
import com.brian.models.UserDataItem
import com.brian.providers.resources.ResourcesProvider


class AllUsersAdapter(override val layoutId: Int, var context: ResourcesProvider) :
    BaseRecyclerAdapter<UsersItemBinding, UserDataItem>() {

    var listener: onViewClick? = null

    override fun bind(holder: ViewHolder, item: UserDataItem, position: Int) {

        holder.binding.item = item

        if (item.reqSendBySelf!!.contains(context.getString(R.string.No)) && item.reqSendByOther!!.contains(
                context.getString(R.string.yes)
            ) && item.isAccepted!!.contains(context.getString(R.string.No))
        ) {
            holder.binding.btnAccept.visibility = VISIBLE
            holder.binding.btnReject.visibility = VISIBLE
        } else if (item.reqSendBySelf!!.contains(context.getString(R.string.No)) && item.reqSendByOther!!.contains(
                context.getString(R.string.No)
            ) && item.isAccepted!!.contains(context.getString(R.string.No))
        ) {
            holder.binding.btnSendRequest.visibility = VISIBLE
        } else if (item.reqSendBySelf!!.contains(context.getString(R.string.yes)) && item.reqSendByOther!!.contains(
                context.getString(R.string.no)
            ) && item.isAccepted!!.contains(context.getString(R.string.No))
        ) {
            holder.binding.btnCancelRequest.visibility = VISIBLE

        } else {
            holder.binding.friends.visibility = VISIBLE

        }


        holder.binding.friendImage.setOnClickListener {
            listener?.viewUserProfile(position)
        }

        holder.binding.btnCancelRequest.setOnClickListener {
            listener?.cancelrequest(position)
        }

        holder.binding.btnSendRequest.setOnClickListener {
            listener?.sendRequest(position)
        }

        holder.binding.btnAccept.setOnClickListener {
            listener?.acceptRequest(position)
        }

        holder.binding.btnReject.setOnClickListener {
            listener?.rejectRequest(position)
        }

        holder.binding.friends.setOnClickListener {
            listener?.removeFriend(position)
        }


    }

    interface onViewClick {
        fun viewUserProfile(position: Int)
        fun sendRequest(position: Int)
        fun cancelrequest(position: Int)
        fun acceptRequest(position: Int)
        fun rejectRequest(position: Int)
        fun removeFriend(position: Int)

    }


}