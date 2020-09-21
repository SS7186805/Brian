package com.brian.views.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.brian.R
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter

class SwipeToDeleteAdapter(dataSet: List<MessageData> = emptyList())
    : DragDropSwipeAdapter<MessageData, SwipeToDeleteAdapter.ViewHolder>(dataSet) {

    var listener: onViewClick? = null


    class ViewHolder(itemView: View) : DragDropSwipeAdapter.ViewHolder(itemView) {

    }

    override fun onBindViewHolder(item: MessageData, viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.setOnClickListener{
            listener?.onAprroveClick()
        }
    }



    override fun getViewToTouchToStartDraggingItem(item: MessageData, viewHolder: SwipeToDeleteAdapter.ViewHolder, position: Int): View? {
        // We return the view holder's view on which the user has to touch to drag the item
        return viewHolder.itemView.findViewById(R.id.tvTime)
    }

    override fun getViewHolder(itemView: View): ViewHolder = SwipeToDeleteAdapter.ViewHolder(itemView)

    interface onViewClick {
        fun onAprroveClick()

    }
}