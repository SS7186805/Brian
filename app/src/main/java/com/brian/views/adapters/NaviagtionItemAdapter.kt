package com.brian.views.adapters

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import com.brian.R
import com.brian.base.BaseRecyclerAdapter
import com.brian.databinding.NavigationItemBinding
import com.brian.views.NavigationItem



class NavigationItemAdapter (override val layoutId: Int,val context:Context) : BaseRecyclerAdapter<NavigationItemBinding, NavigationItem>(){

    var listener:onClickItem?=null

    override fun bind(holder: ViewHolder, item: NavigationItem, position: Int) {

        holder.binding.apply {
            icon.setImageResource(item.image)
            name.text=item.text
            if(item.isSelected){

                icon.setColorFilter(ContextCompat.getColor(context, R.color.black))
                name.setTextColor(context.resources.getColor(R.color.black))
                mainItem.isSelected=true

            }else{
                icon.setColorFilter(ContextCompat.getColor(context, R.color.icon_color))
                name.setTextColor(context.resources.getColor(R.color.text_Color_drawer_item))
                mainItem.isSelected=false
            }

        }









        holder.binding.mainItem.setOnClickListener{

            listener?.onClick(position,item)
        }

    }


    interface onClickItem{
        fun onClick(position: Int,item:NavigationItem)
    }

}