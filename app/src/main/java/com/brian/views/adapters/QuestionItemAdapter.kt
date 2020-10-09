package com.brian.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brian.R
import com.brian.models.AnswersItem
import kotlinx.android.synthetic.main.questionitem.view.*

class QuestionItemAdapter:RecyclerView.Adapter<QuestionItemAdapter.QuestionHolder>() {
    var Qlist = ArrayList<AnswersItem>()

    fun setQuestion(list: ArrayList<AnswersItem>){
        this.Qlist = list
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.questionitem, parent, false)
        return QuestionHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionHolder, position: Int) {
      //  Qlist[position]?.let { holder.bind(it) }

        holder.bind(Qlist[position])
    }

    override fun getItemCount(): Int {
        return Qlist.size
    }

    class QuestionHolder(item: View):RecyclerView.ViewHolder(item) {

        var setAnswer = item.option1

        fun bind(answersItem: AnswersItem) {
            setAnswer.text =answersItem.answer
        }

    }
}