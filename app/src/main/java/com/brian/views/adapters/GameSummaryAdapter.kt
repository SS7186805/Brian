package com.brian.views.adapters

import androidx.recyclerview.widget.GridLayoutManager
import com.brian.R
import com.brian.databinding.QuestionItemBinding
import com.brian.internals.toArrayList
import com.brian.models.QuestionData

class GameSummaryAdapter(override val layoutId: Int) :
    BaseRecyclerAdapter<QuestionItemBinding, QuestionData>() {
    private lateinit var questionSummaryAdapter: QuestionSummaryAdapter
    override fun bind(holder: ViewHolder, item: QuestionData, position: Int) {
        holder.binding.gameSummary = item
        holder.binding.title.text ="Question ${position+1}"
        holder.binding.QuestionSummaryRecycler.layoutManager =
            GridLayoutManager(holder.binding.QuestionSummaryRecycler.context, 2)
        questionSummaryAdapter = QuestionSummaryAdapter(R.layout.layout_question_recycler_item)
        holder.binding.QuestionSummaryRecycler.adapter = questionSummaryAdapter
        questionSummaryAdapter.setNewItems(item.answers!!.toArrayList())

    }
}