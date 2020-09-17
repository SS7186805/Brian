package com.brian.views.adapters

import android.content.Context
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.content.ContextCompat
import com.brian.R
import com.brian.base.BaseRecyclerAdapter
import com.brian.databinding.BadgeItemBinding
import com.brian.databinding.MyChallengesItemBinding
import com.brian.databinding.NavigationItemBinding
import com.brian.views.NavigationItem



class MyChallengesAdapter (override val layoutId: Int) : BaseRecyclerAdapter<MyChallengesItemBinding, MyChallenges>(){

    var listener:onViewClick?=null

    override fun bind(holder: ViewHolder, item: MyChallenges, position: Int) {

        if(item.isAccepted){
            holder.binding.lAcceptReject.visibility=GONE
            holder.binding.check.visibility= VISIBLE

        }else{
            holder.binding.lAcceptReject.visibility= VISIBLE
            holder.binding.check.visibility= GONE

        }

        if(item.cancelChallenge){
            holder.binding.cancelChallenge.visibility= VISIBLE
            holder.binding.lAcceptReject.visibility=GONE
            holder.binding.check.visibility= GONE

        }

        if(item.isRequest){
            holder.binding.accept.text="Approve"
            holder.binding.videoImage.visibility= GONE

        }


        holder.binding.accept.setOnClickListener{
            if(holder.binding.accept.text.contains("Approve")){
                listener?.onAprroveClick()
            }
        }






    }


    interface onViewClick{
       fun onAprroveClick()

    }




}