package com.brian.views.adapters

import android.icu.text.Transliterator
import androidx.recyclerview.widget.GridLayoutManager
import com.brian.R
import com.brian.databinding.QuestionItemBinding
import com.brian.internals.toArrayList
import com.brian.models.AnswersItem
import com.brian.models.QuestionData

class GameSummaryAdapter(override val layoutId: Int) :
    BaseRecyclerAdapter<QuestionItemBinding, QuestionData>() {
    private lateinit var questionSummaryAdapter: QuestionSummaryAdapter
    override fun bind(holder: ViewHolder, item: QuestionData, position: Int) {
        holder.binding.gameSummary = item
        holder.binding.title.text = "Question ${position + 1}"

        var correctOption: Int = 0
        for (i in 0 until item.answers!!.size) {
            if (item.answers[i].isCorrect == 1) {
                correctOption = i+1
                break
            }
        }

        holder.binding.correctOption.text = "Option ${correctOption}"

        holder.binding.QuestionSummaryRecycler.layoutManager =
            GridLayoutManager(holder.binding.QuestionSummaryRecycler.context, 2)
        questionSummaryAdapter = QuestionSummaryAdapter(R.layout.layout_question_recycler_item)

        holder.binding.QuestionSummaryRecycler.adapter = questionSummaryAdapter
        questionSummaryAdapter.setNewItems(item.answers!!.toArrayList())


    }
}