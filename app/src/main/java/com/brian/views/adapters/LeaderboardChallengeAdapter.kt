package com.brian.views.adapters

import android.content.Context
import android.util.Log
import android.view.View.*
import androidx.core.content.ContextCompat
import com.brian.R
import com.brian.base.BaseRecyclerAdapter
import com.brian.databinding.BadgeItemBinding
import com.brian.databinding.LeaderboardBinding
import com.brian.databinding.MyChallengesItemBinding
import com.brian.databinding.NavigationItemBinding
import com.brian.views.NavigationItem



class LeaderboardChallengeAdapter (override val layoutId: Int) : BaseRecyclerAdapter<LeaderboardBinding, LeaderBoardChallenge>(){

    var listener:onViewClick?=null

    override fun bind(holder: ViewHolder, item: LeaderBoardChallenge, position: Int) {

        if(item.rank!=0){
            holder.binding.tvRank.visibility= VISIBLE

        }else{
            holder.binding.tvRank.visibility= INVISIBLE

        }

      holder.binding.tvRank.text=item.rank.toString()
      holder.binding.tvChallenges.text="${item.challengeCount} ${item.text}"






    }


    interface onViewClick{
       fun onAprroveClick()

    }




}