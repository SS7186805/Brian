package com.brian.views.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.brian.R
import com.brian.models.AllChatsDataItem
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter

class SwipeToDeleteAdapter(dataSet: ArrayList<AllChatsDataItem> = ArrayList())
    : DragDropSwipeAdapter<AllChatsDataItem, SwipeToDeleteAdapter.ViewHolder>(dataSet) {

    var listener: onViewClick? = null


    class ViewHolder(itemView: View) : DragDropSwipeAdapter.ViewHolder(itemView) {

    }

    override fun onBindViewHolder(item: AllChatsDataItem, viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.setOnClickListener{
            listener?.onAprroveClick()
        }
    }



    override fun getViewToTouchToStartDraggingItem(item: AllChatsDataItem, viewHolder: SwipeToDeleteAdapter.ViewHolder, position: Int): View? {
        // We return the view holder's view on which the user has to touch to drag the item
        return viewHolder.itemView.findViewById(R.id.tvTime)
    }

    override fun getViewHolder(itemView: View): ViewHolder = SwipeToDeleteAdapter.ViewHolder(itemView)

    interface onViewClick {
        fun onAprroveClick()

    }


}