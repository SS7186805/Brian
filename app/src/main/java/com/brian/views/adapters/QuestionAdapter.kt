package com.brian.views.adapters


import android.content.Context
import android.widget.AdapterView
import com.brian.databinding.LayoutQuestionRecyclerItemBinding
import com.brian.internals.`interface`.ItemClickListener
import com.brian.models.AnswersItem

class QuestionAdapter(override val layoutId: Int,private var clickListener: ItemClickListener) :
    BaseRecyclerAdapter<LayoutQuestionRecyclerItemBinding, AnswersItem>() {
    override fun bind(holder: ViewHolder, item: AnswersItem, position: Int) {
        holder.binding.questionSet=item

        holder.itemView.setOnClickListener{
            clickListener.onClick(it,position)
        }
    }
}