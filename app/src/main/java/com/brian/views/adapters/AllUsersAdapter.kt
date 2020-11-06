package com.brian.views.adapters

import android.os.Handler
import android.util.Log
import android.view.View.*
import androidx.constraintlayout.solver.GoalRow
import com.brian.R
import com.brian.base.BaseRecyclerAdapter
import com.brian.databinding.UsersItemBinding
import com.brian.models.UserDataItem
import com.brian.providers.resources.ResourcesProvider


class AllUsersAdapter(override val layoutId: Int, var context: ResourcesProvider) :
    BaseRecyclerAdapter<UsersItemBinding, UserDataItem>() {

    var listener: onViewClick? = null

    override fun bind(holder: ViewHolder, item: UserDataItem, position: Int) {

        Log.e("reqSendBySelf", item.reqSendBySelf.toString())
        Log.e("reqSendByOther", item.reqSendByOther.toString())
        Log.e("isAccepted", item.isAccepted.toString())

        holder.binding.item = item

        if (item.reqSendBySelf!!.contains(context.getString(R.string.No)) && item.reqSendByOther!!.contains(
                context.getString(R.string.yes)
            ) && item.isAccepted!!.contains(context.getString(R.string.No))
        ) {
            holder.binding.btnAccept.visibility = VISIBLE
            holder.binding.btnReject.visibility = VISIBLE
            holder.binding.friends.visibility = GONE
            holder.binding.btnSendRequest.visibility = GONE
            holder.binding.btnCancelRequest.visibility = GONE

        } else if (item.reqSendBySelf!!.contains(context.getString(R.string.No)) && item.reqSendByOther!!.contains(
                context.getString(R.string.No)
            ) && item.isAccepted!!.contains(context.getString(R.string.No))
        ) {
            Log.e("SendReqyesy", "SendRequest")
            holder.binding.btnSendRequest.visibility = VISIBLE
            holder.binding.btnCancelRequest.visibility = GONE
            holder.binding.friends.visibility = GONE
            holder.binding.btnAccept.visibility = INVISIBLE
            holder.binding.btnReject.visibility = INVISIBLE


        } else if (item.reqSendBySelf!!.contains(context.getString(R.string.yes)) && item.reqSendByOther!!.contains(
                context.getString(R.string.no)
            ) && item.isAccepted!!.contains(context.getString(R.string.No))
        ) {
            Log.e("Cancelrequest", "SendRequest")
            holder.binding.btnCancelRequest.visibility = VISIBLE
            holder.binding.btnSendRequest.visibility = GONE
            holder.binding.friends.visibility = GONE
            holder.binding.btnAccept.visibility = INVISIBLE
            holder.binding.btnReject.visibility = INVISIBLE


        } else {
            holder.binding.friends.visibility = VISIBLE
            holder.binding.btnCancelRequest.visibility = GONE
            holder.binding.btnSendRequest.visibility = GONE
            holder.binding.btnAccept.visibility = INVISIBLE
            holder.binding.btnReject.visibility = INVISIBLE
        }


        holder.binding.tvFrndName.setOnClickListener {
            holder.binding.tvFrndName.isEnabled = false
            Handler().postDelayed({

                holder.binding.tvFrndName.isEnabled = true

            }, 500)
            listener?.viewUserProfile(position)

        }

        holder.binding.friendImage.setOnClickListener {
            holder.binding.friendImage.isEnabled = false
            Handler().postDelayed({

                holder.binding.friendImage.isEnabled = true

            }, 500)
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