package com.brian.views.adapters

import android.content.Context
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.content.ContextCompat
import com.brian.R
import com.brian.base.BaseRecyclerAdapter
import com.brian.databinding.BadgeItemBinding
import com.brian.databinding.MyFriendsItemBinding
import com.brian.databinding.NavigationItemBinding
import com.brian.views.NavigationItem



class MyFriendsAdapter (override val layoutId: Int) : BaseRecyclerAdapter<MyFriendsItemBinding, MyFriends>(){

    var listener: onViewClick? = null

    override fun bind(holder: ViewHolder, item: MyFriends, position: Int) {



        if(item.isAcccepted){
            holder.binding.btnAccept.visibility=GONE
            holder.binding.btnReject.visibility=GONE
        }else{
            holder.binding.btnAccept.visibility= VISIBLE
            holder.binding.btnReject.visibility= VISIBLE
        }


        if(item.isSendRequest){
            holder.binding.btnAccept.visibility=GONE
            holder.binding.btnReject.visibility=GONE
            holder.binding.btnSendRequest.visibility= VISIBLE
        }

        if(item.isFriend){
            holder.binding.btnAccept.visibility=GONE
            holder.binding.btnReject.visibility=GONE
            holder.binding.btnSendRequest.visibility= GONE
            holder.binding.friends.visibility= VISIBLE
        }
        holder.binding.friendImage.setOnClickListener{
            listener?.onAprroveClick()
        }



    }

    interface onViewClick {
        fun onAprroveClick()

    }





}