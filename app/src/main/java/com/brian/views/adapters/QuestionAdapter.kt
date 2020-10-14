package com.brian.views.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import com.brian.databinding.LayoutQuestionRecyclerItemBinding
import com.brian.internals.interfaces.ItemClickListener
import com.brian.models.AnswersItem


const val iscorrect = 1

class QuestionAdapter(override val layoutId: Int, private var clickListener: ItemClickListener) :
    BaseRecyclerAdapter<LayoutQuestionRecyclerItemBinding, AnswersItem>() {
    private var selectedIndex = -1

    @SuppressLint("ResourceAsColor")
    override fun bind(holder: ViewHolder, item: AnswersItem, position: Int) {
        holder.binding.questionSet = item

        if (selectedIndex > -1) {
            if (item.isCorrect == iscorrect) {
                holder.binding.option.setCardBackgroundColor(Color.GREEN)
            } else if (position == selectedIndex) {
                holder.binding.option.setCardBackgroundColor(Color.RED)
            } else holder.binding.option.setCardBackgroundColor(Color.GRAY)
        }

        holder.itemView.setOnClickListener {
            if (selectedIndex == -1) {
                selectedIndex = position
                item.selected=item.answer
                item.selected=list[position].answer
                clickListener.onClick(list[position], position,item.isCorrect== iscorrect)
                notifyDataSetChanged()
            }
        }

    }
}
