package com.brian.views.adapters

import androidx.recyclerview.widget.GridLayoutManager
import com.brian.R
import com.brian.databinding.QuestionItemBinding
import com.brian.models.AnswersItem
import com.brian.models.SubmittedAnswersItem

class GameSummaryAdapter(override val layoutId: Int) :
    BaseRecyclerAdapter<QuestionItemBinding, SubmittedAnswersItem>() {
    private lateinit var questionSummaryAdapter: QuestionSummaryAdapter
    override fun bind(holder: ViewHolder, item: SubmittedAnswersItem, position: Int) {

        holder.binding.gameSummary = item
        holder.binding.title.text = "Question ${position + 1}"

        var correctOption: String = ""
        for (i in 0 until item.question!!.answers?.size!!) {
            if (item.question!!.answers?.get(i)!!.isCorrect == 1) {
                correctOption = item.question.answers!![i]?.answer.toString()
                break
            }
        }

        holder.binding.correctOption.text = correctOption

        holder.binding.QuestionSummaryRecycler.layoutManager =
            GridLayoutManager(holder.binding.QuestionSummaryRecycler.context, 2)
        questionSummaryAdapter = QuestionSummaryAdapter(R.layout.layout_question_recycler_item)

        holder.binding.QuestionSummaryRecycler.adapter = questionSummaryAdapter
        questionSummaryAdapter.setNewItems((item.question!!.answers as List<AnswersItem>?)!!)


    }
}