package com.brian.views.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import com.brian.databinding.LayoutQuestionRecyclerItemBinding
import com.brian.models.AnswersItem


class QuestionSummaryAdapter(override val layoutId: Int) :
    BaseRecyclerAdapter<LayoutQuestionRecyclerItemBinding, AnswersItem>() {

    @SuppressLint("ResourceAsColor")
    override fun bind(holder: ViewHolder, item: AnswersItem, position: Int) {
        holder.binding.questionSet = item

        if (item.selected == item.answer) {
            if (item.isCorrect == 1) {
                holder.binding.option.setCardBackgroundColor(Color.GREEN)
            } else if (item.isCorrect == 0) {
                holder.binding.option.setCardBackgroundColor(Color.RED)
            }
        }
    }
}
